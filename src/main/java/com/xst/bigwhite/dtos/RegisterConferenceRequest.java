package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 会议注册请求累
 * @author wangjun
 *
 */
public class RegisterConferenceRequest {
	
	/**
	 * 手机号 
	 * 备注 : 当用户用app客户端注册时 填写手机号注册
	 */
	@NotBlank
	public String mobileno;
	
	/**
	 * 会议回话ID
	 */
    public String sessionId;
    
    /**
	 * 会议名称
	 */
	@NotBlank(message="会议名称不能为空")
	public String sessionname;
	
	
  	public String getMobileno() {
  		return mobileno;
  	}

  	public void setMobileno(String mobileno) {
  		this.mobileno = mobileno;
  	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionname() {
		return sessionname;
	}

	public void setSessionname(String sessionname) {
		this.sessionname = sessionname;
	}
	
	
}
