package com.xst.bigwhite.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * VerifyMessage 验证消息类
 * @author wangjun
 *
 */
@Entity
public class VerifyMessage implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7201243189478581054L;

	/**
	 * JPA 主键
	 */
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;
	
	/**
	 * 手机号
	 */
	@JsonIgnore
	@NotBlank(message="手机号不能为空")  
	@Size(min=11, max=20)
	public String mobileno;
	
	/**
	 * 验证码
	 */
	@NotBlank(message="验证不能为空")
	@Size(min=2, max=12)
	public String verifycode;
	
	/**
	 * 生成日期
	 */
	public Date createdate;
	
	/**
	 * 是否已验证
	 */
	public Boolean used = new Boolean(false);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Boolean getUsed() {
		return used;
	}

	public void setUsed(Boolean used) {
		this.used = used;
	}
	
	public VerifyMessage(String mobileno,String verifycode){
		this.mobileno = mobileno;
		this.verifycode = verifycode;
	}
	
	VerifyMessage(){
		
	}
}
