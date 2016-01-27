package com.xst.bigwhite.dtos;

import org.hibernate.property.MapAccessor.MapSetter;

import com.xst.bigwhite.models.Account;
import com.xst.bigwhite.models.Device;

/**
 * 扫描二维码后返回设备信息
 * @author wangjun
 *
 */
public class ScanDeviceResponse {

	/**
	 * 设备号
	 */
	private String deviceno ;
	
	/**
	 * 设备名称
	 */
	private String devicename;
	
	/**
	 * 管理员名称
	 */
	private String masternick;

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
	
	public String getMasternick() {
		return masternick;
	}
	public void setMasternick(String masternick) {
		this.masternick = masternick;
	}
	public static ScanDeviceResponse mapping(Device device) {
		
		ScanDeviceResponse response = new ScanDeviceResponse();
		
		response.setDevicename(device.getName());
		response.setDeviceno(device.getNo());
		Account master = device.getAccount();
		if(master!=null){
			response.setMasternick(master.getUsername());
		}
		
		return response;
	}

	
}
