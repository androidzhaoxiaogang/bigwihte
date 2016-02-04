package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 账号会议关联表
 * @author wangjun
 *
 */
@Entity
public class ConferenceAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7474859764812811189L;


	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 参与会议的帐号信息
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	/**
	 * 会议信息
	 */
	@JsonIgnore
	@ManyToOne
	private Conference conference;
	
	/**
	 * 参加会议的状态
	 * 在线 或者离线
	 */
	@Enumerated(EnumType.STRING)
	public ConferenceAccountStatusType status = ConferenceAccountStatusType.ONLINE;
	
	/**
	 * 建立时间
	 */
	public Date createDate = new Date();
	
	/**
	 * 更新时间
	 */
	public Date updateDate =new Date();
	
	/**
	 * 会议总分钟数
	 */
	public Integer totalMinutes = new Integer(0);

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public Conference getConference() {
		return conference;
	}


	public void setConference(Conference conference) {
		this.conference = conference;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public ConferenceAccountStatusType getStatus() {
		return status;
	}


	public void setStatus(ConferenceAccountStatusType status) {
		this.status = status;
	}


	public Integer getTotalMinutes() {
		return totalMinutes;
	}


	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

    public Date getCreateDate() {
		return createDate;
	}


	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public	ConferenceAccount(Conference conference, Account account){
    	this.conference = conference;
    	this.account = account;
    }
    
	ConferenceAccount(){
		
	}
}
