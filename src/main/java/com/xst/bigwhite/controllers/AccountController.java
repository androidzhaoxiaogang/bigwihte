package com.xst.bigwhite.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xst.bigwhite.BigwhiteApplication;
import com.xst.bigwhite.daos.*;
import com.xst.bigwhite.dtos.AccountDeviceInfo;
import com.xst.bigwhite.dtos.AccountInfoRequest;
import com.xst.bigwhite.dtos.AccountInfoResponse;
import com.xst.bigwhite.dtos.AccountNoteSetRequest;
import com.xst.bigwhite.dtos.AccoutContractResponse;
import com.xst.bigwhite.dtos.AuthorizeInfoRequest;
import com.xst.bigwhite.dtos.ChechVerifyCodeRequest;
import com.xst.bigwhite.dtos.ConferenceAccountRequest;

import com.xst.bigwhite.dtos.ConferenceAccountResponse;
import com.xst.bigwhite.dtos.ConfirmAccountRequest;
import com.xst.bigwhite.dtos.RegisterMobileRequest;
import com.xst.bigwhite.dtos.RegisterMobileResponse;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.AccountDevice;
import com.xst.bigwhite.models.AccountNote;
import com.xst.bigwhite.models.ConferenceAccount;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.VerifyMessage;
import com.xst.bigwhite.service.AccountDeviceService;
import com.xst.bigwhite.service.AccountNoteService;
import com.xst.bigwhite.utils.Helpers;
import com.xst.bigwhite.utils.SMSManager;
import com.justalk.cloud.Signer;
import com.mysema.query.Tuple;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/account")
public class AccountController {
	private static final Logger log = LoggerFactory.getLogger(BigwhiteApplication.class);
	
	private final DeviceRepository deviceRepository;
	private final AccountRepository accountRepository;
	private final VerifyMessageRepository verifyMessageRepository;
	private final AccountDeviceRepository accountDeviceRepository;
	private final AccountNoteRepository accountNoteRepository;
	private final AccountNoteService accountNoteService;
	private final AccountDeviceService accountDeviceService;

	@Autowired
	AccountController(AccountRepository accountRepository,
			DeviceRepository deviceRepository,
			VerifyMessageRepository verifyMessageRepository,
			AccountDeviceRepository accountDeviceRepository,
			AccountNoteRepository accountNoteRepository,
			AccountNoteService accountNoteService,
			AccountDeviceService accountDeviceService) {
		
		this.deviceRepository = deviceRepository;
		this.accountRepository = accountRepository;
		this.verifyMessageRepository = verifyMessageRepository;
		this.accountDeviceRepository = accountDeviceRepository;
		this.accountNoteRepository = accountNoteRepository;
		
		this.accountNoteService = accountNoteService;
		this.accountDeviceService = accountDeviceService;
	}


	@Autowired
	SMSManager smsManager;
	
