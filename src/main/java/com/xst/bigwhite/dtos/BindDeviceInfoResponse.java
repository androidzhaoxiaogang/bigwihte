package com.xst.bigwhite.dtos;

import com.xst.bigwhite.models.DeviceBind;
import com.xst.bigwhite.models.DeviceBind.BindStatus;

public class BindDeviceInfoResponse {
	
	/**
	 * 大白号
     */
	private String deviceno ;
    
	/**
	 * 设备名称
	 */
	private String devicename;
	
	/**
	 * 设备头像
	 */
	private String headimage;
	
	/**
	 * 确认通过
	 */
	private Boolean confirmed;
	
	/**
	 * 是否启用IPC
	 */
	public Boolean ipc = new Boolean(false);
	
	/**
	 * 绑定状态
	 */
	private BindStatus status = BindStatus.Unbind;

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

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public BindStatus getStatus() {
		return status;
	}

	public void setStatus(BindStatus status) {
		this.status = status;
	}

	public Boolean getIpc() {
		return ipc;
	}

	public void setIpc(Boolean ipc) {
		this.ipc = ipc;
	}

	
}
