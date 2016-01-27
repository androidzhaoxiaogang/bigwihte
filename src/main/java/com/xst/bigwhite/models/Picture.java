package com.xst.bigwhite.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

/**
 * 图片信息表
 * @author wangjun
 *
 */
@Entity
public class Picture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4052946459095209733L;

	/**
	 * JPA 主键ID
	 */
	@Id
	@GeneratedValue
	private Long id;
	
	/**
	 * 图片URL
	 */
	@Size(min=2, max= 400)
	public String imageurl;
	
	/**
	 * 图片本地路径
	 */
	@Size(min=2, max= 400)
	public String imagepath;
	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getImagepath() {
		return imagepath;
	}

	public void setImagepath(String imagepath) {
		this.imagepath = imagepath;
	}

	public Picture(String imageurl,String imagepath){
    	this.imagepath = imageurl;
    	this.imageurl = imagepath;
    }
	
	Picture(){
		//JPA
	}
}
