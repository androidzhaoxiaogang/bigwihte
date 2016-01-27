package com.xst.bigwhite.dtos;

/**
 * RegisterDeviceResponse 注册大白设备返回信息类
 * @author wangjun
 *
 */
public class RegisterDeviceResponse {

	/**
	 *  设备的唯一号 由系统自动生成
	 */
	public String deviceno;


	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}
	
}
