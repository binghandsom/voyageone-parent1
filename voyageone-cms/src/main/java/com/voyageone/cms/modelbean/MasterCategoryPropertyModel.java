package com.voyageone.cms.modelbean;

import java.util.List;

public class MasterCategoryPropertyModel extends BaseModel implements Comparable<MasterCategoryPropertyModel>{
	/**
	 * 属性id.
	 */
	private Integer propId;
	/**
	 * 属性名.
	 */
	private String propName;
	/**
	 * 属性类型.
	 */
	private Integer propType;
	/**
	 * 属性值.
	 */
	private String propValue;
	/**
	 * 是否是父属性.
	 */
	private Integer isParent;
	/**
	 * 是否必填.
	 */
	private Integer isRequired;
	/**
	 * 是否是最顶层属性.
	 */
	private Integer isTopProp;
	/**
	 * 父属性id.
	 */
	private Integer parentPropId;
	/**
	 * 属性值uuid.
	 */
	private String valueUuid;
	/**
	 * 父属性值的uuid.
	 */
	private String valueParent;
	
	/**
	 * 孩子节点.
	 */
	private List<MasterCategoryPropertyModel> children;
	/**
	 * 属性选项值.
	 */
	private List<PropertyOption> options;
	/**
	 * 属性规则.
	 */
	private List<PropertyRule> rules;
	public Integer getPropId() {
		return propId;
	}
	public void setPropId(Integer propId) {
		this.propId = propId;
	}
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public Integer getPropType() {
		return propType;
	}
	public void setPropType(Integer propType) {
		this.propType = propType;
	}
	public String getPropValue() {
		return propValue;
	}
	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}
	public Integer getIsParent() {
		return isParent;
	}
	public void setIsParent(Integer isParent) {
		this.isParent = isParent;
	}
	public Integer getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}
	public Integer getIsTopProp() {
		return isTopProp;
	}
	public void setIsTopProp(Integer isTopProp) {
		this.isTopProp = isTopProp;
	}
	public Integer getParentPropId() {
		return parentPropId;
	}
	public void setParentPropId(Integer parentPropId) {
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
	public List<MasterCategoryPropertyModel> getChildren() {
		return children;
	}
	public void setChildren(List<MasterCategoryPropertyModel> children) {
		this.children = children;
	}
	public List<PropertyOption> getOptions() {
		return options;
	}
	public void setOptions(List<PropertyOption> options) {
		this.options = options;
	}
	public List<PropertyRule> getRules() {
		return rules;
	}
	public void setRules(List<PropertyRule> rules) {
		this.rules = rules;
	}
	
	/**
	 * 实现比较接口.
	 */
	@Override
	public int compareTo(MasterCategoryPropertyModel paramT) {
		return this.isRequired.compareTo(paramT.isRequired);
	}
	
}
