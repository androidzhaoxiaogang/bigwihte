package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Conference 会议信息
 * @author wangjun
 *
 */
@Entity
public class Conference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1160609362317170009L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	public Long id;
	
	/**
	 * 会议编号
	 */
	@NotBlank(message="会议编号不能为空")
	public String sessionId;
	
	@NotBlank(message="会议UI不能为空")
	public String ui;
	
	/**
	 * 会议名称
	 */
	@NotBlank(message="会议名称不能为空")
	public String sessionname;
	
	/**
	 * 创建日期
	 */
	public Date createDate = new Date();
	
	/**
	 * 最后激活时间
	 */
	@Temporal(TemporalType.TIMESTAMP)  
    @Column(length = 20)  
	public Date activeTime = new Date();
	
	/**
	 * 结束时间
	 */
	public Date endDate = new Date();
	
	/**
	 * 参与 会议 的用户
	 *
	 */
	@OneToMany(mappedBy = "conference")
    private Set<ConferenceAccount> accounts = new HashSet<>();

	/**
	 * 会议总时长 
	 * 单位:分钟
	 */
	public Integer totalMinutes = new Integer(0);
	
	/**
	 * 设备信息
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
	
	public Integer getTotalMinutes() {
		return totalMinutes;
	}


	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getUi() {
		return ui;
	}


	public void setUi(String ui) {
		this.ui = ui;
	}


	public String getSessionname() {
		return sessionname;
	}


	public void setSessionname(String sessionname) {
		this.sessionname = sessionname;
	}


	public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getActiveTime() {
		return activeTime;
	}


	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Set<ConferenceAccount> getAccounts() {
		return accounts;
	}


	public void setAccounts(Set<ConferenceAccount> accounts) {
		this.accounts = accounts;
	}


	public Device getDevice() {
		return device;
	}


	public void setDevice(Device device) {
		this.device = device;
	}

	public Conference(String sessionId,String sessionName,Device device){
    	this.sessionId = sessionId;
    	this.sessionname = sessionName;
    	this.device = device;
    }
	
	public Conference(String sessionId,String sessionName){
    	this.sessionId = sessionId;
    	this.sessionname = sessionName;
    }
	
	Conference() { // jpa only
	}
}
