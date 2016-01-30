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

	public ConferenceStatusType getStatus() {
		return status;
	}

	public void setStatus(ConferenceStatusType status) {
		this.status = status;
	}
	

}
