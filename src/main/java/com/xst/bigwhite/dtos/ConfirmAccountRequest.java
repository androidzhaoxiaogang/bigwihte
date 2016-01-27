package com.xst.bigwhite.dtos;

/**
 * 管理员审核是否通过用户请求
 * @author wangjun
 *
 */
public class ConfirmAccountRequest extends RegisterMobileRequest {
	
	/**
	 * 是否通过用户请求
	 */
	public boolean yesno = new Boolean(false);

	/**
	 * 
	 */
	public String deviceno;
	
	public boolean isYesno() {
		return yesno;
	}

	public void setYesno(boolean yesno) {
		this.yesno = yesno;
	}

	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}
	

}