	/**
	 * 修改账户的密码
	 * 
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	Boolean updatePassword(@RequestBody AccountInfoRequest input) {
		String password = input.password;
		if(password==null || password.equals("")){
			throw new RestRuntimeException("密码不能为空,请先修改密码!");
		}
		
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(input.mobileno);
		if (accounted.isPresent()) {
			Account account = accounted.get();

			account.setPassword(password);
			accountRepository.save(account);
		} else {
			throw new RestRuntimeException("账户不存在!");
		}

		return true;
	}

	/**
	 * 修改账户的昵称
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
	@ResponseBody
	Boolean updateUserName(@RequestBody AccountInfoRequest input) {
		
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(input.mobileno);
		if (accounted.isPresent()) {
			Account account = accounted.get();
			
			if(input.deviceno!=null && input.nick!=null){
				Iterable<AccountDevice> devices = accountDeviceService.getAccountDevice(input.mobileno,input.deviceno);
				if(devices!=null && devices.iterator().hasNext()){
					AccountDevice device = devices.iterator().next();
					device.setNick(input.nick);
					accountDeviceRepository.save(device);
				}
			}
			else if(input.username!=null && !account.username.equals(input.username))
			{
				account.setUsername(input.username);
				accountRepository.save(account);
			}
			
		} else {
			throw new RestRuntimeException("账户" + input.mobileno + "不存在!");
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
	Boolean setAcountNoteName(@RequestBody AccountNoteSetRequest input) {
	   AccountNote accountNode =null;
	   if(StringUtils.isBlank(input.notename)){
		   throw new RestRuntimeException("备注名信息不能为空！");
	   }
	   
	   Iterable<AccountNote> accountNotes = accountNoteService.findAccountNoteByMobileno(input.mobileno,input.deviceno,input.note_mobileno);
	   if(accountNotes!=null && accountNotes.iterator().hasNext()){
		   for(AccountNote note: accountNotes){
			   if(note.getContact().mobileno.equals(input.note_mobileno)){
				   accountNode = (AccountNote) accountNotes.iterator().next(); 
				   break;
			   }
		   }
	   }else{
		   Optional<Account> accounted= accountRepository.findTop1ByMobileno(input.mobileno);
		   if(!accounted.isPresent()){
			   throw new RestRuntimeException("没有查到账户" + input.mobileno +"的信息！");
		   }
			   
		   Optional<Account> noteAccounted= accountRepository.findTop1ByMobileno(input.note_mobileno);
		   if(!noteAccounted.isPresent()){
			   throw new RestRuntimeException("没有查到备注账户" + input.note_mobileno +"的信息！");
		   }
		   
		   Optional<Device> deviced = deviceRepository.findTop1Byno(input.deviceno);
		   if(!deviced.isPresent()){
			   throw new RestRuntimeException("没有查到备注设备" + input.deviceno +"的信息！");
		   }
		   
		   accountNode = new AccountNote(accounted.get(),noteAccounted.get(),deviced.get());
	   }
	   
	   if(accountNode!=null){
		   accountNode.setNoteName(input.notename);
		   accountNoteRepository.save(accountNode);
	   }
	   
	   return true;
	}

	/**
	 * 修改账户的昵称
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/updateDeviceNick", method = RequestMethod.POST)
	@ResponseBody
	Boolean updateDeviceNick(@RequestBody AccountInfoRequest input) {
	    if(input.getMobileno() != null && input.getDeviceno()!= null && input.getDevicenick() !=null){
	    	Iterable<AccountDevice> devices = accountDeviceService.getAccountDevice(input.mobileno,input.deviceno);
	    	if(devices!=null && devices.iterator().hasNext()){
	    		AccountDevice device = devices.iterator().next();
	    		device.setDeviceNick(input.devicenick);
				accountDeviceRepository.save(device);
	    	}else{
	    		throw new RestRuntimeException("没有查到账户" + input.mobileno + "设备号" + input.deviceno + "已经绑定的信息！");
	    	}
	    }else{
	    	throw new RestRuntimeException("用户名或者设备号和设备昵称不能为空！");
	    }
		
	    return true;
	}
	
	
	@Value("${justalk.prikey}")
	String fileName;
	
	/**
	 * 手机号登入
	 * 
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/authorize", method = RequestMethod.POST)
	@ResponseBody
	String authorize(@RequestBody AuthorizeInfoRequest input) {
		 
		 String id = input.id; 
		 String nonce = input.nonce; 
		 int expires = 3600 * 24 *1;
		 
		 return Signer.signWithFile(fileName,id,nonce,expires);
	}
	
	/**
	 * 手机号登入
	 * 
	 * @param AccountInfoRequest
	 * @return AccountInfoResponse
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	AccountInfoResponse login(@RequestBody AccountInfoRequest input) {

		AccountInfoResponse response = new AccountInfoResponse();
		String password = input.getPassword();
		String mobileno = input.mobileno;

		if(password==null || password.equals("")){
			throw new RestRuntimeException("密码不能为空,请先修改密码!");
		}
		
		Optional<Account> accounted = accountRepository.findTop1ByMobilenoAndPassword(mobileno, password);

		if (accounted.isPresent()) {
			Account account = accounted.get();
			account.setUpdatedate(new Date());
			accountRepository.save(account);
			
			response.setMobileno(mobileno);
			response.setDevices(new ArrayList<AccountDeviceInfo>());
			response.setUsername(account.getUsername());
			response.setHeadimage(account.getHeadimage());
			
			Set<AccountDevice> devices = account.getDevices();
			for (AccountDevice acdevice : devices) {

				AccountDeviceInfo deviceInfo = AccountDeviceInfo.mapping(acdevice);
				
				if (acdevice.getDevice() == null) {
					deviceInfo.setDevicemaster(false);
				} else {
					deviceInfo.setDevicemaster(acdevice.devicemaster);
				}
				
				response.getDevices().add(deviceInfo);
			}

		} else {
			throw new RestRuntimeException("手机号或者密码不正确!");
		}

		return response;
	}
	

	/**
	 * 注册手机号码
	 * 
	 * @param RegisterMobileRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/checkRegistry", method = RequestMethod.POST)
	@ResponseBody
	Boolean checkRegistry(@RequestBody RegisterMobileRequest input) {
		RegisterMobileResponse response = new RegisterMobileResponse();
		Optional<Account> accountd = accountRepository.findTop1ByMobileno(input.mobileno);
		
		return accountd.isPresent();
	}

	/**
	 * 注册手机号码
	 * 
	 * @param RegisterMobileRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/registry", method = RequestMethod.POST)
	@ResponseBody
	RegisterMobileResponse registryMobile(@RequestBody RegisterMobileRequest input) {

		// Set<ConstraintViolation<Account>> failures =
		// validator.validate(input);
		RegisterMobileResponse response = new RegisterMobileResponse();
		Optional<Account> accountd = accountRepository.findTop1ByMobileno(input.mobileno);
		
		if (!accountd.isPresent()) {
			Account accountNew = new Account(input.mobileno, input.mobileno);
			accountNew.setCreatedate(new Date());
			accountNew.setPassword("bigmax!@#");
			accountRepository.save(accountNew);
			
			response.setVerfiycode(getVerfiyCode(input.mobileno));
			
			String msgid = UUID.randomUUID().toString().replaceAll("-", "");
			this.smsManager.sendSMS(input.mobileno, "恭喜您,注册大白账号成功!验证码为:" + response.verfiycode + "【佳视通】", msgid);

		} else {
			throw new RestRuntimeException("手机号:" + input.mobileno + "已经被注册!");
		}

		return response;
	}
	
	 /* 获取手机验证码
	 * 
	 * @param RegisterMobileRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/verfiyCode", method = RequestMethod.POST)
	@ResponseBody
	RegisterMobileResponse veryCode(@RequestBody RegisterMobileRequest input) {
		RegisterMobileResponse response = new RegisterMobileResponse();
		
		String verifycode = getVerfiyCode(input.mobileno);
		response.setVerfiycode(verifycode);
		
		String msgid = UUID.randomUUID().toString().replaceAll("-", "");
		this.smsManager.sendSMS(input.mobileno, "您好!大白账号验证码为:" + response.verfiycode + "【佳视通】", msgid);

		return response;
	}
	 
	private void saveVerifyMessage(String mobileno,String verifycode){
		VerifyMessage verifyMessage = new VerifyMessage(mobileno, verifycode);
		verifyMessage.setCreatedate(new Date());
		verifyMessageRepository.save(verifyMessage);
	}

    private String getVerfiyCode(String mobileno){
    	String verifycode = Helpers.random(6);
    	
    	Optional<VerifyMessage> message = verifyMessageRepository.findTop1ByMobileno(mobileno);
		if (message.isPresent()) {
			VerifyMessage verifyMessage = message.get();
			Calendar rightNow = Calendar.getInstance();
			rightNow.add(Calendar.MINUTE, -5);
			if (rightNow.getTime().after(verifyMessage.getCreatedate())) {
				verifyMessage.setVerifycode(verifycode);
				verifyMessageRepository.save(verifyMessage);
			} else {
				verifycode = verifyMessage.verifycode;
			}
		} else {
			saveVerifyMessage(mobileno, verifycode);
		}

		return verifycode;
    }
	
	/**
	 * 检查验证码是否正确
	 * 
	 * @param ChechVerifyCodeRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/checkVerifyCode", method = RequestMethod.POST)
	@ResponseBody
	Boolean checkVerifyCode(@RequestBody ChechVerifyCodeRequest input) {

		String mobileno = input.mobileno;
		String verifycode = input.verifycode;
		Optional<VerifyMessage> verify = verifyMessageRepository.findTop1ByMobilenoAndVerifycode(mobileno, verifycode);

		VerifyMessage verifyMessage;
		if(verify.isPresent()){
			verifyMessage = verify.get();
			Calendar rightNow = Calendar.getInstance();
			rightNow.add(Calendar.MINUTE, -5);
			if (rightNow.getTime().after(verifyMessage.getCreatedate())) {
				throw new RestRuntimeException("验证号:" + input.verifycode + "已经过期!");
			}
		}else{
			throw new RestRuntimeException("验证号:" + input.verifycode + "不存在!");
		}
		
		return true;
	}

	/**
	 * 更新当前账户的信息
	 * 
	 * @param AccountInfoRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/registerDetails", method = RequestMethod.POST)
	@ResponseBody
	boolean registryMobileRegisterDetails(@RequestBody AccountInfoRequest input) {

		String mobileno = input.mobileno;
		String deviceno = input.deviceno;
		String password = input.password;
		String username = input.username;
		
		if(username==null || username.length()<=0)
			throw new RestRuntimeException("用户名不能为空!");
		
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(mobileno);
		if (accounted.isPresent()) {
			Boolean deviceadmin = false;

			Account account = accounted.get();
			account.setUsername(username);
			
			account.setUpdatedate(new Date());
			if(password!=null && password.length()>0)
				account.setPassword(password);
			
			Account accountUpdated = accountRepository.save(account);

			Optional<Device> deviced = deviceRepository.findTop1Byno(input.getDeviceno());
			if (deviced.isPresent()) {
				AccountDevice accountDevice = null;
				Iterable<AccountDevice> accountDevices = accountDeviceService.getAccountDevice(mobileno,deviceno);
				if(accountDevices.iterator().hasNext()){
					accountDevice = accountDevices.iterator().next();
				}
				
				if(accountDevice == null){
					accountDevice = new AccountDevice();
					accountDevice.setAccount(account);
					accountDevice.setCreatedate(new Date());
					accountDevice.setNick(input.getUsername());
					
	
					Device device = deviced.get();
					if (device.getAccount() == null) {
	
						deviceadmin = true;
						device.setAccount(accountUpdated);
						accountDevice.setConfirmed(true);
						accountDevice.setDeviceNick(device.name);
						
						deviceRepository.save(device);
					}
	
					accountDevice.setDevice(device);
					accountDevice.setDevicemaster(deviceadmin);
					accountDeviceRepository.save(accountDevice);
				}
			}else{
				throw new RestRuntimeException("设备号:" + deviceno + "不存在!");
			}

			return true;
		} else {
			throw new RestRuntimeException("用户:" + mobileno + "不存在!");
		}

	}
	

	
	/**
	 * 更新当前账户的信息 取消用户和设备的绑定关系
	 * 
	 * @param AccountInfoRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/bind", method = RequestMethod.POST)
	@ResponseBody
	Boolean bindDevice(@RequestBody final AccountInfoRequest input) {
		String mobileno = input.mobileno;
		String deviceno = input.deviceno;
		
		Optional<Device> deviced = deviceRepository.findTop1Byno(deviceno);
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(mobileno);
		
		if(!deviced.isPresent()){
			   throw new RestRuntimeException("设备号:" + deviceno + "不存在!");
		}
		Device device = deviced.get();
		
		if (accounted.isPresent()) {
			Account account = accounted.get();
			Set<AccountDevice> devices = account.getDevices();
			if (devices != null && !devices.isEmpty()) {
				Optional<AccountDevice> accountDeviced = devices.stream().filter((ac) -> {
					return ac.getDevice().no.equals(deviceno);
				}).findAny();
				if (!accountDeviced.isPresent()) {
					saveAccountDevice(account,device);
				}
			}else{
				saveAccountDevice(account,device);
				return true;
			}
		}else{
			throw new RestRuntimeException("用户:" + mobileno + "不存在!");
		}
		
		return false;
	}
	
	
	private void saveAccountDevice(Account account,Device device){
		   AccountDevice accountDevice = new AccountDevice();
		   accountDevice.createdate = new Date();
		   accountDevice.setDevice(device);
		   accountDevice.setAccount(account);
		   accountDevice.setNick(account.getUsername());
		   accountDevice.setDeviceNick(device.name);
		   accountDevice.setConfirmed(false);
		   if(device.getDevices() ==null || device.getDevices().isEmpty()){
			   accountDevice.setDevicemaster(true);
		   }
		   
		   accountDeviceRepository.save(accountDevice);
	}
	
	/**
	 * 更新当前账户的信息 取消用户和设备的绑定关系
	 * 
	 * @param AccountInfoRequest
	 * @return RegisterMobileResponse
	 */
	@RequestMapping(value = "/unbind", method = RequestMethod.POST)
	@ResponseBody
	Boolean unBindDevice(@RequestBody final AccountInfoRequest input) {
		String mobileno = input.mobileno;
		String deviceno = input.deviceno;
		
		Optional<Account> accounted = accountRepository.findTop1ByMobileno(mobileno);
		if (accounted.isPresent()) {
			Account account = accounted.get();
			Set<AccountDevice> devices = account.getDevices();
			if (devices != null && !devices.isEmpty()) {
				Optional<AccountDevice> deviced = devices.stream().filter((ac) -> {
					return ac.getDevice().no.equals(deviceno);
				}).findAny();
				if (deviced.isPresent()) {
					AccountDevice accountdevice = deviced.get();
					accountDeviceRepository.delete(accountdevice);
					devices.remove(accountdevice);
					
					if (accountdevice.devicemaster) { // 删除设备时检查是否为当前设备的管理员
						
						Comparator<? super AccountDevice> comparator = new AccountDeviceComparator();
						Optional<AccountDevice> deviceSorted = devices.stream().filter((ac) -> {
																	return ac.getDevice().no.equals(deviceno);
																}).sorted(comparator).findFirst();
						
						if (deviceSorted.isPresent()) {

							AccountDevice accountMaster = deviced.get();
							accountMaster.setDevicemaster(true);
							accountDeviceRepository.save(accountMaster);

							Device masterDevice = accountMaster.getDevice();
							masterDevice.setAccount(accountMaster.getAccount());
							deviceRepository.save(masterDevice);
						}else{
						  Device device = accountdevice.getDevice();
						  device.setAccount(null);
						  deviceRepository.save(device);
						}
						
						return true;
					}

				}
			}
		} else {
			throw new RestRuntimeException("用户:" + mobileno + "不存在!");
		}

		return false;
	}

