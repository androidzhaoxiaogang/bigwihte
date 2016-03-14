package com.xst.bigwhite.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.models.DeviceNote;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.models.QDeviceNote;

@Service("deviceNoteService")
public class DeviceNoteService {
	
	@PersistenceContext
    private EntityManager entityManager;

	public Iterable<DeviceNote> findAcountNoteByDeviceAndMobile(String deviceno, String mobileno) {
		
		QDeviceNote qDeviceNote = QDeviceNote.deviceNote;
		
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression bAccount = qAccount.mobileno.eq(mobileno);
		BooleanExpression bDevice = qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<DeviceNote> deviceNotes = query.from(qDeviceNote)
				                                     .leftJoin(qDeviceNote.account,qAccount).fetch()
				                                     .leftJoin(qDeviceNote.device,qDevice).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bAccount.and(bDevice))
				                                     .list(qDeviceNote);

		return deviceNotes;
	}

	public Iterable<DeviceNote> findAcountNoteByDevice(String deviceno) {

		QDeviceNote qDeviceNote = QDeviceNote.deviceNote;
		
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;

		BooleanExpression bDevice = qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<DeviceNote> deviceNotes = query.from(qDeviceNote)
				                                     .leftJoin(qDeviceNote.account,qAccount).fetch()
				                                     .leftJoin(qDeviceNote.device,qDevice).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bDevice)
				                                     .list(qDeviceNote);

		return deviceNotes;
	}

}
