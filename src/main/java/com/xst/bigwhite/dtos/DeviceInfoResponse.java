package com.xst.bigwhite.dtos;

import java.util.ArrayList;
import java.util.List;


/**
 * 设备下所有的账户信息
 * @author wangjun
 *
 */
public class DeviceInfoResponse {
	
	/**
     * 大白号
     */
	public String deviceno ;
    
	/**
	 * 设备名称
	 */
	public String devicename;
	
	
	private List<DeviceAccountInfo> accounts =new ArrayList<>();


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


	public List<DeviceAccountInfo> getAccounts() {
		return accounts;
	}


	public void setAccounts(List<DeviceAccountInfo> accounts) {
		this.accounts = accounts;
	}
	
	

}
