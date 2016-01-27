package com.xst.bigwhite.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户会议关联表
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
	 * 帐号信息
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
	 * 是否会议发起人
	 */
	public Boolean ownered = new Boolean(false);


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


	public Conference getConference() {
		return conference;
	}


	public void setConference(Conference conference) {
		this.conference = conference;
	}


	public Boolean getOwnered() {
		return ownered;
	}


	public void setOwnered(Boolean ownered) {
		this.ownered = ownered;
	}
	

}
