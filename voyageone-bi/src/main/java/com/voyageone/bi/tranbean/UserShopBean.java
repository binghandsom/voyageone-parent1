package com.voyageone.bi.tranbean;

import java.io.Serializable;

public class UserShopBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6414528404478513709L;
	
	private int id;
	private String code;
	private String name;
	private String name_en;
	private String channel_id;
	private String ecomm_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getEcomm_id() {
		return ecomm_id;
	}
	public void setEcomm_id(String ecomm_id) {
		this.ecomm_id = ecomm_id;
	}
	
}