	/**
	 * 管理员确认是否通过帐号申请的绑定到指定的设备
	 * 
	 * @param ConfirmAccountRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/updateConfirm", method = RequestMethod.POST)
	@ResponseBody
	Boolean updateConfirm(@RequestBody ConfirmAccountRequest input) {
		String mobileno = input.mobileno;
	    String deviceno = input.deviceno;
	    
		AccountDevice accountDevice = null;
		Iterable<AccountDevice> accountDevices = accountDeviceService.getAccountDevice(mobileno,deviceno);
		if(accountDevices.iterator().hasNext()){
			accountDevice = accountDevices.iterator().next();
			accountDevice.confirmed = true;
			accountDevice.setConfirmeddate(new Date());
			accountDeviceRepository.save(accountDevice);
			
			return true;
		}else{
			throw new RestRuntimeException("用户:" + mobileno + "没有注册到设备:" + deviceno);
		}
		
	}

	/**
	 * 查询是否审核通过设备(申请的绑定到指定的设备列表 )
	 * 
	 * @param ConfirmAccountRequest
	 * @return ArrayList<AccountDeviceInfo>
	 */
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	@ResponseBody
	ArrayList<AccountDeviceInfo> confirmByAccount(@RequestBody RegisterMobileRequest input) {

		ArrayList<AccountDeviceInfo> accountDeviceInfoes = new ArrayList<AccountDeviceInfo>();

		Iterable<AccountDevice> deviceInfoes = accountDeviceService.getAccountDeviceByAccountMobile(input.mobileno);
		if (deviceInfoes != null && deviceInfoes.iterator().hasNext()) {
			for (AccountDevice accountDevice : deviceInfoes) {
				AccountDeviceInfo item = AccountDeviceInfo.mapping(accountDevice);
				accountDeviceInfoes.add(item);
			}
		}

		return accountDeviceInfoes;
	}
	
	

