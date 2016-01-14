package com.voyageone.cms.modelbean;

public class FeedDefaultPropOptionModel extends BaseModel {

	/**
	 * 属性名.
	 */
	private String propName;
	/**
	 * 属性选项名.
	 */
	private String optionName;
	/**
	 * 属性选项值.
	 */
	private String optionValue;
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public String getOptionName() {
		return optionName;
	}
	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	
}
