package com.xst.bigwhite.dtos;

import com.xst.bigwhite.models.AccountDevice;

/**
 * 账户和设备相关信息
 * @author wangjun
 *
 */
public class AccountDeviceInfo {
	
	/**
     * 帐户手机
     */
	public String mobileno;
	
	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	/**
     * 大白号
     */
	public String deviceno ;
    
	/**
	 * 设备名称
	 */
	public String devicename;
	
    /**
     * 昵称
     */
	public String nick;
	
	/**
	 * 设备头像
	 */
    public String headimage;
    
	/**
     * 是否是设备管理员
     */
	public Boolean devicemaster = new Boolean(false);
	
	/**
	 * 是否管理员审核通过
	 */
	public Boolean confirmed = new Boolean(false);
	
	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}

	public String getNick() {
		return nick;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Boolean getDevicemaster() {
		return devicemaster;
	}

	public void setDevicemaster(Boolean devicemaster) {
		this.devicemaster = devicemaster;
	}
	

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public static AccountDeviceInfo mapping(AccountDevice acdevice) {

		AccountDeviceInfo deviceInfo = new AccountDeviceInfo();

		deviceInfo.setDevicename(acdevice.getDevice().name);
		deviceInfo.setDeviceno(acdevice.getDevice().no);
		deviceInfo.setNick(acdevice.nick);
		
		deviceInfo.setHeadimage(acdevice.getAccount().headimage);
		deviceInfo.setMobileno(acdevice.getAccount().getMobileno());
		
		deviceInfo.setDevicemaster(acdevice.getDevicemaster());
		deviceInfo.setConfirmed(acdevice.getConfirmed());
		return deviceInfo;
	}
	
}
