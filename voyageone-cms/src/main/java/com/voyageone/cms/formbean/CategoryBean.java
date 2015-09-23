package com.voyageone.cms.formbean;

import java.util.ArrayList;
import java.util.List;

public class CategoryBean extends CategoryBaseBean{

	private String name;
	private String cnName;
	private List<CategoryBean> children = new ArrayList<CategoryBean>();
	
	private CategoryBean parent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public List<CategoryBean> getChildren() {
		return children;
	}
	public void setChildren(List<CategoryBean> children) {
		this.children = children;
	}
	/**
	 * @return the parent
	 */
	public CategoryBean getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(CategoryBean parent) {
		this.parent = parent;
	}
	
	
}
