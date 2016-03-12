package com.xst.bigwhite.dtos;

public class UpdateDeviceInfoRequest extends DeviceInfoRequest {
	
	/**
	 * 设备名称
	 */
   public String devicename;

	public String getDevicename() {
		return devicename;
	}

	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}
   
  
}
