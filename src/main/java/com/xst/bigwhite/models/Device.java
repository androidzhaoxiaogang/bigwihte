package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Device 设备信息类 
 * 手机  平板 大白 等设备的描述信息   
 * @author wangjun
 *
 */
@Entity
public class Device implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3403056606380429836L;

	/**
	 * 设备的类型
	 * 手机  平板 大白
	 * @author wangjun
	 *
	 */
	public enum DeviceType {
		/**
		 * 手机
		 */
		Mobile,
		/**
		 * 平板
		 */
		Pad,
		/**
		 * 大白
		 */
		Bigwhite
	}
	

	/**
	 * JPA 主键ID
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * 设备绑定的用户
	 */
	@OneToMany(mappedBy = "device")
    private Set<AccountDevice> devices = new HashSet<>();

	
	/**
	 * 设备的登入历史信息
	 */
	@OneToMany(mappedBy = "device")
	@JsonIgnore
    private Set<AccountLogin> logins = new HashSet<>();
	
	/**
	 * 设备所属用户的帐号 即管理员
	 */
	@JsonIgnore
	@ManyToOne
	private Account account;

	
	/**
	 * 设备唯一号
	 */
	@NotBlank
	@Size(min=2, max= 60)
	public String sn;
	
	/**
	 * 大白设备编号
	 * 按规则生成的号码 供设备分享,查找等使用
	 */
	@Size(min=2, max= 20)
	public String no;
	
	/**
	 * 大白头像
	 */
	@Size(min=2, max= 400)
	public String headimage;
	
	
	/**
	 * 设备名称
	 */
	@NotBlank
	@Size(min=2, max= 40)
	public String name;

	/**
	 * 设备的MAC地址
	 */
	@Size(min=2, max= 40)
	public String mac;
	
	/**
	 *设备类型
	 */
    @Enumerated(EnumType.ORDINAL)
    public DeviceType devicetype = DeviceType.Bigwhite;
	
    /**
     * 设备系统名称
     */
    @Size(min=2, max= 40)
    public String os;
    
	/**
	 * 设备描述信息
	 */
    @Size(min=2, max= 255)
	public String description;
	
	
	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}


	public Set<AccountLogin> getLogins() {
		return logins;
	}


	public void setLogins(Set<AccountLogin> logins) {
		this.logins = logins;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}


	public String getNo() {
		return no;
	}


	public void setNo(String no) {
		this.no = no;
	}


	public Set<AccountDevice> getDevices() {
		return devices;
	}


	public void setDevices(Set<AccountDevice> devices) {
		this.devices = devices;
	}


	public String getHeadimage() {
		return headimage;
	}


	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getMac() {
		return mac;
	}


	public void setMac(String mac) {
		this.mac = mac;
	}



	public DeviceType getDevicetype() {
		return devicetype;
	}


	public void setDevicetype(DeviceType devicetype) {
		this.devicetype = devicetype;
	}


	public String getOs() {
		return os;
	}


	public void setOs(String os) {
		this.os = os;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Device(String name, String sn,String mac){
		this.name = name;
		this.sn = sn;
		this.mac = mac;
	}
	
	public Device(Account acount,String name, String sn,String mac){
		this(name,sn,mac);
		this.account = acount;
	}

	Device() { // jpa only
	}
}
