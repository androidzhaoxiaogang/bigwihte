package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 会议注册请求累
 * @author wangjun
 *
 */
public class RegisterConferenceRequest extends ConferenceInfo{
	
	/**
	 * 设备号
	 * 备注 : 大白号
	 */
	@NotBlank
	public String deviceno;
	
    /**
	 * 会议名称
	 */
	@NotBlank(message="会议名称不能为空")
	public String sessionname;
	
	
	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public String getSessionname() {
		return sessionname;
	}

	public void setSessionname(String sessionname) {
		this.sessionname = sessionname;
	}
	
}
