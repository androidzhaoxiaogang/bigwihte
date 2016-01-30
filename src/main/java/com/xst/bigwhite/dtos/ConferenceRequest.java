package com.xst.bigwhite.dtos;

public class ConferenceRequest {
	
	/**
	 * 会议回话ID
	 */
    public String sessionId;
    
    /**
     * 会议UI
     */
    public String ui;
    

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
	

}
