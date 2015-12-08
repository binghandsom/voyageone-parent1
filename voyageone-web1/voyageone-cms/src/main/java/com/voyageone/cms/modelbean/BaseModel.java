package com.voyageone.cms.modelbean;

import java.sql.Timestamp;

public class BaseModel {
	/**
	 * 创建时间.
	 */
	private Timestamp created;
	/**
	 * 创建者.
	 */
	private String creater;
	/**
	 * 更新时间.
	 */
	private Timestamp modified;
	/**
	 * 更新者.
	 */
	private String modifier;
	
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Timestamp getModified() {
		return modified;
	}
	public void setModified(Timestamp modified) {
		this.modified = modified;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
}
