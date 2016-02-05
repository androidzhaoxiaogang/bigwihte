package com.xst.bigwhite.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.BigwhiteApplication;
import com.xst.bigwhite.daos.AccountRepository;
import com.xst.bigwhite.daos.ConferenceAccountRecordRepository;
import com.xst.bigwhite.daos.ConferenceAccountRepository;
import com.xst.bigwhite.daos.ConferenceRecordRepository;
import com.xst.bigwhite.daos.ConferenceRepository;
import com.xst.bigwhite.daos.DeviceRepository;
import com.xst.bigwhite.dtos.AccountInfo;
import com.xst.bigwhite.dtos.ConferenceAccountRequest;
import com.xst.bigwhite.dtos.ConferenceAccountResponse;
import com.xst.bigwhite.dtos.ConferenceActionRequest;
import com.xst.bigwhite.dtos.ConferenceRequest;
import com.xst.bigwhite.dtos.ConferenceResponse;
import com.xst.bigwhite.dtos.ConferenceUpdateRequest;
import com.xst.bigwhite.dtos.JoinConferenceRequest;
import com.xst.bigwhite.dtos.RegisterConferenceRequest;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.Conference;
import com.xst.bigwhite.models.ConferenceAccount;
import com.xst.bigwhite.models.ConferenceAccountActionType;
import com.xst.bigwhite.models.ConferenceAccountRecord;
import com.xst.bigwhite.models.ConferenceAccountStatusType;
import com.xst.bigwhite.models.ConferenceOperatorType;
import com.xst.bigwhite.models.ConferenceRecord;
import com.xst.bigwhite.models.ConferenceStatusType;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QConference;
import com.xst.bigwhite.models.QConferenceAccount;
import com.xst.bigwhite.models.QConferenceAccountRecord;
import com.xst.bigwhite.models.QDevice;


@Controller
@EnableAutoConfiguration
@RequestMapping("/api/conference")
public class ConferenceController {
	
	private final AccountRepository accountRepository;
	private final DeviceRepository deviceRepository;
	private final ConferenceRepository conferenceRepository;
	private final ConferenceAccountRepository conferenceAccountRepository;
	private final ConferenceRecordRepository conferenceRecordRepository;
	private final ConferenceAccountRecordRepository conferenceAccountRecordRepository;
	
	private static final Logger log = LoggerFactory.getLogger(BigwhiteApplication.class);

	@Autowired
	public ConferenceController(AccountRepository accountRepository,
			DeviceRepository deviceRepository,
			ConferenceRepository conferenceRepository,
			ConferenceAccountRepository conferenceAccountRepository,
			ConferenceRecordRepository conferenceRecordRepository,
			ConferenceAccountRecordRepository conferenceAccountRecordRepository){
		this.accountRepository = accountRepository;
		this.deviceRepository = deviceRepository;
		this.conferenceRepository = conferenceRepository;
		this.conferenceAccountRepository = conferenceAccountRepository;
		this.conferenceRecordRepository = conferenceRecordRepository;
		this.conferenceAccountRecordRepository = conferenceAccountRecordRepository;
	}
	
	
	@PersistenceContext
    private EntityManager entityManager;

	/*@Autowired
	SMSManager smsManager;*/
	
