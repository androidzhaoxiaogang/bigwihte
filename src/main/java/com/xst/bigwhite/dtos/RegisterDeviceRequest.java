package com.xst.bigwhite.dtos;

/**
 * RegisterDeviceRequest 大白注册信息类
 * @author wangjun
 *
 */
public class RegisterDeviceRequest {

	/**
	 * 设备硬件唯一号
	 */
	public String sn;
	
	/**
	 * 设备MAC地址
	 */
	public String mac;
	
	/**
	 * 设备的名称
	 */
	public String devicename;
	
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}


}
