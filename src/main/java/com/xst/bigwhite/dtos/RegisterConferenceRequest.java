package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 会议注册请求累
 * @author wangjun
 *
 */
public class RegisterConferenceRequest {
	
	/**
	 * 设备号
	 * 备注 : 大白号
	 */
	@NotBlank
	public String deviceno;
	
	/**
	 * 会议回话ID
	 */
    public String sessionId;
    
    /**
     * 会议UI
     */
    public String ui;
    
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

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	public String getUi() {
		return ui;
	}

	public void setUi(String ui) {
		this.ui = ui;
	}

	public String getSessionname() {
		return sessionname;
	}

	public void setSessionname(String sessionname) {
		this.sessionname = sessionname;
	}
	
	
}
