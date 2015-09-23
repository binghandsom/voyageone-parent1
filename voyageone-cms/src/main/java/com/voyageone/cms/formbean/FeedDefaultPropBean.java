package com.voyageone.cms.formbean;

import java.util.List;

public class FeedDefaultPropBean {
	/**
	 * 渠道id.
	 */
	private String channelId;
	/**
	 * 属性名.
	 */
	private String propName;
	/**
	 * 属性类型.
	 */
	private int propType;
	/**
	 * 属性值.
	 */
	private String propValue;
	/**
	 * 属性值列表.
	 */
	private List<String> propValues;
	/**
	 * 是否已经设定.
	 */
	private boolean isDone;
	/**
	 * 属性选项.
	 */
	private List<FeedDefaultPropOptionBean> options;
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public int getPropType() {
		return propType;
	}
	public void setPropType(int propType) {
		this.propType = propType;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public List<FeedDefaultPropOptionBean> getOptions() {
		return options;
	}
	public void setOptions(List<FeedDefaultPropOptionBean> options) {
		this.options = options;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
	public List<String> getPropValues() {
		return propValues;
	}
	public void setPropValues(List<String> propValues) {
		this.propValues = propValues;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
