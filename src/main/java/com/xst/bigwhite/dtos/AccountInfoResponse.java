package com.xst.bigwhite.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前账户信息
 * @author wangjun
 *
 */
public class AccountInfoResponse {

	/**
	 * 手机号
	 */
	public String mobileno;
	
	/**
	 * 用户名
	 */
	public String username;
	
	/**
	 * 昵称
	 */
	public String nick;
	
	/**
	 * 用户头像
	 */
    public String headimage;
    
    
	/**
	 * 当前账户下面所有设备信息
	 */
	private List<AccountDeviceInfo> devices= new ArrayList<>();
	
	
	public String getMobileno() {
		return mobileno;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public List<AccountDeviceInfo> getDevices() {
		return devices;
	}

	public void setDevices(List<AccountDeviceInfo> devices) {
		this.devices = devices;
	}

}
