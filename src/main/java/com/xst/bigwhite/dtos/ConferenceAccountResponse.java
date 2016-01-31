package com.xst.bigwhite.dtos;

import java.util.Date;

import com.xst.bigwhite.models.ConferenceAccount;
import com.xst.bigwhite.models.ConferenceAccountStatusType;

/**
 *  一个用户参与的会议的详细信息
 * @author Administrator
 *
 */
public class ConferenceAccountResponse {
	/**
	 * 参加会议的状态
	 * 在线 或者离线
    */
	public ConferenceAccountStatusType status = ConferenceAccountStatusType.ONLINE;
	
	/**
	 * 会议总分钟数
	 */
	public Integer totoalMinutes = new Integer(0);

	/**
	 * 会议编号
	 */
	public String sessionId;

	public String ui;
	
	/**
	 * 会议名称
	 */
	public String sessionName;
	
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
	 * 设备号
	 * @return
	 */
	public String deviceno;
	
	/**
	 * 设备名称
	 */
	public String devicename;
	
	/**
	 * 用户名
	 * @return
	 */
	public String mobileno;
	
	/**
	 * 用户名
	 */
	public String nick;
	
	public Integer getTotoalMinutes() {
		return totoalMinutes;
	}


	public void setTotoalMinutes(Integer totoalMinutes) {
		this.totoalMinutes = totoalMinutes;
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



	public String getSessionName() {
		return sessionName;
	}



	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
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



	public ConferenceAccountStatusType getStatus() {
		return status;
	}


	public void setStatus(ConferenceAccountStatusType status) {
		this.status = status;
	}


	public String getDeviceno() {
		return deviceno;
	}


	public void setDeviceno(String deviceno) {
		this.deviceno = deviceno;
	}


	public String getDevicename() {
		return devicename;
	}


	public void setDevicename(String devicename) {
		this.devicename = devicename;
	}


	public String getMobileno() {
		return mobileno;
	}


	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}


	public String getNick() {
		return nick;
	}


	public void setNick(String nick) {
		this.nick = nick;
	}


	public static ConferenceAccountResponse mapping(ConferenceAccount conference) {
		ConferenceAccountResponse item = new ConferenceAccountResponse();
		
		item.setActiveTime(conference.getConference().getActiveTime());
		item.setCreateDate(conference.getConference().getCreateDate());
		item.setEndDate(conference.getConference().getEndDate());
		item.setSessionId(conference.getConference().getSessionId());
		item.setSessionName(conference.getConference().getSessionName());
		item.setTotoalMinutes(conference.getConference().getTotalMinutes());
		item.setStatus(conference.getStatus());
		
		item.setDevicename(conference.getConference().getDevice().name);
		item.setDeviceno(conference.getConference().getDevice().no);
		
		item.setMobileno(conference.getAccount().mobileno);
		item.setNick(conference.getAccount().getUsername());
		
		return item;
	}

}
