package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 设备和账户关联表
 * @author wangjun
 *
 */
@Entity
public class AccountDevice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2040713280884619042L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 帐号信息
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	/**
	 * 设备信息
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
	/**
	 * 用户当前设备的昵称
	 * 备注名称
	 */
	public String nick;
	
	/**
	 * 账户加入当前设备的验证码
	 */
	public String verifycode;
	
	/**
	 * 设备管理员
	 */
	public Boolean devicemaster = new Boolean(false);
	
	
	/**
	 * 是否验证通过
	 */
	public Boolean confirmed = new Boolean(false);

	/**
	 * 加入日期
	 */
	public Date createdate = new Date();
	
	/**
	 * 通过日期
	 */
	public Date confirmeddate = new Date();

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

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Boolean getDevicemaster() {
		return devicemaster;
	}

	public void setDevicemaster(Boolean devicemaster) {
		this.devicemaster = devicemaster;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getConfirmeddate() {
		return confirmeddate;
	}

	public void setConfirmeddate(Date confirmeddate) {
		this.confirmeddate = confirmeddate;
	}

	
}
