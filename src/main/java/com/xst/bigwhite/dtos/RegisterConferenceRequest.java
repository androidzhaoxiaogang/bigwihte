package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 会议注册请求累
 * @author wangjun
 *
 */
public class RegisterConferenceRequest extends ConferenceRequest{
	
    /**
	 * 会议名称
	 */
	@NotBlank(message="会议名称不能为空")
	public String sessionname;
	
	public String getSessionname() {
		return sessionname;
	}

	public void setSessionname(String sessionname) {
		this.sessionname = sessionname;
	}
	
}
