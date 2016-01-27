package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 会议记录表
 * @author wangjun
 *
 */
@Entity
public class ConferenceRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6178741956835246835L;
	
	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	public Long id;
	
	/**
	 * 会议信息
	 */
	@JsonIgnore
	@ManyToOne
	private Conference conference;
	
	/**
	 * 帐号信息
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;
	
	/**
	 * 开始时间
	 */
	public Date beginDate = new Date();
	
	/**
	 * 结束时间
	 */
	public Date endDate = new Date();
	
	/**
	 * 操作类型
	 */
	public ConferenceOperatorType endReason = ConferenceOperatorType.CONNECT;

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

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public ConferenceOperatorType getEndReason() {
		return endReason;
	}

	public void setEndReason(ConferenceOperatorType endReason) {
		this.endReason = endReason;
	}
	
	
}
