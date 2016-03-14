package com.xst.bigwhite.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.models.AccountDevice;
import com.xst.bigwhite.models.ConferenceAccount;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QAccountDevice;
import com.xst.bigwhite.models.QConference;
import com.xst.bigwhite.models.QConferenceAccount;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.models.QDeviceNote;

@Service("accountDeviceService")
public class AccountDeviceService {

	@PersistenceContext
    private EntityManager entityManager;

	public Iterable<ConferenceAccount> getAccountConferenceByDeviceno(String no) {
		QConferenceAccount qConferenceAccount = QConferenceAccount.conferenceAccount;
		QAccount qAccount = QAccount.account;
		QConference qConference = QConference.conference;
		QDevice qDevice = QDevice.device;
		BooleanExpression device = qDevice.no.eq(no);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<ConferenceAccount> accountdevices = query.from(qConferenceAccount)
				.leftJoin(qConferenceAccount.account, qAccount).fetch()
				.leftJoin(qConferenceAccount.conference, qConference).fetch()
				.leftJoin(qConference.device, qDevice).fetch()
				.where(device)
				.list(qConferenceAccount);

		return accountdevices;
	}

	
	public Iterable<AccountDevice> getAccountByDeviceno(String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QDevice qDevice = QDevice.device;
		QDeviceNote qDeviceNote = QDeviceNote.deviceNote;
		QAccount qAccount = QAccount.account;
		BooleanExpression bDeviceno = qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
				.leftJoin(qAccountDevice.account, qAccount).fetch()
				.leftJoin(qAccountDevice.device, qDevice).fetch()
				.where(bDeviceno)
				.list(qAccountDevice);

		return accountdevices;
	}
	
	
	public Iterable<AccountDevice> getAccountDeviceMaster(String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression deviceMobile = qDevice.no.eq(deviceno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(deviceMobile.and(qAccountDevice.devicemaster.eq(true)))
			 .list(qAccountDevice);
	
		return accountdevices;
	}
	
	public Iterable<AccountDevice> getAccountDevice(String mobileno , String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression accountMobile = qAccount.mobileno.eq(mobileno);
		BooleanExpression deviceMobile = qDevice.no.eq(deviceno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(accountMobile.and(deviceMobile))
			 .list(qAccountDevice);
	
		return accountdevices;
	}

	/*
	public static Iterable<AccountDevice> getAccountDevice(EntityManager entityManager,String mobileno , String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression accountMobile = qAccount.mobileno.eq(mobileno);
		BooleanExpression deviceMobile = qDevice.no.eq(deviceno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(accountMobile.and(deviceMobile))
			 .list(qAccountDevice);
	
		return accountdevices;
	}*/
	
	public Iterable<AccountDevice> getAccountDeviceByDeviceno(String no) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression device = qDevice.no.eq(no);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(device)
			 .list(qAccountDevice);
	
		return accountdevices;
	}

	
	public Iterable<AccountDevice> getAccountDeviceByAccountMobile(String mobileno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression account = qAccount.mobileno.eq(mobileno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(account)
			 .list(qAccountDevice);
	
		return accountdevices;
	}

	public Iterable<AccountDevice> getAccountDeviceMasterByDeviceNo(String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression deviceMobile = qDevice.no.eq(deviceno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(deviceMobile.and(qAccountDevice.devicemaster.eq(true)))
			 .list(qAccountDevice);
	
		return accountdevices;
	}

	public Iterable<ConferenceAccount> getAccountConferenceByAccountMobile(String mobileno) {
		QConferenceAccount qConferenceAccount = QConferenceAccount.conferenceAccount;
		QAccount qAccount = QAccount.account;
		QConference qConference = QConference.conference;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression account = qAccount.mobileno.eq(mobileno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<ConferenceAccount> accountdevices = query.from(qConferenceAccount)
			 .leftJoin(qConferenceAccount.account,qAccount).fetch()
			 .leftJoin(qConferenceAccount.conference,qConference).fetch()
			 .leftJoin(qConference.device,qDevice).fetch()
			 .where(account)
			 .list(qConferenceAccount);
	
		return accountdevices;
	}

	
}
