package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class CategoryUSBean  extends  CategoryBaseBean{
	
	private String showName;
  
	private String name;
	
	private String headerTitle;
	
	private String urlKey;
	
	private boolean isEnableFilter;
	
	private boolean isVisibleOnMenu;
	
	private boolean isPublished;
	
	private boolean isEffective;
	
	private String categoryParentUrl;
	@Extends
	private String seoCanonical;
	@Extends
	private String seoTitle;
	@Extends
	private String seoDescription;
	@Extends
	private String seoKeywords;
	@Extends
	private String googleCategoryId;
	@Extends
	private String amazonBrowseCategoryId;
	
	private String displayOrder;
	@Extends
	private String priceGrabberCategoryId;
	@Extends
	private String mainCategoryId;
	
	private String publishDatetime;
	
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

	public void setEnableFilter(boolean isEnableFilter) {
		this.isEnableFilter = isEnableFilter;
	}

	public String getSeoCanonical() {
		return seoCanonical;
	}

	public void setSeoCanonical(String seoCanonical) {
		this.seoCanonical = seoCanonical;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}
	
	public String getGoogleCategoryId() {
		return googleCategoryId;
	}

	public void setGoogleCategoryId(String googleCategoryId) {
		this.googleCategoryId = googleCategoryId;
	}

	public String getAmazonBrowseCategoryId() {
		return amazonBrowseCategoryId;
	}

	public void setAmazonBrowseCategoryId(String amazonBrowseCategoryId) {
		this.amazonBrowseCategoryId = amazonBrowseCategoryId;
	}

	/**
	 * @return the displayOrder
	 */
	public String getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getPriceGrabberCategoryId() {
		return priceGrabberCategoryId;
	}

	public void setPriceGrabberCategoryId(String priceGrabberCategoryId) {
		this.priceGrabberCategoryId = priceGrabberCategoryId;
	}

	public void setVisibleOnMenu(boolean isVisibleOnMenu) {
		this.isVisibleOnMenu = isVisibleOnMenu;
	}

	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}

	public void setEffective(boolean isEffective) {
		this.isEffective = isEffective;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	

	public String getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public String getCategoryParentUrl() {
		return categoryParentUrl;
	}

	public void setCategoryParentUrl(String categoryParentUrl) {
		this.categoryParentUrl = categoryParentUrl;
	}

	public String getPublishDatetime() {
		return publishDatetime;
	}

	public void setPublishDatetime(String publishDatetime) {
		this.publishDatetime = publishDatetime;
	}


	
	
}
