package com.voyageone.batch.bi.bean.modelbean.jumei;

import java.util.Date;

public class JumeiProductAddedBean {

	private String code = "0";
	private String name;
	private String nameEn;

	private String brand;
	private String category;
	private String jumeiId;
	private String status;

	private Date createTime;
	private String createUser = "0";
	private Date updateTime;
	private String updateUser = "0";
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getJumeiId() {
		return jumeiId;
	}
	public void setJumeiId(String jumeiId) {
		this.jumeiId = jumeiId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String toString() {
		return code+";"+name+";"+nameEn+";"+brand+";"+category+";"+jumeiId+";"+status;
	}
}
