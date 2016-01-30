package com.xst.bigwhite.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.xst.bigwhite.models.ConferenceStatusType;

/**
 * 会议的详细信息
 * @author wangjun
 *
 */
public class ConferenceResponse {

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
	public Date activeTime = new Date();
	
	/**
	 * 结束时间
	 */
	public Date endDate = new Date();
	
	/**
	 * 会议总时长 
	 * 单位:分钟
	 */
	public Integer totalMinutes = new Integer(0);
	
	 /** 会议的状态
	 * 开放 暂停 关闭
	 */
	public ConferenceStatusType status = ConferenceStatusType.OPENED;
	
	
	/**
	 * 参与会议的用户
	 */
	public List<AccountInfo> accounts =new ArrayList<>();


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


	public Integer getTotalMinutes() {
		return totalMinutes;
	}


	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}


	public ConferenceStatusType getStatus() {
		return status;
	}


	public void setStatus(ConferenceStatusType status) {
		this.status = status;
	}


	public List<AccountInfo> getAccounts() {
		return accounts;
	}


	public void setAccounts(List<AccountInfo> accounts) {
		this.accounts = accounts;
	}
	
	
}
