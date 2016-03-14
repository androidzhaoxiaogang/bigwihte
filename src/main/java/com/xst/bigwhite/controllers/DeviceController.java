
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
import com.xst.bigwhite.dtos.AccountDeviceInfo;
import com.xst.bigwhite.dtos.AccountInfoRequest;
import com.xst.bigwhite.dtos.ConferenceAccountRequest;
import com.xst.bigwhite.dtos.ConferenceAccountResponse;
import com.xst.bigwhite.dtos.ConferenceDeviceRequest;
import com.xst.bigwhite.dtos.ConfirmAccountRequest;
import com.xst.bigwhite.dtos.DeviceAccountInfo;
import com.xst.bigwhite.dtos.DeviceInfoRequest;
import com.xst.bigwhite.dtos.DeviceInfoResponse;
import com.xst.bigwhite.dtos.RegisterDeviceRequest;
import com.xst.bigwhite.dtos.RegisterDeviceResponse;
import com.xst.bigwhite.dtos.ScanDeviceRequest;
import com.xst.bigwhite.dtos.ScanDeviceResponse;
import com.xst.bigwhite.dtos.ScanInputType;
import com.xst.bigwhite.dtos.UpdateDeviceInfoRequest;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.AccountDevice;
import com.xst.bigwhite.models.ConferenceAccount;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QAccountDevice;
import com.xst.bigwhite.models.QConference;
import com.xst.bigwhite.models.QConferenceAccount;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.utils.Helpers;
import com.xst.bigwhite.utils.RepositoryHelper;

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
	
	@PersistenceContext
	private EntityManager entityManager;


	/**
	 * 注册设备
	 * 
	 * @param RegisterDeviceRequest
	 * @return RegisterDeviceResponse
	 */
	@RequestMapping(value = "/registry", method = RequestMethod.POST)
	@ResponseBody
	RegisterDeviceResponse registryDevice(@RequestBody RegisterDeviceRequest input) {
		RegisterDeviceResponse response = new RegisterDeviceResponse();
		Optional<Device> device = deviceRepository.findTop1BySn(input.sn);

		if (device.isPresent()) {
			response.deviceno = device.get().no;
			Device deviceRegister = device.get();
			deviceRegister.setName(input.devicename);
			deviceRepository.save(deviceRegister);
		} else {
			Device deviceRegister = new Device(input.devicename, input.sn, input.mac);

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
	 * 
	 * @param ScanDeviceRequest
	 * @return ScanDeviceResponse
	 */
	@RequestMapping(value = "/scanQR", method = RequestMethod.POST)
	@ResponseBody
	ScanDeviceResponse scanDeviceQR(@RequestBody ScanDeviceRequest input) {
		ScanDeviceResponse response = new ScanDeviceResponse();
		Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
		if (deviced.isPresent()) {
			Device device = deviced.get();

			response = ScanDeviceResponse.mapping(device);
		} else {
			throw new RestRuntimeException("设备号:" + input.getDeviceno() + "没有注册或者不存在!");
		}

		response.setScanType(input.getScanType() == null ? ScanInputType.ScanQR : input.getScanType());
		return response;
	}

	/**
	 * 修改设备的用户或者昵称
	 * 
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/updateDeviceName", method = RequestMethod.POST)
	@ResponseBody
	Boolean updateDeviceName(@RequestBody UpdateDeviceInfoRequest input) {

		Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
		if (deviced.isPresent()) {
			Device device = deviced.get();
			if (!device.name.equals(input.devicename)) {
				device.name = input.devicename;
				deviceRepository.save(device);
			} 
		} else {
			throw new RestRuntimeException("设备名称" + input.getDeviceno() + "不存在!");
		}

		return true;
	}

	/**
	 * 更新大白管理元
	 * 
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/setMaster", method = RequestMethod.POST)
	@ResponseBody
	Boolean setMaster(@RequestBody final AccountInfoRequest input) {
		if(input.mobileno==null || input.deviceno==null){
			throw new RestRuntimeException("设备名称或用户名不能未空!");
		}
		
		Iterable<AccountDevice> accountDeviced = RepositoryHelper.getAccountDevice(entityManager,input.mobileno,input.deviceno);
		if(accountDeviced.iterator().hasNext()){
		  Iterable<AccountDevice> masterAccounted = RepositoryHelper.getAccountDeviceMasterByDeviceNo(entityManager,input.deviceno); 
		  if(masterAccounted.iterator().hasNext()){
			  AccountDevice masterAccount = masterAccounted.iterator().next();
			  masterAccount.setDevicemaster(false);
			  accountDeviceRepository.save(masterAccount);
		  }
			
		  AccountDevice accountDevice = accountDeviced.iterator().next();
		  accountDevice.setDevicemaster(true);
		  accountDeviceRepository.save(accountDevice);
		}else{
			throw new RestRuntimeException("设备名称" + input.getDeviceno() + "用户名" + input.mobileno + "没有绑定!");
		}
		
		return true;
	}
	
	
	/**
	 * 查询设备关联的账户信息
	 * 
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

			Iterable<AccountDevice> accountList = RepositoryHelper.getAccountByDeviceno(entityManager,deviceno);

			List<DeviceAccountInfo> accounts = new ArrayList<>();
			response.setAccounts(accounts);

			accountList.forEach((ac) -> {
				// if(ac.confirmed){
				DeviceAccountInfo accountInfo = DeviceAccountInfo.mapping(ac);
				accounts.add(accountInfo);
				// }
			});

		} else {
			throw new RestRuntimeException("设备号:" + input.getDeviceno() + "没有注册或者不存在!");
		}

		return response;
	}

	/**
	 * 查询是否审核通过设备(申请的绑定到指定的设备列表 )
	 * 
	 * @param ConfirmAccountRequest
	 * @return ArrayList<AccountDeviceInfo>
	 */
	@RequestMapping(value = "/confirms", method = RequestMethod.POST)
	@ResponseBody
	ArrayList<AccountDeviceInfo> confirmAccounts(@RequestBody DeviceInfoRequest input) {

		ArrayList<AccountDeviceInfo> accountDeviceInfoes = new ArrayList<AccountDeviceInfo>();

		Iterable<AccountDevice> accounts = RepositoryHelper.getAccountByDeviceno(entityManager,input.getDeviceno());
		if (accounts != null && accounts.iterator().hasNext()) {
			for (AccountDevice accountDeviceInfo : accounts) {

				AccountDeviceInfo item = AccountDeviceInfo.mapping(accountDeviceInfo);
				accountDeviceInfoes.add(item);

			}
		}

		return accountDeviceInfoes;

	}

	/**
	 * 查询当设备下所有的会议信息
	 * 
	 * @param ConferenceDeviceRequest
	 * @return ArrayList<ConferenceAccountResponse>
	 */
	@RequestMapping(value = "/conferences", method = RequestMethod.POST)
	@ResponseBody
	List<ConferenceAccountResponse> deviceConferences(@RequestBody ConferenceDeviceRequest input) {
		List<ConferenceAccountResponse> response = new ArrayList<ConferenceAccountResponse>();

		Iterable<ConferenceAccount> conferences = RepositoryHelper.getAccountConferenceByDeviceno(entityManager,input.deviceno);

		if (conferences != null && conferences.iterator().hasNext()) {
			for (ConferenceAccount conference : conferences) {
				ConferenceAccountResponse item = ConferenceAccountResponse.mapping(conference);
				response.add(item);
			}
		} else {
			throw new RestRuntimeException("设备:" + input.deviceno + "会议不存在!");
		}

		return response;
	}

	
}
