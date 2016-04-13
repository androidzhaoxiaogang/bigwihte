
package com.xst.bigwhite.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xst.bigwhite.daos.AccountDeviceRepository;
import com.xst.bigwhite.daos.AccountRepository;
import com.xst.bigwhite.daos.DeviceBindRepository;
import com.xst.bigwhite.daos.DeviceNoteRepository;
import com.xst.bigwhite.daos.DeviceRepository;
import com.xst.bigwhite.daos.VerifyMessageRepository;
import com.xst.bigwhite.dtos.AccountDeviceInfo;
import com.xst.bigwhite.dtos.AccountInfoRequest;
import com.xst.bigwhite.dtos.AccountNoteSetRequest;
import com.xst.bigwhite.dtos.BindDeviceInfoResponse;
import com.xst.bigwhite.dtos.ConferenceAccountResponse;
import com.xst.bigwhite.dtos.ConferenceDeviceRequest;
import com.xst.bigwhite.dtos.ConfirmAccountRequest;
import com.xst.bigwhite.dtos.DeviceAccountInfo;
import com.xst.bigwhite.dtos.DeviceInfoRequest;
import com.xst.bigwhite.dtos.DeviceInfoResponse;
import com.xst.bigwhite.dtos.DeviceSetNoteInfoRequest;
import com.xst.bigwhite.dtos.IpcDeviceInfoRequest;
import com.xst.bigwhite.dtos.JoinDeviceInfoRequest;
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
import com.xst.bigwhite.models.DeviceBind;
import com.xst.bigwhite.models.DeviceBind.BindStatus;
import com.xst.bigwhite.models.DeviceNote;
import com.xst.bigwhite.service.AccountDeviceService;
import com.xst.bigwhite.service.DeviceBindService;
import com.xst.bigwhite.service.DeviceNoteService;
import com.xst.bigwhite.utils.Helpers;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/device")
public class DeviceController {

	private final DeviceRepository deviceRepository;
	private final AccountRepository accountRepository;
	private final VerifyMessageRepository verifyMessageRepository;
	private final AccountDeviceRepository accountDeviceRepository;
	private final DeviceNoteRepository deviceNoteRepository;
	private final DeviceBindRepository deviceBindRepository;
	
	private final AccountDeviceService accountDeviceService;
	private final DeviceNoteService deviceNoteService;
	private final DeviceBindService deviceBindService;

