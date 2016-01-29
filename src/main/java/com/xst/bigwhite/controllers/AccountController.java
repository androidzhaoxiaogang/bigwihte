package com.xst.bigwhite.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.xst.bigwhite.BigwhiteApplication;
import com.xst.bigwhite.daos.*;
import com.xst.bigwhite.dtos.AccountDeviceInfo;
import com.xst.bigwhite.dtos.AccountInfoRequest;
import com.xst.bigwhite.dtos.AccountInfoResponse;
import com.xst.bigwhite.dtos.ChechVerifyCodeRequest;
import com.xst.bigwhite.dtos.ConfirmAccountRequest;
import com.xst.bigwhite.dtos.RegisterMobileRequest;
import com.xst.bigwhite.dtos.RegisterMobileResponse;
import com.xst.bigwhite.exception.RestRuntimeException;
import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.AccountDevice;
import com.xst.bigwhite.models.Device;
import com.xst.bigwhite.models.QAccount;
import com.xst.bigwhite.models.QAccountDevice;
import com.xst.bigwhite.models.QDevice;
import com.xst.bigwhite.models.VerifyMessage;
import com.xst.bigwhite.utils.Helpers;
import com.xst.bigwhite.utils.SMSManager;

@Controller
@EnableAutoConfiguration
@RequestMapping("/api/account")
public class AccountController {

	private final DeviceRepository deviceRepository;
	private final AccountRepository accountRepository;
	private final VerifyMessageRepository verifyMessageRepository;
	private final AccountDeviceRepository accountDeviceRepository;
	private static final Logger log = LoggerFactory.getLogger(BigwhiteApplication.class);

	@Autowired
	AccountController(AccountRepository accountRepository,
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
			if(!account.username.equals(input.username))
			{
				account.setUsername(input.username);
				accountRepository.save(account);
			}else{
				if(input.deviceno!=null && input.nick!=null){
					Iterable<AccountDevice> devices = getAccountDevice(input.mobileno,input.deviceno);
					
					ArrayList<AccountDeviceInfo> AccountDeviceInfoes = new ArrayList<AccountDeviceInfo>();
					
					devices.forEach((d)->{
						
						AccountDeviceInfo accountDeviceInfo = AccountDeviceInfo.mapping(d);
						
						if (d.getDevice() == null) {
							accountDeviceInfo.setDevicemaster(false);
						} else {
							accountDeviceInfo.setDevicemaster(d.getDevice().no == input.getDeviceno() && d.devicemaster);
						}
						
						AccountDeviceInfoes.add(accountDeviceInfo);
						
					});
				}
			}
			
		} else {
			throw new RestRuntimeException("账户" + input.mobileno + "不存在!");
		}

		return true;
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
		
		Optional<Account> accounted = accountRepository.findTop1ByMobilenoOrPassword(mobileno, password);

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
	@RequestMapping(value = "/registry", method = RequestMethod.POST)
	@ResponseBody
	RegisterMobileResponse registryMobile(@RequestBody RegisterMobileRequest input) {

		// Set<ConstraintViolation<Account>> failures =
		// validator.validate(input);
		RegisterMobileResponse response = new RegisterMobileResponse();
		Optional<Account> accountd = accountRepository.findTop1ByMobileno(input.mobileno);
		String verifycode = Helpers.random(6);
		if (!accountd.isPresent()) {
			Account accountNew = new Account(input.mobileno, input.mobileno);
			accountNew.setCreatedate(new Date());
			accountRepository.save(accountNew);

			Optional<VerifyMessage> message = verifyMessageRepository.findTop1ByMobileno(input.mobileno);
			if (message.isPresent()) {
				VerifyMessage verifyMessage = message.get();
				Calendar rightNow = Calendar.getInstance();
				rightNow.add(Calendar.MINUTE, -5);
				if (rightNow.getTime().after(verifyMessage.getCreatedate())) {
					saveVerifyMessage(input.mobileno, verifycode);
					response.verfiycode = verifycode;
				} else {
					response.verfiycode = verifyMessage.verifycode;
				}
			} else {
				saveVerifyMessage(input.mobileno, verifycode);
				response.verfiycode = verifycode;
			}

			String msgid = UUID.randomUUID().toString().replaceAll("-", "");
			this.smsManager.sendSMS(input.mobileno, "恭喜您,注册大白账号成功!验证码为:" + response.verfiycode + "【佳视通】", msgid);
		} else {
			throw new RestRuntimeException("手机号:" + input.mobileno + "已经被注册!");
		}

		return response;
	}
	
