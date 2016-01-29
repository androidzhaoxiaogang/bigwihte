package com.xst.bigwhite.controllers;

import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xst.bigwhite.daos.AccountRepository;
import com.xst.bigwhite.daos.ConferenceAccountRepository;
import com.xst.bigwhite.daos.ConferenceRecordRepository;
import com.xst.bigwhite.daos.ConferenceRepository;
import com.xst.bigwhite.dtos.RegisterConferenceRequest;
import com.xst.bigwhite.dtos.RegisterConferenceResponse;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.Conference;
import com.xst.bigwhite.models.ConferenceOperatorType;
import com.xst.bigwhite.models.ConferenceRecord;
import com.xst.bigwhite.utils.SMSManager;
import com.xst.bigwhite.utils.UUIDGenerator;

@SuppressWarnings("unused")
@Controller
@EnableAutoConfiguration
@RequestMapping("/api/conference")
public class ConferenceController {
	
	private final AccountRepository accountRepository;
	private final ConferenceRepository conferenceRepository;
	private final ConferenceAccountRepository conferenceAccountRepository;
	private final ConferenceRecordRepository conferenceRecordRepository;
	
	@Autowired
	public ConferenceController(AccountRepository accountRepository,
			ConferenceRepository conferenceRepository,
			ConferenceAccountRepository conferenceAccountRepository,
			ConferenceRecordRepository conferenceRecordRepository){
		this.accountRepository = accountRepository;
		this.conferenceRepository = conferenceRepository;
		this.conferenceAccountRepository = conferenceAccountRepository;
		this.conferenceRecordRepository = conferenceRecordRepository;
	}
	
	
	@PersistenceContext
    private EntityManager entityManager;

	/*@Autowired
	SMSManager smsManager;*/
	
	 /* 手机申请会议
	 * 
	 * @param RegisterConferenceRequest
	 * @return RegisterConferenceResponse
	 */
	@RequestMapping(value = "/registry", method = RequestMethod.POST)
	@ResponseBody
	RegisterConferenceResponse registryConference(@RequestBody RegisterConferenceRequest input) {
		RegisterConferenceResponse response = new RegisterConferenceResponse();
		
		String mobileno = input.mobileno;
		String sessionId = input.sessionId;
		String sessionName = input.sessionname;
		
		response.setMobileno(mobileno);
		
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(mobileno);
		if(accounted.isPresent()){
			Account account = accounted.get();
			if(sessionId!=null && sessionId.trim().length()>0){  //会议已经存在
				Optional<Conference> conferenced = conferenceRepository.findTop1BySessionId(sessionId);
				if(conferenced.isPresent()){
					Conference conference = conferenced.get();
					if(!conference.getCreateBy().mobileno.equals(mobileno)){
						throw new RestRuntimeException("会议会话:" + input.sessionId + "会议创建人为:" + conference.getCreateBy().mobileno);
					}else{
						conference.setSessionname(sessionName);
						conference.setActiveTime(new Date());
						conferenceRepository.save(conference);
						
						ConferenceRecord conferenceRecord = new ConferenceRecord(conference,account,ConferenceOperatorType.REOPENSESSION);
						conferenceRecordRepository.save(conferenceRecord);
						
						response.setSessionId(conference.getSessionId());
						response.setSessionname(conference.getSessionname());
					}
				}else{
					throw new RestRuntimeException("会议会话:" + input.sessionId + "不存在!");
				}
			}else{   //建立新的会议
				sessionId =  UUIDGenerator.getUUID();
				Conference conference = new Conference(sessionId,sessionName,account);
				Conference regConference = conferenceRepository.save(conference);
				
				ConferenceRecord conferenceRecord = new ConferenceRecord(conference,account,ConferenceOperatorType.CREATESESSION);
				conferenceRecordRepository.save(conferenceRecord);
				
				response.setSessionId(sessionId);
				response.setSessionname(sessionName);
			}
			
		}else{
			throw new RestRuntimeException("账户" + input.mobileno + "不存在!");
		}
		
		return response;
    }
	
}
