package com.xst.bigwhite.dtos;

/**
 * 移动设备 手机 平板注册信息类
 * @author wangjun
 *
 */
public class RegisterMobileRequest {
	/**
	 * 手机号 
	 * 备注 : 当用户用app客户端注册时 填写手机号注册
	 */
	public String mobileno;
	

  	public String getMobileno() {
  		return mobileno;
  	}

  	public void setMobileno(String mobileno) {
  		this.mobileno = mobileno;
  	}
  	
}
