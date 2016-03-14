package com.xst.bigwhite.dtos;

import org.hibernate.validator.constraints.NotBlank;

public class AccountNoteSetRequest extends AccountInfoRequest {

	/**
	 * 备注名称
	 */
	@NotBlank
	public String notename;
	
	/**
	 * 备注的联系人账号
	 */
	@NotBlank
	public String note_mobileno;
	
	/**
	 * 备注的设备号
	 */
	public String deviceno;

	public String getNotename() {
		return notename;
	}

	public void setNotename(String notename) {
		this.notename = notename;
	}

	public String getNote_mobileno() {
		return note_mobileno;
	}

	public void setNote_mobileno(String note_mobileno) {
		this.note_mobileno = note_mobileno;
	}

	public String getDeviceno() {
		return deviceno;
	}

	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}

	
}
