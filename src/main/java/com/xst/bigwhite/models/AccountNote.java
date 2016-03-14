package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *  AccountLogin 用户备注信息类
 * @author wangjun
 *
 */
@Entity
public class AccountNote  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8635320741500649511L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	

	/**
	 * 用户帐号
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	

	/**
	 * 帐号的联系人
	 */
	@JsonIgnore
	@ManyToOne
	private Account contact;
	
	/**
	 * 帐号的设备
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
	/**
	 * 联系人名称
	 */
	public String noteName;
	
	/**
	 * 修改日期
	 */
	public Date updateDate = new Date();

	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public Account getContact() {
		return contact;
	}


	public void setContact(Account contact) {
		this.contact = contact;
	}


	public Device getDevice() {
		return device;
	}


	public void setDevice(Device device) {
		this.device = device;
	}


	public String getNoteName() {
		return noteName;
	}


	public void setNoteName(String noteName) {
		this.noteName = noteName;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public AccountNote(Account account, Account contact,Device device) {
		this.account = account;
		this.contact = contact;
		this.device = device;
	}

	
	AccountNote() { // jpa only
	}
}
