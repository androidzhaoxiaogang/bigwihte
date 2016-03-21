package com.xst.bigwhite.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.models.DeviceBind;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.models.QDeviceBind;

@Service("deviceBindService")
public class DeviceBindService {

	@PersistenceContext
    private EntityManager entityManager;

	
	public Iterable<DeviceBind> findDeviceBindByDevice(String deviceno, String joindeviceno) {
		QDeviceBind qDeviceBind = QDeviceBind.deviceBind;
		
		//QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		//QDevice qDevice2 = QDevice.device;
		
		BooleanExpression bDevice =  qDevice.no.eq(deviceno);
		BooleanExpression bDevice2 = qDevice.no.eq(joindeviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<DeviceBind> deviceBinds = query.from(qDeviceBind)
				                                     .leftJoin(qDeviceBind.device,qDevice).fetch()
				                                      //.leftJoin(qDeviceBind.binded,qDevice2).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bDevice.and(bDevice2))
				                                     .list(qDeviceBind);

		return deviceBinds;
	}

	public Iterable<DeviceBind> findDeviceBindByDeviceno(String deviceno) {
		
		QDeviceBind qDeviceBind = QDeviceBind.deviceBind;
		
		//QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		QDevice qDevice2 = QDevice.device;
		
		BooleanExpression bDevice =  qDevice.no.eq(deviceno);
		// Iterable<AccountDevice> accountList =
		// accountDeviceRepository.findAll(bDeviceno);
		JPAQuery query = new JPAQuery(entityManager);
		
		Iterable<DeviceBind> deviceBinds = query.from(qDeviceBind)
				                                     .leftJoin(qDeviceBind.device,qDevice).fetch()
				                                     //.leftJoin(qDeviceBind.binded,qDevice2).fetch()
				                                     //.leftJoin(qAccountNote.contact,qAccount2).fetch()
				                                     .where(bDevice)
				                                     .list(qDeviceBind);

		return deviceBinds;
	}

}