	/**
	 * 查询是否审核通过设备(申请的绑定到指定的设备列表 )
	 * 
	 * @param ConfirmAccountRequest
	 * @return ArrayList<AccountDeviceInfo>
	 */
	@RequestMapping(value = "/confirms", method = RequestMethod.POST)
	@ResponseBody
	ArrayList<AccountDeviceInfo> confirmAccounts(@RequestBody ConfirmAccountRequest input) {

		ArrayList<AccountDeviceInfo> accountDeviceInfoes = new ArrayList<AccountDeviceInfo>();

		Iterable<AccountDevice> deviceInfoes = accountDeviceService.getAccountDeviceByAccountMobile(input.mobileno);
		if (deviceInfoes != null && deviceInfoes.iterator().hasNext()) {
			for (AccountDevice accountDevice : deviceInfoes) {
				if (accountDevice.getAccount() != null && accountDevice.getDevice() != null) {

					Iterable<AccountDevice> devices = accountDeviceService.getAccountDeviceByDeviceno(accountDevice.getDevice().no);
					if (devices != null && devices.iterator().hasNext()) {
						for (AccountDevice accountDeviceInfo : devices) {

							AccountDeviceInfo item = AccountDeviceInfo.mapping(accountDeviceInfo);
							accountDeviceInfoes.add(item);

						}
					}
				}
			}
		}

		return accountDeviceInfoes;
	}
	
