package com.xst.bigwhite.dtos;

/**
 *  检查手机验证码
 * @author wangjun
 *
 */

public class ChechVerifyCodeRequest extends RegisterMobileRequest{
	
	/**
	 * 手机验证码
	 */
	public String verifycode;

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	
}
