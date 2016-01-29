package com.xst.bigwhite.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.util.ArrayBuilders.BooleanBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Account 用户帐号类
 * @author wangjun
 *
 */
@Entity
public class Account implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -910147047472784813L;

	/**
	 * 用户绑定或使用的设备 
	 * 大白 手机 平板等
	 */
	@OneToMany(mappedBy = "account")
    private Set<AccountDevice> devices = new HashSet<>();

	/**
	 * 用户参与的会议 
	 *
	 */
	@OneToMany(mappedBy = "account")
    private Set<ConferenceAccount> conferences = new HashSet<>();
	
	/**
	 * 用户的登入历史信息
	 */
	@OneToMany(mappedBy = "account")
	@JsonIgnore
    private Set<AccountLogin> logins = new HashSet<>();
	
	
	/**
	 * 创建的会议
	 */
	@OneToMany(mappedBy = "createBy")
	@JsonIgnore
    private Set<Conference> ownerConferences = new HashSet<>();
	
	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 用户密码
	 */
	@JsonIgnore
	public String password;
	
	/**
	 * 用户名 姓名
	 */
	@NotBlank(message="用户名不能为空")  
	@Size(min=2, max=30)
	public String username;

	/**
	 * 手机号
	 */
	@NotBlank(message="手机号不能为空")  
	@Size(min=11, max=20)
	@Column(name = "mobileno", unique = true, nullable = false, length = 32)
	public String mobileno;

	/**
	 * 头像
	 */
	@Size(min=2, max= 300)
	public String headimage;
	
	/**
	 * 创建时间
	 */
	public Date createdate = new Date();
	
	/**
	 * 更新时间
	 */
	public Date updatedate= new Date();
	
	/**
	 * 最后更新时间
	 */
	public Date lastlogindate = new Date();
	
	
	public Set<Conference> getOwnerConferences() {
		return ownerConferences;
	}

	public void setOwnerConferences(Set<Conference> ownerConferences) {
		this.ownerConferences = ownerConferences;
	}
	

	public Set<ConferenceAccount> getConferences() {
		return conferences;
	}

	public void setConferences(Set<ConferenceAccount> conferences) {
		this.conferences = conferences;
	}

	public Set<AccountDevice> getDevices() {
		return devices;
	}

	public void setDevices(Set<AccountDevice> devices) {
		this.devices = devices;
	}

	public Set<AccountLogin> getLogins() {
		return logins;
	}

	public void setLogins(Set<AccountLogin> logins) {
		this.logins = logins;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public Date getLastlogindate() {
		return lastlogindate;
	}

	public void setLastlogindate(Date lastlogindate) {
		this.lastlogindate = lastlogindate;
	}


	public Account(String username, String mobileno, String password) {
		this(username,mobileno);
		this.password = password;
	}

	public Account(String username, String mobileno) {
		this.username = username;
		this.mobileno = mobileno;
	}
	
	Account() { // jpa only
	}
}
