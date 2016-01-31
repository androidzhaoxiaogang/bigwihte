package com.xst.bigwhite.dtos;

import com.xst.bigwhite.models.ConferenceStatusType;

/**
 * 修改会议的状态
 * @author wangjun
 *
 */
public class ConferenceUpdateRequest extends ConferenceRequest {
   
	/**
	 *  修改会议的状态
	 */
	public ConferenceStatusType status = ConferenceStatusType.CLOSED;
	
	/**
	 * 当前会议的总分钟数
	 */
	public Integer totalMinutes = new Integer(0);
	

	public ConferenceStatusType getStatus() {
		return status;
	}

	public void setStatus(ConferenceStatusType status) {
		this.status = status;
	}

	public Integer getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(Integer totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
	

}
