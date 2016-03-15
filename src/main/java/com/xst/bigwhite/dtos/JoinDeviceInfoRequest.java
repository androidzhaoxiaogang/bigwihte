package com.xst.bigwhite.dtos;

public class JoinDeviceInfoRequest extends DeviceInfoRequest {
    /**
    * 绑定设备名称
    */
	public String joindeviceno;
	
	/**
	 * 确认通过
	 */
	public Boolean confirmed;

	public String getJoindeviceno() {
		return joindeviceno;
	}

	public void setJoindeviceno(String joindeviceno) {
		this.joindeviceno = joindeviceno;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}
	
}
