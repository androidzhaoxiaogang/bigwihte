package com.xst.bigwhite.models;

/**
 * 会议操作类型
 * @author wangjun
 *
 */
public enum ConferenceOperatorType {
	/**
	 * 建立会议
	 */
	 CREATE,
	 /**
	  * 继续会议
	  */
	 OPEN,
	 /**
	  * 关闭
	  */
	 CLOSE,
	 /**
	  * 异常关闭
	  */
     ABORT,
}