	 /* 查询当前账户下所有的会议信息
	 * 
	 * @param ConferenceAccountResponse
	 * @return ArrayList<ConferenceAccountResponse>
	 */
	@RequestMapping(value = "/contacts", method = RequestMethod.POST)
	@ResponseBody
	List<AccoutContractResponse> contactAccounts(@RequestBody AccountInfoRequest input) {
		List<AccoutContractResponse>  response = new ArrayList<AccoutContractResponse>();
		
		Iterable<AccountNote> accountNotes = accountNoteService.getAccountNotesByAccountMobileAndDevice(input.mobileno,input.deviceno);
		if (accountNotes!=null && accountNotes.iterator().hasNext()) {
			for(AccountNote accountNote :  accountNotes){
				AccoutContractResponse accountContractResponse = new AccoutContractResponse();
				
				accountContractResponse.setHeadimage(accountNote.getAccount().headimage);
				accountContractResponse.setMobileno(accountNote.getAccount().mobileno);
				accountContractResponse.setUsername(accountNote.getAccount().username);
				accountContractResponse.setNoteName(accountNote.getAccount().username);
				
				if(StringUtils.isNotBlank(accountNote.noteName)){
					accountContractResponse.setNoteName(accountNote.noteName);
				}
				
				response.add(accountContractResponse);
			}
		}
		
		return response;
	 }
	
