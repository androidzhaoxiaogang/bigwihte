package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

public class ConferenceRequest extends ConferenceInfo {
	/**
	 * 设备号
	 * 备注 : 大白号
	 */
	@NotBlank
	public String deviceno;
	
	
	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}


}
