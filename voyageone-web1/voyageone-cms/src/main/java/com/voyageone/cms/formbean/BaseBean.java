package com.voyageone.cms.formbean;

public class BaseBean {

	private String created;

	private String creater;

	private String modified;

	private String modifier;
	
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created == null ? null:created.substring(0, 19);
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater == null ? null : creater.trim();
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified == null ? null:modified.substring(0, 19);
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier == null ? null : modifier.trim();
	}
	
}