	/*private void validateUser(String username, String mobileno) {
		this.accountRepository.findByUsernameOrMobileno(username, mobileno)
				.orElseThrow(() -> new RestRuntimeException("用户:" + username + "/" + mobileno + "不存在!"));
	}*/
	
	/**
	 * 查询当前账户下所有的会议信息
	 * 
	 * @param ConferenceAccountResponse
	 * @return ArrayList<ConferenceAccountResponse>
	 */
	@RequestMapping(value = "/conferences", method = RequestMethod.POST)
	@ResponseBody
	List<ConferenceAccountResponse> conferenceAccounts(@RequestBody ConferenceAccountRequest input) {
		List<ConferenceAccountResponse>  response = new ArrayList<ConferenceAccountResponse>();
		
		Iterable<ConferenceAccount> conferences = accountDeviceService.getAccountConferenceByAccountMobile(input.mobileno);
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
	
}

/*
 * @ControllerAdvice class AccountControllerAdvice {
 * 
 * @ResponseBody
 * 
 * @ExceptionHandler(UserNotFoundException.class)
 * 
 * @ResponseStatus(HttpStatus.NOT_FOUND) VndErrors
 * userNotFoundExceptionHandler(UserNotFoundException ex) { return new
 * VndErrors("error", ex.getMessage()); } }
 */

class AccountDeviceComparator implements Comparator<AccountDevice> {

	@Override
	public int compare(AccountDevice o1, AccountDevice o2) {

		return o1.createdate.after(o2.createdate) ? 1 : 0;
	}

}
