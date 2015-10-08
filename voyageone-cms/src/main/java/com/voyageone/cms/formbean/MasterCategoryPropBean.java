package com.voyageone.cms.formbean;

public class MasterCategoryPropBean{
	/**
	 * 属性id.
	 */
	private int propId;
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
	 * 是否是父属性.
	 */
	private int isParent;
	/**
	 * 是否必填.
	 */
	private int isRequired;
	/**
	 * 是否是最顶层属性.
	 */
	private int isTopProp;
	/**
	 * 父属性id.
	 */
	private int parentPropId;
	/**
	 * 属性值uuid.
	 */
	private String valueUuid;
	/**
	 * 父属性值的uuid.
	 */
	private String valueParent;
	
	public int getPropId() {
		return propId;
	}
	public void setPropId(int propId) {
		this.propId = propId;
	}
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
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	public int getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}
	public int getIsTopProp() {
		return isTopProp;
	}
	public void setIsTopProp(int isTopProp) {
		this.isTopProp = isTopProp;
	}
	public int getParentPropId() {
		return parentPropId;
	}
	public void setParentPropId(int parentPropId) {
		this.parentPropId = parentPropId;
	}
	public String getValueUuid() {
		return valueUuid;
	}
	public void setValueUuid(String valueUuid) {
		this.valueUuid = valueUuid;
	}
	public String getValueParent() {
		return valueParent;
	}
	public void setValueParent(String valueParent) {
		this.valueParent = valueParent;
	}
	
}
