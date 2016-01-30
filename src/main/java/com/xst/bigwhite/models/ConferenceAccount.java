package com.xst.bigwhite.models;

import java.io.Serializable;

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
	public ConferenceJoinStatusType status = ConferenceJoinStatusType.ONLINE;
	
	/**
	 * 会议总分钟数
	 */
	public int minutes;

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


	public ConferenceJoinStatusType getStatus() {
		return status;
	}


	public void setStatus(ConferenceJoinStatusType status) {
		this.status = status;
	}


	public int getMinutes() {
		return minutes;
	}


	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}


}
