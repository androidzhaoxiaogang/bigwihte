package com.xst.bigwhite.dtos;

/**
 * 用户账户相关信息
 * @author wangjun
 *
 */
public class AccountInfoRequest extends AccountInfo {
	
	
	/**
	 *  密码
	 */
    public String password;

    /**
     * 大白号
     */
    public String deviceno ;
    
   
    /**
     * 设备昵称
     */
    public String devicenick;
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceno() {
		return deviceno;
	}

	
	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public String getDevicenick() {
		return devicenick;
	}

	public void setDevicenick(String devicenick) {
		this.devicenick = devicenick;
	}


}
