package com.xst.bigwhite.controllers;

import java.util.ArrayList;
import java.util.List;
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

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.daos.AccountDeviceRepository;
import com.xst.bigwhite.daos.AccountRepository;
import com.xst.bigwhite.daos.DeviceRepository;
import com.xst.bigwhite.daos.VerifyMessageRepository;
import com.xst.bigwhite.dtos.DeviceAccountInfo;
import com.xst.bigwhite.dtos.DeviceInfoResponse;
import com.xst.bigwhite.dtos.RegisterDeviceRequest;
import com.xst.bigwhite.dtos.RegisterDeviceResponse;
import com.xst.bigwhite.dtos.ScanDeviceRequest;
import com.xst.bigwhite.dtos.ScanDeviceResponse;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.AccountDevice;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QAccountDevice;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.utils.Helpers;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/device")
public class DeviceController {
	
	
	private final DeviceRepository deviceRepository;
	private final AccountRepository accountRepository;
	private final VerifyMessageRepository verifyMessageRepository;
	private final AccountDeviceRepository accountDeviceRepository;

	@Autowired
	DeviceController(AccountRepository accountRepository,
			DeviceRepository deviceRepository,
			VerifyMessageRepository verifyMessageRepository,
			AccountDeviceRepository accountDeviceRepository) {
		this.deviceRepository = deviceRepository;
		this.accountRepository = accountRepository;
		this.verifyMessageRepository = verifyMessageRepository;
		this.accountDeviceRepository = accountDeviceRepository;
	}
	
	/**
	 * 注册设备
	 * @param RegisterDeviceRequest
	 * @return RegisterDeviceResponse
	 */
    @RequestMapping(value = "/registry",method = RequestMethod.POST)
    @ResponseBody RegisterDeviceResponse registryDevice(@RequestBody RegisterDeviceRequest input) {
    	RegisterDeviceResponse response = new RegisterDeviceResponse();
    	Optional<Device> device = deviceRepository.findTop1BySn(input.sn);
    	
        if(device.isPresent()){
        	response.deviceno = device.get().no;
        	Device deviceRegister = device.get();
        	deviceRegister.setName(input.devicename);
        	deviceRepository.save(deviceRegister);
        }else{
        	Device deviceRegister = new Device(input.devicename,input.sn,input.mac);
  
        	Device registered = deviceRepository.save(deviceRegister);
        	String deviceno = Helpers.getDeviceNo(registered.getId());
        	
        	registered.setNo(deviceno);
        	deviceRepository.save(registered);
        	
        	response.setDeviceno(deviceno);
        }
        
    	return response;
    }
    

    /**
     * 扫描二维码查询设备信息
     * @param ScanDeviceRequest
     * @return ScanDeviceResponse
     */
	 @RequestMapping(value = "/scanQR",method = RequestMethod.POST)
	    @ResponseBody ScanDeviceResponse scanDeviceQR(@RequestBody ScanDeviceRequest input) {
	    	ScanDeviceResponse response = new ScanDeviceResponse();
	    	Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
	    	if(deviced.isPresent()){
	    		Device device = deviced.get();

	    		response = ScanDeviceResponse.mapping(device);
	    	}else{
	    		throw new RestRuntimeException("设备号:" + input.getDeviceno() + "没有注册或者不存在!");
	    	}
	    	
	    	
	    	return response;
	    }

	 /**
	  * 查询设备关联的账户信息
	  * @param ScanDeviceRequest
	  * @return DeviceInfoResponse
	  */
	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	@ResponseBody
	DeviceInfoResponse searchAccounts(@RequestBody ScanDeviceRequest input) {
		DeviceInfoResponse response = new DeviceInfoResponse();

		String deviceno = input.getDeviceno();
		Optional<Device> deviced = deviceRepository.findTop1Byno(deviceno);
		if (deviced.isPresent()) {
			Device device = deviced.get();
			response.setDevicename(device.getName());
			response.setDeviceno(deviceno);
			
            Iterable<AccountDevice> accountList = getAccountByDeviceno(deviceno);
            
            List<DeviceAccountInfo> accounts =new ArrayList<>();
            response.setAccounts(accounts);
            
            accountList.forEach((ac)->{
            	//if(ac.confirmed){
            		DeviceAccountInfo accountInfo = DeviceAccountInfo.mapping(ac);
            		accounts.add(accountInfo);
            	//}
            });
            
		} else {
			throw new RestRuntimeException("设备号:" + input.getDeviceno() + "没有注册或者不存在!");
		}

		return response;
	}

	@PersistenceContext
    private EntityManager entityManager;
	
	
	private Iterable<AccountDevice> getAccountByDeviceno(String deviceno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QDevice qDevice = QDevice.device;
		QAccount qAccount = QAccount.account;
		BooleanExpression bDeviceno = qDevice.no.eq(deviceno);
		//Iterable<AccountDevice> accountList = accountDeviceRepository.findAll(bDeviceno);
		
		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
				 .leftJoin(qAccountDevice.account,qAccount).fetch()
				 .leftJoin(qAccountDevice.device,qDevice).fetch()
				 .where(bDeviceno)
				 .list(qAccountDevice);
				 
		return accountdevices;
	}
	    
}