	@Autowired
	DeviceController(AccountRepository accountRepository, 
			DeviceRepository deviceRepository,
			VerifyMessageRepository verifyMessageRepository, 
			AccountDeviceRepository accountDeviceRepository,
			DeviceNoteRepository deviceNoteRepository,
			DeviceBindRepository deviceBindRepository,
			AccountDeviceService accountDeviceService,
			DeviceNoteService deviceNoteService,
			DeviceBindService deviceBindService) {
		
		this.deviceRepository = deviceRepository;
		this.accountRepository = accountRepository;
		this.verifyMessageRepository = verifyMessageRepository;
		this.accountDeviceRepository = accountDeviceRepository;
		this.deviceNoteRepository = deviceNoteRepository;
		this.deviceBindRepository = deviceBindRepository;
		
		this.accountDeviceService = accountDeviceService;
		this.deviceNoteService = deviceNoteService;
		this.deviceBindService = deviceBindService;
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
	 * 修改账户的联系人备注名
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/setAcountNoteName", method = RequestMethod.POST)
	@ResponseBody
	Boolean setAcountNoteName(@RequestBody DeviceSetNoteInfoRequest input) {
		
		DeviceNote deviceNote =null;
		
		if(!StringUtils.isNotBlank(input.noteName)){
			throw new RestRuntimeException("设备备注名称不能为空!");
		}
		
		Iterable<DeviceNote> deviceNotes = deviceNoteService.findAcountNoteByDeviceAndMobile(input.getDeviceno(),input.mobileno);
		if(deviceNotes!=null && deviceNotes.iterator().hasNext()){
			deviceNote = deviceNotes.iterator().next();
		}else{
			Optional<Account> accounted = accountRepository.findTop1ByMobileno(input.mobileno);
			if(!accounted.isPresent()){
				throw new RestRuntimeException("账户:" + input.mobileno + "不存在!");
			}
			
			Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
			if(!deviced.isPresent()){
				throw new RestRuntimeException("设备:" + input.getDeviceno() + "不存在!");
			}
			
			deviceNote = new DeviceNote(accounted.get(),deviced.get());
		}
		
		if(deviceNote!=null){
			deviceNote.setNoteName(input.noteName);
			deviceNoteRepository.save(deviceNote);
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
		
		Iterable<AccountDevice> accountDeviced = accountDeviceService.getAccountDevice(input.mobileno,input.deviceno);
		if(accountDeviced.iterator().hasNext()){
		  Iterable<AccountDevice> masterAccounted = accountDeviceService.getAccountDeviceMasterByDeviceNo(input.deviceno); 
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
	 * 更新大白管理元
	 * 
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/bindDevice", method = RequestMethod.POST)
	@ResponseBody
	Boolean bindDevice(@RequestBody final JoinDeviceInfoRequest input) {
		if(StringUtils.isBlank(input.getDeviceno()) || StringUtils.isBlank(input.joindeviceno)){
			throw new RestRuntimeException("设备号或者被绑定的设备号为空!");
		}
		
		Iterable<DeviceBind> deviceBinds = deviceBindService.findDeviceBindByDeviceno(input.getDeviceno());
		if(deviceBinds!=null && deviceBinds.iterator().hasNext()){
			for(DeviceBind devicebind : deviceBinds){
				//DeviceBind devicebind = deviceBinds.iterator().next();
				if(devicebind.getBinded().no.equals(input.joindeviceno)){
					if(devicebind.status == BindStatus.Unbind){
						//
					}else if(devicebind.status == BindStatus.Binded){
						throw new RestRuntimeException("设备号" + input.getDeviceno() + "和设备号" + input.joindeviceno + "已经绑定!");
					}else{
						devicebind.status = BindStatus.Unbind;
						devicebind.updateDate = new Date();
						devicebind.setConfirmed(false);
						deviceBindRepository.save(devicebind);
					}
				}
			}
		}else{
			Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
			if(!deviced.isPresent()){
				throw new RestRuntimeException("设备号" + input.getDeviceno() + "不存在!");
			}
			
			Optional<Device> devicebinded = deviceRepository.findTop1Byno(input.joindeviceno);
			if(!devicebinded.isPresent()){
				throw new RestRuntimeException("绑定设备号" + input.joindeviceno + "不存在!");
			}
				
			DeviceBind devicebind = new DeviceBind(deviced.get(),devicebinded.get());
			devicebind.status = BindStatus.Unbind;
			devicebind.setConfirmed(false);
			deviceBindRepository.save(devicebind);
		}
			
		return true;
	}
	
	/**
	 * 确认大白设备是否绑定通过
	 * 
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/confirmBindDevice", method = RequestMethod.POST)
	@ResponseBody
	Boolean confirmBindDevice(@RequestBody final JoinDeviceInfoRequest input) {
		
		Boolean isFind = false;
		Iterable<DeviceBind> deviceBinds = deviceBindService.findDeviceBindByDeviceno(input.getDeviceno());
		if(deviceBinds!=null && deviceBinds.iterator().hasNext()){
			for(DeviceBind devicebind : deviceBinds){
				//DeviceBind devicebind = deviceBinds.iterator().next();
				if(devicebind.getBinded().no.equals(input.joindeviceno)){
					//DeviceBind devicebind = deviceBinds.iterator().next();
					devicebind.setConfirmed(input.confirmed);
					devicebind.setStatus(input.confirmed ? BindStatus.Binded : BindStatus.Injected);
					devicebind.updateDate = new Date();
					
					deviceBindRepository.save(devicebind);
					isFind= true;
					break;
				}
			}
			
			if(!isFind){
				throw new RestRuntimeException("设备号" + input.getDeviceno() + "和设备号" + input.joindeviceno + "绑定申请不存在!");
			}
		}else{
			throw new RestRuntimeException("设备号" + input.getDeviceno() + "和设备号" + input.joindeviceno + "绑定申请不存在!");
		}
		
		return true;
	}
	
	/**
	 * 查询设备关联的账户信息
	 * 
	 * @param ScanDeviceRequest
	 * @return DeviceInfoResponse
	 */
	@RequestMapping(value = "/devices", method = RequestMethod.POST)
	@ResponseBody
	List<BindDeviceInfoResponse> bindedDevices(@RequestBody DeviceInfoRequest input) {
		List<BindDeviceInfoResponse> response = new ArrayList<>();
		
		Iterable<DeviceBind> deviceBinds = deviceBindService.findDeviceBindByDeviceno(input.getDeviceno());
		if(deviceBinds!=null && deviceBinds.iterator().hasNext()){
			for(DeviceBind bind : deviceBinds){
				BindDeviceInfoResponse item = mappingBindDeviceInfoResponse(bind);
				response.add(item);
			}
		}
		
		return response;
	}
	
	private BindDeviceInfoResponse mappingBindDeviceInfoResponse(DeviceBind bind) {
		BindDeviceInfoResponse item = new BindDeviceInfoResponse();
		
		item.setConfirmed(bind.getConfirmed());
		item.setStatus(bind.getStatus());
		item.setDeviceno(bind.getBinded().no);
		item.setDevicename(bind.getBinded().name);
		item.setHeadimage(bind.getBinded().headimage);
		item.setIpc(bind.getIpc());
		
		return item;
	}

	/**
	 * 启用账户的IPC
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/enableIPC", method = RequestMethod.POST)
	@ResponseBody
	Boolean enableIPC(@RequestBody IpcDeviceInfoRequest input) {
	
		Iterable<DeviceBind> deviceBinds = deviceBindService.findDeviceBindByDevice(input.getDeviceno(),input.bind_deviceno);
		if(deviceBinds!=null && deviceBinds.iterator().hasNext()){
			DeviceBind deviceBind = deviceBinds.iterator().next();
			deviceBind.setIpc(input.getYesno());
			
			deviceBindRepository.save(deviceBind);
		}else{
		  throw new RestRuntimeException("设备号:" + input.getDeviceno() + "和设备号:" + input.bind_deviceno +"还没有绑定!"); 	
		}
		
		return false;
	}

	/**
	 * 启用账户的IPC
	 * @param AccountInfoRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/checkIPC", method = RequestMethod.POST)
	@ResponseBody
	Boolean checkIPC(@RequestBody IpcDeviceInfoRequest input) {
		Iterable<DeviceBind> deviceBinds = deviceBindService.findDeviceBindByDevice(input.getDeviceno(),input.bind_deviceno);
		if(deviceBinds!=null && deviceBinds.iterator().hasNext()){
			DeviceBind deviceBind = deviceBinds.iterator().next();
			return deviceBind.ipc;
		}
		
		return false;
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

			Iterable<AccountDevice> accountList = accountDeviceService.getAccountByDeviceno(deviceno);

			List<DeviceAccountInfo> accounts = new ArrayList<>();
			response.setAccounts(accounts);

			accountList.forEach((ac) -> {
				// if(ac.confirmed){
				DeviceAccountInfo accountInfo = DeviceAccountInfo.mapping(ac);
				accounts.add(accountInfo);
				// }
			});
			
			Iterable<DeviceNote> deviceNotes = deviceNoteService.findAcountNoteByDevice(input.getDeviceno());
			if(deviceNotes!=null && deviceNotes.iterator().hasNext()){
				updateDeviceNoteName(deviceNotes,accounts);
			}

		} else {
			throw new RestRuntimeException("设备号:" + input.getDeviceno() + "没有注册或者不存在!");
		}

		return response;
	}

	private void updateDeviceNoteName(Iterable<DeviceNote> deviceNotes, List<DeviceAccountInfo> accounts) {
		deviceNotes.forEach((dn)->{
			String mobileno = dn.getAccount().mobileno;
			accounts.forEach((ac)->{
				if(ac.mobileno.equals(mobileno)){
					ac.setNoteName(dn.getNoteName());
				}
			});
		});
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

		Iterable<AccountDevice> accounts = accountDeviceService.getAccountByDeviceno(input.getDeviceno());
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

		Iterable<ConferenceAccount> conferences = accountDeviceService.getAccountConferenceByDeviceno(input.deviceno);

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
