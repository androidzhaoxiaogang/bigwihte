package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *  AccountLogin 用户登入信息类
 * @author wangjun
 *
 */
@Entity
public class AccountLogin implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8735320741500649500L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 登入时使用的设置
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
	/**
	 * 登入的帐号
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	/**
	 * 登入时间
	 */
	public Date logindate;
	
	/**
	 * 登入的IP
	 */
	@Size(min=2, max= 100)
	public String loginip;
	
	/**
	 * GPS纬度
	 */
	@Size(min=2, max= 30)
	public String lat;
	
	/**
	 * GPS 经度
	 */
	@Size(min=2, max= 30)
	public String lng;
	
	/**
	 * 移动设备网络代码（Mobile Network Code，MNC）
	 * 中国移动 = 00 / 02 / 07, 中国联通 = 01 / 06, 中国电信 = 03 / 05。
	 */
	@Size(min=2, max= 20)
	public String mnc;
	
	/**
	 * spg 基站ID
	 */
	@Size(min=2, max= 20)
	public String rid;
	
	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Device getDevice() {
		return device;
	}


	public void setDevice(Device device) {
		this.device = device;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public Date getLogindate() {
		return logindate;
	}


	public void setLogindate(Date logindate) {
		this.logindate = logindate;
	}


	public String getLoginip() {
		return loginip;
	}


	public void setLoginip(String loginip) {
		this.loginip = loginip;
	}


	public String getLat() {
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public String getLng() {
		return lng;
	}


	public void setLng(String lng) {
		this.lng = lng;
	}


	public String getMnc() {
		return mnc;
	}


	public void setMnc(String mnc) {
		this.mnc = mnc;
	}


	public String getRid() {
		return rid;
	}


	public void setRid(String rid) {
		this.rid = rid;
	}


	public AccountLogin(Account account, Device device, Date logindate) {
		this.account = account;
		this.device = device;
		this.logindate = logindate;
	}

	
	AccountLogin() { // jpa only
	}
	
}