	 /** 大白申请会议
	 * 
	 * @param RegisterConferenceRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/registry", method = RequestMethod.POST)
	@ResponseBody
	Boolean registryConference(@RequestBody RegisterConferenceRequest input) {
		String deviceno = input.deviceno;
		String ui = input.ui;
		String sessionId = input.sessionId;
		String sessionName = input.sessionname;
		
		Optional<Device> deviced = deviceRepository.findTop1Byno(deviceno);
		if(deviced.isPresent()){
			Device device = deviced.get();
			
				Optional<Conference> conferenced = conferenceRepository.findTop1BySessionIdAndUi(sessionId, ui);
				if(conferenced.isPresent()){
						Conference conference = conferenced.get();
						//conference.setSessionname(sessionName);
						conference.setActiveTime(new Date());
						conference.setStatus(ConferenceStatusType.OPENED);
						conferenceRepository.save(conference);
						
						ConferenceRecord conferenceRecord = new ConferenceRecord(conference,device,ConferenceOperatorType.OPEN);
						conferenceRecordRepository.save(conferenceRecord);
				}else{   //建立新的会议
					Conference conference = new Conference(sessionId,ui,sessionName,device);
					conference.setActiveTime(new Date());
					conference.setStatus(ConferenceStatusType.OPENED);
					Conference regConference = conferenceRepository.save(conference);
					
					ConferenceRecord conferenceRecord = new ConferenceRecord(regConference,device,ConferenceOperatorType.CREATE);
					conferenceRecordRepository.save(conferenceRecord);
				   
				}
			
		}else{
			throw new RestRuntimeException("设备号:" + input.deviceno + "不存在!");
		}
		
		return true;
    }
	
	 /** 手机申请加入会议
	 * 
	 * @param JoinConferenceRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	@ResponseBody
	Boolean joinConference(@RequestBody JoinConferenceRequest input) {
		Optional<Conference> conferenced = conferenceRepository.findTop1BySessionIdAndUi(input.sessionId, input.ui);
		if(conferenced.isPresent()){
			Conference conference = conferenced.get();
			Iterable<ConferenceAccount> accounts = getAccountConferenceByAccount(input.mobileno,input.sessionId, input.ui);
			if(accounts!=null && accounts.iterator().hasNext()){
				ConferenceAccount account = accounts.iterator().next();
				account.setUpdateDate(new Date());
				account.setStatus(ConferenceAccountStatusType.ONLINE);
				conferenceAccountRepository.save(account);
			}else{
				Optional<Account> accounted = accountRepository.findTop1ByMobileno(input.mobileno);
				if(accounted.isPresent()){
					ConferenceAccount account = new ConferenceAccount(conference,accounted.get());
					account.setStatus(ConferenceAccountStatusType.ONLINE);
					conferenceAccountRepository.save(account);
				}else{
					throw new RestRuntimeException("账户:" + input.mobileno + "不存在!");
				}
			}
			
		}else{
			throw new RestRuntimeException("会议号:" + input.sessionId + "不存在!");
		}
		return true;
	}
	
	/**
	 * 会议状态信息 包括用户的信息和会议状态
	 * 
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	@ResponseBody
	Boolean updateStatusConference(@RequestBody ConferenceUpdateRequest input) {
		Optional<Conference> conferenced = conferenceRepository.findTop1BySessionIdAndUi(input.sessionId, input.ui);
		if (conferenced.isPresent()) {
		
			Conference conference = conferenced.get();
			
			conference.setStatus(input.status);
			if(input.totalMinutes>0){
				conference.setTotalMinutes(input.totalMinutes);
			}
			conferenceRepository.save(conference);

			ConferenceRecord conferenceRecord = new ConferenceRecord(conference,conference.getDevice(),
					input.status != ConferenceStatusType.OPENED ? ConferenceOperatorType.CLOSE :ConferenceOperatorType.OPEN );
			conferenceRecordRepository.save(conferenceRecord);
				
			Set<ConferenceAccount> conferenceAccounts = conference.getAccounts();
			if (conferenceAccounts != null && !conferenceAccounts.isEmpty()) {
				for (ConferenceAccount conferenceAccount : conferenceAccounts) {
					
					ConferenceAccountActionType conferenceAccountActionType;
					if (input.status != ConferenceStatusType.OPENED) {
						conferenceAccountActionType = ConferenceAccountActionType.ABORT;
						conferenceAccount.setStatus(ConferenceAccountStatusType.OFFLINE);
						
					} else {
						conferenceAccount.setStatus(ConferenceAccountStatusType.ONLINE);
						conferenceAccountActionType = ConferenceAccountActionType.RECONNECT;
					}
				
					conferenceAccountRepository.save(conferenceAccount);
					
					ConferenceAccountRecord recordAccount = new ConferenceAccountRecord(conference,conferenceAccount.getAccount(),conferenceAccountActionType);
					conferenceAccountRecordRepository.save(recordAccount);
				}
			}
		} else {
			throw new RestRuntimeException("会议号:" + input.sessionId + "不存在!");
		}

		return true;
	}
	
	
	/**
	 * 一个会议的详细信息 
	 * 会议名称 参与的用户等等
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/details", method = RequestMethod.POST)
	@ResponseBody
	ConferenceResponse detailsOfConference(@RequestBody ConferenceRequest input) {
		String ui = input.ui;
		String sessionId = input.sessionId;
		
		ConferenceResponse  response = new ConferenceResponse();
		Optional<Conference> conferenced = conferenceRepository.findTop1BySessionIdAndUi(sessionId, ui);
		if (conferenced.isPresent()) {
			Conference conference = conferenced.get();
			response = ConferenceResponse.mapping(conference);
			
			List<AccountInfo> accounts = AccountInfo.mappingList(conference.getAccounts());
			response.setAccounts(accounts);
		}else{
			throw new RestRuntimeException("会议号:" + input.ui + "不存在!");
		}
		
		return response;
	}
	
	
	/**
	 * 用户参与一个会议的详细信息 
	 * 会议名称 参与的用户等等
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/accountDetails", method = RequestMethod.POST)
	@ResponseBody
	List<ConferenceAccountResponse> detailsAccountOfConference(@RequestBody ConferenceAccountRequest input) {
		List<ConferenceAccountResponse>  response = new ArrayList<ConferenceAccountResponse>();
		
		Iterable<ConferenceAccount> conferences = getAccountConferenceByAccount(input.mobileno,input.sessionId, input.ui);
		if (conferences!=null && conferences.iterator().hasNext()) {
			for(ConferenceAccount conference : conferences){
				ConferenceAccountResponse item = ConferenceAccountResponse.mapping(conference);
				response.add(item);
			}
		}else{
			throw new RestRuntimeException("用户:" + input.mobileno + "会议不存在!");
		}
		
		return response;
	}
	


	/**
	 * 修改参加会议信息
	 * 包括用户的信息和会议状态
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/accountAction", method = RequestMethod.POST)
	@ResponseBody
	Boolean accountActionConference(@RequestBody ConferenceActionRequest input) {
		Optional<Conference> conferenced = conferenceRepository.findTop1BySessionIdAndUi(input.sessionId, input.ui);
		if (conferenced.isPresent()) {
			Iterable<ConferenceAccount> conference =  getAccountConferenceByAccount(input.mobileno,input.sessionId, input.ui);
			if(conference!=null && conference.iterator().hasNext()){
				ConferenceAccount account = conference.iterator().next();
				
				ConferenceAccountRecord recordAccount = new ConferenceAccountRecord(account.getConference(),account.getAccount(),input.actionType);
				recordAccount.setBeginDate(input.getBeginDate());
				recordAccount.setEndDate(input.endDate);
				recordAccount.setMinutes(input.minutes);
				recordAccount.setOperatorDesc(input.operatorDesc);
				
				conferenceAccountRecordRepository.save(recordAccount);
				
			    Integer totalMinutes = getSumAccountConferenceByAccount(input.mobileno,input.sessionId, input.ui);
			    account.setTotalMinutes(totalMinutes);
			    conferenceAccountRepository.save(account);
			}
		}else{
			throw new RestRuntimeException("会议号: " +  input.sessionId + " 不存在!");
		}
	
	   return true;
	}
	
	private Iterable<ConferenceAccount> getAccountConferenceByAccount(String mobileno,String sessionId,String ui) {
		QConferenceAccount qConferenceAccount = QConferenceAccount.conferenceAccount;
		QAccount qAccount = QAccount.account;
		QConference qConference = QConference.conference;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression account = qAccount.mobileno.eq(mobileno);
		BooleanExpression conference = qConference.sessionId.eq(sessionId).and(qConference.ui.eq(ui));

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<ConferenceAccount> accountdevices = query.from(qConferenceAccount)
			 .leftJoin(qConferenceAccount.account,qAccount).fetch()
			 .leftJoin(qConferenceAccount.conference,qConference).fetch()
			 .leftJoin(qConference.device,qDevice).fetch()
			 .where(account.and(conference))
			 .list(qConferenceAccount);
	
		return accountdevices;
	}
	
	private Integer getSumAccountConferenceByAccount(String mobileno,String sessionId,String ui) {
		QConferenceAccountRecord qConferenceAccountRecord = QConferenceAccountRecord.conferenceAccountRecord;
		QAccount qAccount = QAccount.account;
		QConference qConference = QConference.conference;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression account = qAccount.mobileno.eq(mobileno);
		BooleanExpression conference = qConference.sessionId.eq(sessionId).and(qConference.ui.eq(ui));

		JPAQuery query = new JPAQuery(entityManager);
		Integer sum = query.from(qConferenceAccountRecord)
			 .leftJoin(qConferenceAccountRecord.account,qAccount).fetch()
			 .leftJoin(qConferenceAccountRecord.conference,qConference).fetch()
			 .leftJoin(qConference.device,qDevice).fetch()
			 .where(account.and(conference))
			 .createQuery(qConferenceAccountRecord.minutes.sum())
			 .getFirstResult();
	
		return sum;
	}
}
