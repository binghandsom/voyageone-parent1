package com.voyageone.cms.modelbean;

public class Category {

	private int categoryId;
	
	private String channelId;
	
	private String name;
	
	private String headerTitle;
	
	private String urlKey;
	
	private boolean isEnableFilter;
	
	private boolean isVisibleOnMenu;
	
	private boolean isPublished;
	
	private boolean isEffective;
	
	private String created;
	
	private String creater;
	
	private String modified;
	
	private String modifier;

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeaderTitle() {
		return headerTitle;
	}

	public void setHeaderTitle(String headerTitle) {
		this.headerTitle = headerTitle;
	}

	public String getUrlKey() {
		return urlKey;
	}

	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}

	public boolean isEnableFilter() {
		return isEnableFilter;
	}

	public void setIsEnableFilter(boolean isEnableFilter) {
		this.isEnableFilter = isEnableFilter;
	}

	public boolean isVisibleOnMenu() {
		return isVisibleOnMenu;
	}

	public void setIsVisibleOnMenu(boolean isVisibleOnMenu) {
		this.isVisibleOnMenu = isVisibleOnMenu;
	}

	public boolean isPublished() {
		return isPublished;
	}

	public void setIsPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}

	public boolean isEffective() {
		return isEffective;
	}

	public void setIsEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	
}
