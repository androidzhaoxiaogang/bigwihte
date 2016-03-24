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

	/**
     * 大白号
     */
	public String deviceno ;
    
	/**
	 * 设备名称
	 */
	public String devicename;
	
	
	/**
	 * 设备昵称
	 */
	public String devicenick;
	
	
	/**
	 * 用户名
	 */
	public String username;
	
    /**
     * 用户昵称
     */
	public String nick;
	
	
	/**
	 * 用户头像
	 */
    public String headimage;
    
	/**
	 * 设备头像
	 */
    public String deviceheadimage;
    
	/**
     * 是否是设备管理员
     */
	public Boolean devicemaster = new Boolean(false);
	
	/**
	 * 是否管理员审核通过
	 */
	public Boolean confirmed = new Boolean(false);
	
	/**
	 * 是否启用IPC
	 */
	public Boolean ipc = new Boolean(false);
	
	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	
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

	
	public String getDevicenick() {
		return devicenick;
	}

	public void setDevicenick(String devicenick) {
		this.devicenick = devicenick;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	public String getDeviceheadimage() {
		return deviceheadimage;
	}

	public void setDeviceheadimage(String deviceheadimage) {
		this.deviceheadimage = deviceheadimage;
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

	public Boolean getIpc() {
		return ipc;
	}

	public void setIpc(Boolean ipc) {
		this.ipc = ipc;
	}

	public static AccountDeviceInfo mapping(AccountDevice acdevice) {

		AccountDeviceInfo deviceInfo = new AccountDeviceInfo();

		deviceInfo.setDevicename(acdevice.getDevice().name);
		deviceInfo.setDeviceno(acdevice.getDevice().no);
		deviceInfo.setDeviceheadimage(acdevice.getDevice().getHeadimage());
		deviceInfo.setDevicenick(acdevice.getDeviceNick());
		
		deviceInfo.setHeadimage(acdevice.getAccount().headimage);
		deviceInfo.setMobileno(acdevice.getAccount().getMobileno());
		deviceInfo.setNick(acdevice.getNick());
		deviceInfo.setIpc(acdevice.getIpc());

		deviceInfo.setDevicemaster(acdevice.getDevicemaster());
		deviceInfo.setConfirmed(acdevice.getConfirmed());
		return deviceInfo;
	}
	
}
