package com.xst.bigwhite.models;

/**
 * 参与会议的操作
 * @author Administrator
 *
 */
public enum ConferenceAccountActionType{
	 /**
	  * 连接
	  */
	 CONNECT,
	 /**
	  * 重连
	  */
	 RECONNECT,
	 /**
	  * 会议中
	  */
	 SESSIONING,
	 /**
	  * 断开
	  */
	 DISCONNECT,
	 /**
	  * 异常中断
	  */
	 ABORT,
}
