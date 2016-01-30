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
	 * 设备信息
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
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
	@Enumerated(EnumType.STRING)
	public ConferenceOperatorType operatorType = ConferenceOperatorType.CREATE;

	/**
	 * 操作描述
	 */
	public String operatorDesc;
	
	
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


	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
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

	
	public ConferenceOperatorType getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(ConferenceOperatorType operatorType) {
		this.operatorType = operatorType;
	}
	
	

	public String getOperatorDesc() {
		return operatorDesc;
	}

	public void setOperatorDesc(String operatorDesc) {
		this.operatorDesc = operatorDesc;
	}

	ConferenceRecord(){
		//JPA
	}
	
	public ConferenceRecord(Conference conference,Device device,ConferenceOperatorType operatorType){
		this.conference = conference;
		this.device = device;
	    this.operatorType = operatorType;
	}
}
