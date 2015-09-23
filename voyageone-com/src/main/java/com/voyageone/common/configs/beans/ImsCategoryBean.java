package com.voyageone.common.configs.beans;

import java.util.List;

public class ImsCategoryBean {
	private int categoryId;
	private int parentCid;
	private int isParent;
	private String categoryName;
	private String categoryPath;
	private int sortOrder;
	private List<ImsCategoryBean> subCategories;
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getParentCid() {
		return parentCid;
	}
	public void setParentCid(int parentCid) {
		this.parentCid = parentCid;
	}
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryPath() {
		return categoryPath;
	}
	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	public List<ImsCategoryBean> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<ImsCategoryBean> subCategories) {
		this.subCategories = subCategories;
	}
	
	
}
