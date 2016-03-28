package com.xst.bigwhite.dtos;

/**
 * 设置设备IPC
 * 
 * @author Jun
 *
 */
public class IpcDeviceInfoRequest extends DeviceInfoRequest {
	
	public String bind_deviceno;
	public Boolean yesno = new Boolean(false);

	public String getBind_deviceno() {
		return bind_deviceno;
	}

	public void setBind_deviceno(String bind_deviceno) {
		this.bind_deviceno = bind_deviceno;
	}

	public Boolean getYesno() {
		return yesno;
	}

	public void setYesno(Boolean yesno) {
		this.yesno = yesno;
	}

	
}
