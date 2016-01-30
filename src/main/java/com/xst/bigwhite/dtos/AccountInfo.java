package com.xst.bigwhite.dtos;

public class AccountInfo {
	/**
	 * 手机号
	 */
	public String mobileno;
	
	
	/**
	 * 用户名
	 */
	public String username;
	
	 /**
     * 
     * @return
     */
    public String nick;
    
    

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
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
