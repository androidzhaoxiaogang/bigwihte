package com.xst.bigwhite.dtos;

/**
 * 用户账户相关信息
 * @author wangjun
 *
 */
public class AccountInfoRequest {
	
	/**
	 * 手机号
	 */
	public String mobileno;
	
	
	/**
	 * 用户名
	 */
	public String username;
	
	
	/**
	 *  密码
	 */
    public String password;

    /**
     * 大白号
     */
    public String deviceno ;
    
    /**
     * 
     * @return
     */
    public String nick;
    
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceno() {
		return deviceno;
	}

	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

}
