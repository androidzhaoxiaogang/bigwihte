package com.xst.bigwhite.dtos;

import com.xst.bigwhite.models.AccountDevice;

/*
 * 设备关联的账户信息类
 */
public class DeviceAccountInfo {

	/**
	 * 手机号
	 */
	public String mobileno;
	
	/**
	 * 用户名或者昵称
	 */
	public String username;
	
	
	 /**
     * 昵称
     */
	public String nick;
    
	
	/**
	 * 账户头像
	 */
	public String headImage;
	
	  /**
     * 是否是设备管理员
     */
	public Boolean devicemaster = new Boolean(false);

	public String getMobileno() {
		return mobileno;
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


	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public Boolean getDevicemaster() {
		return devicemaster;
	}

	public void setDevicemaster(Boolean devicemaster) {
		this.devicemaster = devicemaster;
	}

	public static DeviceAccountInfo mapping(AccountDevice ac) {
		
		DeviceAccountInfo accountInfo = new DeviceAccountInfo();
		
		accountInfo.setDevicemaster(ac.getDevicemaster());
    	accountInfo.setMobileno(ac.getAccount().mobileno);
    	accountInfo.setNick(ac.nick);
    	accountInfo.setUsername(ac.getAccount().getUsername());
    	accountInfo.setHeadImage(ac.getAccount().headimage);
    	
    	return accountInfo;
    	
	}
	
}
