package com.xst.bigwhite.dtos;

/**
 * 账户备注信息
 * @author wangjun
 *
 */
public class AccoutContractResponse{

	/**
	 * 手机号
	 */
	public String mobileno;
	
	/**
	 * 用户名
	 */
	public String username;
	

	/**
	 * 用户头像
	 */
    public String headimage;
    
    /**
     *备注名称 
     */
	public String noteName;

	/**
	 * 是否启用了IPC
	 */
    public Boolean ipc = new Boolean(false);
	
	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public Boolean getIpc() {
		return ipc;
	}

	public void setIpc(Boolean ipc) {
		this.ipc = ipc;
	}

	
}
