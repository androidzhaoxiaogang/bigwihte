package com.xst.bigwhite.controllers;

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

import com.xst.bigwhite.utils.SMSManager;

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
		
		return null;
    }
	
}
