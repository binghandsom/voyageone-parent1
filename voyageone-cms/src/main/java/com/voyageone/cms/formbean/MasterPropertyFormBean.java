package com.voyageone.cms.formbean;

import java.util.Map;

public class MasterPropertyFormBean {
    
	/**
	 * 渠道id.
	 */
	private String channelId;
	/**
	 * 主数据类目id.
	 */
	private String categoryId;
	/**
	 * 
	 */
	private String parentLevel;
	/**
	 * 
	 */
	private String parentLevelValue;
	/**
	 * 
	 */
	private String level;
	/**
	 * 
	 */
	private String levelValue;
	/**
	 * 
	 */
	private Map<String, Object> hiddenInfo;
	/**
	 * 
	 */
	private Map<String, Object> propModel;

	public String getChannelId() {
	    return channelId;
	}

	public void setChannelId(String channelId) {
	    this.channelId = channelId;
	}

	public String getCategoryId() {
	    return categoryId;
	}

	public void setCategoryId(String categoryId) {
	    this.categoryId = categoryId;
	}

	public String getParentLevel() {
	    return parentLevel;
	}

	public void setParentLevel(String parentLevel) {
	    this.parentLevel = parentLevel;
	}

	public String getParentLevelValue() {
	    return parentLevelValue;
	}

	public void setParentLevelValue(String parentLevelValue) {
	    this.parentLevelValue = parentLevelValue;
	}

	public String getLevel() {
	    return level;
	}

	public void setLevel(String level) {
	    this.level = level;
	}

	public String getLevelValue() {
	    return levelValue;
	}

	public void setLevelValue(String levelValue) {
	    this.levelValue = levelValue;
	}

	public Map<String, Object> getHiddenInfo() {
	    return hiddenInfo;
	}

	public void setHiddenInfo(Map<String, Object> hiddenInfo) {
	    this.hiddenInfo = hiddenInfo;
	}

	public Map<String, Object> getPropModel() {
	    return propModel;
	}

	public void setPropModel(Map<String, Object> propModel) {
	    this.propModel = propModel;
	}
	
}
