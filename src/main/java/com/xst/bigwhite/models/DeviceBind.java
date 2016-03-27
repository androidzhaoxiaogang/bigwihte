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
 * 设备管理的设备
 * @author wangjun
 *
 */
@Entity
public class DeviceBind implements Serializable {
	
	/**
	 * 绑定状态
	 * @author wangjun
	 *
	 */
	public enum BindStatus{
		Binded,
		Unbind,
		Injected
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8645320741500649511L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 设备
	 */
	@JsonIgnore
	@ManyToOne
	private Device device;
	
	
	/**
	 * 设备关联的设备
	 */
	@JsonIgnore
	@ManyToOne
	private Device binded;
	
	/**
	 * 是否
	 */
	public Boolean confirmed;
	
	/**
	 * 绑定日期
	 */
	public Date bindDate = new Date();

	/**
	 * 修改日期
	 */
	public Date updateDate = new Date();
	
	/**
	 * 备注信息
	 */
	public String note;
	
	/**
	 * 设备是否启用IPC
	 */
	public Boolean ipc =new Boolean(false);
	
	
	/**
	 * 绑定状态
	 */
	@Enumerated(EnumType.STRING)
    public BindStatus status = BindStatus.Unbind;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}



	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Device getDevice() {
		return device;
	}


	public void setDevice(Device device) {
		this.device = device;
	}


	public Device getBinded() {
		return binded;
	}


	public void setBinded(Device binded) {
		this.binded = binded;
	}


	public Date getBindDate() {
		return bindDate;
	}


	public void setBindDate(Date bindDate) {
		this.bindDate = bindDate;
	}


	public Date getUpdateDate() {
		return updateDate;
	}


	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public BindStatus getStatus() {
		return status;
	}


	public void setStatus(BindStatus status) {
		this.status = status;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}


	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}


	public Boolean getIpc() {
		return ipc;
	}


	public void setIpc(Boolean ipc) {
		this.ipc = ipc;
	}


	public DeviceBind(Device device, Device bindDevice) {
		this.binded = bindDevice;
		this.device = device;
	}

	
	DeviceBind() { // jpa only
	}
}
