package com.xst.bigwhite.dtos;

import java.util.Date;

import com.xst.bigwhite.models.ConferenceAccountActionType;

/**
 * 参加会议请求信息
 * @author Administrator
 *
 */
public class ConferenceActionRequest extends ConferenceRequest {
	/**
     * 帐户手机
     */
	public String mobileno;

	/**
     * 加入会议的操作方式
     */
	public ConferenceAccountActionType actionType = ConferenceAccountActionType.CONNECT;
	
	/**
	 * 开始时间
	 */
	public Date beginDate = new Date();
	
	/**
	 * 结束时间
	 */
	public Date endDate = new Date();
	
	/**
	 * 分钟数
	 */
	public Integer minutes =new Integer(0);

	/**
	 * 说明文字
	 */
	public String operatorDesc;
	
	
	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
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

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public ConferenceAccountActionType getActionType() {
		return actionType;
	}

	public void setActionType(ConferenceAccountActionType actionType) {
		this.actionType = actionType;
	}

	public String getOperatorDesc() {
		return operatorDesc;
	}

	public void setOperatorDesc(String operatorDesc) {
		this.operatorDesc = operatorDesc;
	}

	
}