	private void saveVerifyMessage(String mobileno,String verifycode){
		VerifyMessage verifyMessage = new VerifyMessage(mobileno, verifycode);
		verifyMessage.setCreatedate(new Date());
		verifyMessageRepository.save(verifyMessage);
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
				Iterable<AccountDevice> accountDevices = getAccountDevice(mobileno,deviceno);
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
	@RequestMapping(value = "/unbind", method = RequestMethod.POST)
	@ResponseBody
	Boolean registryMobileUnBind(@RequestBody final AccountInfoRequest input) {
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
		Iterable<AccountDevice> accountDevices = getAccountDevice(mobileno,deviceno);
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
	 * @param ConfirmAccountRequest
	 * @return Boolean
	 */
	@RequestMapping(value = "/confirms", method = RequestMethod.POST)
	@ResponseBody
	ArrayList<AccountDeviceInfo> confirmAccounts(@RequestBody ConfirmAccountRequest input) {
		
		ArrayList<AccountDeviceInfo> accountDeviceInfoes = new ArrayList<AccountDeviceInfo>();

		Iterable<AccountDevice> deviceInfoes = getAccountDeviceByMaster(input.mobileno);
		if (deviceInfoes != null && deviceInfoes.iterator().hasNext()) {
			for (AccountDevice accountDevice : deviceInfoes) {
				if (accountDevice.getAccount() != null && accountDevice.getDevice() != null) {

					Iterable<AccountDevice> devices = getAccountDeviceByDeviceno(accountDevice.getDevice().no);
					if (devices != null && devices.iterator().hasNext()) {
						for (AccountDevice accountDeviceInfo : devices) {
							if (accountDeviceInfo.confirmed.equals(input.yesno)) {
								AccountDeviceInfo item = AccountDeviceInfo.mapping(accountDeviceInfo);
								accountDeviceInfoes.add(item);
							}
						}
					}
				}
			}
		}
		
		return accountDeviceInfoes;
	}
	
	private Iterable<AccountDevice> getAccountDeviceByDeviceno(String no) {
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

	private Iterable<AccountDevice> getAccountDeviceByMaster(String mobileno) {
		QAccountDevice qAccountDevice = QAccountDevice.accountDevice;
		QAccount qAccount = QAccount.account;
		QDevice qDevice = QDevice.device;
		
		BooleanExpression accountMobile = qAccount.mobileno.eq(mobileno);

		JPAQuery query = new JPAQuery(entityManager);
		Iterable<AccountDevice> accountdevices = query.from(qAccountDevice)
			 .leftJoin(qAccountDevice.account,qAccount).fetch()
			 .leftJoin(qAccountDevice.device,qDevice).fetch()
			 .where(accountMobile.and(qAccountDevice.devicemaster.eq(true)))
			 .list(qAccountDevice);
	
		return accountdevices;
	}

	
	private Iterable<AccountDevice> getAccountDevice(String mobileno , String deviceno) {
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

	/*private void validateUser(String username, String mobileno) {
		this.accountRepository.findByUsernameOrMobileno(username, mobileno)
				.orElseThrow(() -> new RestRuntimeException("用户:" + username + "/" + mobileno + "不存在!"));
	}*/
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
