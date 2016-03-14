package com.xst.bigwhite.dtos;

public class DeviceSetNoteInfoRequest extends DeviceInfoRequest {
	
	/**
	 *账户备注信息 
	 */
   public String noteName;
   
   /**
    * 账户信息
    */
   public String mobileno;

	public String getNoteName() {
		return noteName;
	}

	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
   
	
}
