package com.xst.bigwhite.dtos;

/**
 * 用户加入会议请求信息
 * @author wangjun
 *
 */
public class JoinConferenceRequest extends ConferenceRequest{

	 /**
	 * 用户手机号 
	 */
	 public String mobileno;

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

}
