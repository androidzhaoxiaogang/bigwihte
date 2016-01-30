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
import com.xst.bigwhite.daos.DeviceRepository;
import com.xst.bigwhite.dtos.JoinConferenceRequest;
import com.xst.bigwhite.dtos.RegisterConferenceRequest;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.Conference;
import com.xst.bigwhite.models.ConferenceOperatorType;
import com.xst.bigwhite.models.ConferenceRecord;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.utils.SMSManager;
import com.xst.bigwhite.utils.UUIDGenerator;

@SuppressWarnings("unused")
@Controller
@EnableAutoConfiguration
@RequestMapping("/api/conference")
public class ConferenceController {
	
	private final AccountRepository accountRepository;
	private final DeviceRepository deviceRepository;
	private final ConferenceRepository conferenceRepository;
	private final ConferenceAccountRepository conferenceAccountRepository;
	private final ConferenceRecordRepository conferenceRecordRepository;
	
	@Autowired
	public ConferenceController(AccountRepository accountRepository,
			DeviceRepository deviceRepository,
			ConferenceRepository conferenceRepository,
			ConferenceAccountRepository conferenceAccountRepository,
			ConferenceRecordRepository conferenceRecordRepository){
		this.accountRepository = accountRepository;
		this.deviceRepository = deviceRepository;
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
						conferenceRepository.save(conference);
						
						ConferenceRecord conferenceRecord = new ConferenceRecord(conference,device,ConferenceOperatorType.REOPEN);
						conferenceRecordRepository.save(conferenceRecord);
				}else{   //建立新的会议
					Conference conference = new Conference(sessionId,sessionName,device);
					conference.setActiveTime(new Date());
					Conference regConference = conferenceRepository.save(conference);
					
					ConferenceRecord conferenceRecord = new ConferenceRecord(conference,device,ConferenceOperatorType.CREATE);
					conferenceRecordRepository.save(conferenceRecord);
				   
				}
			
		}else{
			throw new RestRuntimeException("设备号:" + input.deviceno + "不存在!");
		}
		
		return true;
    }
	
	/**
	 * 参加会议
	 * @param input
	 * @return
	 */
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	@ResponseBody
	Boolean joinConference(@RequestBody JoinConferenceRequest input) {
		
	
	   return true;
	}
	
}
