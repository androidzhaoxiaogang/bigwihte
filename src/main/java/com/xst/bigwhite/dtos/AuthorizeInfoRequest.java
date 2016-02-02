package com.xst.bigwhite.dtos;

/**
 * http://justalkcloud.com RSA 鉴权登录
 * @author Jun
 *
 */
public class AuthorizeInfoRequest {
	
	/**
	 * Cloud 帐号（由客户 APP 的授权请求中传入）
	 * d592490821b4196cffeb5097
	 */
	public String id;
	
	/**
	 *  一次性数字（由客户 APP 的授权请求中传入）
	 */
	public String nonce;
}
