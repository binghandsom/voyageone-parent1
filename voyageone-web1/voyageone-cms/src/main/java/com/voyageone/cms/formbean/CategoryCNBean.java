package com.voyageone.cms.formbean;

import com.voyageone.cms.annotation.Extends;

public class CategoryCNBean extends CategoryBaseBean{
	
	private String cnName;
	
	private String cnHeaderTitle;
	
	private String urlKey;
	
	private boolean cnIsEnableFilter;
	
	private boolean cnIsVisibleOnMenu;
	
	private boolean cnIsPublished;
	
	private String displayOrder;
	@Extends
	private String cnSeoTitle;
	@Extends
	private String cnSeoDescription;
	@Extends
	private String cnSeoKeywords;
	@Extends
	private Integer hsCodeId;
	@Extends
	private Integer hsCodePuId;
	@Extends
	private String sizeChartId;
	
	private String sizeChartName;
	
	private String sizeChartUrl;
	@Extends
	private String mainCategoryId;
	
	private String mainCategoryName;
	
	private String mainParentCategoryId;
	
	private String mainParentCategoryTypeId;
	@Extends
	private String tmCategoryId;
	
	private String tmCategoryName;
	@Extends
	private String jdCategoryId;
	
	private String jdCategoryName;
	
	private String publishDatetime;

	
	public String getUrlKey() {
		return urlKey;
	}
	public void setUrlKey(String urlKey) {
		this.urlKey = urlKey;
	}
	/**
	 * @return the cnName
	 */
	public String getCnName() {
		return cnName;
	}
	/**
	 * @param cnName the cnName to set
	 */
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	/**
	 * @return the cnHeaderTitle
	 */
	public String getCnHeaderTitle() {
		return cnHeaderTitle;
	}
	/**
	 * @param cnHeaderTitle the cnHeaderTitle to set
	 */
	public void setCnHeaderTitle(String cnHeaderTitle) {
		this.cnHeaderTitle = cnHeaderTitle;
	}
	
	public boolean isCnIsVisibleOnMenu() {
		return cnIsVisibleOnMenu;
	}
	public void setCnIsVisibleOnMenu(boolean cnIsVisibleOnMenu) {
		this.cnIsVisibleOnMenu = cnIsVisibleOnMenu;
	}
	
	/**
	 * @return the cnSeoTitle
	 */
	public String getCnSeoTitle() {
		return cnSeoTitle;
	}
	/**
	 * @param cnSeoTitle the cnSeoTitle to set
	 */
	public void setCnSeoTitle(String cnSeoTitle) {
		this.cnSeoTitle = cnSeoTitle;
	}
	/**
	 * @return the cnSeoDescription
	 */
	public String getCnSeoDescription() {
		return cnSeoDescription;
	}
	/**
	 * @param cnSeoDescription the cnSeoDescription to set
	 */
	public void setCnSeoDescription(String cnSeoDescription) {
		this.cnSeoDescription = cnSeoDescription;
	}
	/**
	 * @return the cnSeoKeywords
	 */
	public String getCnSeoKeywords() {
		return cnSeoKeywords;
	}
	/**
	 * @param cnSeoKeywords the cnSeoKeywords to set
	 */
	public void setCnSeoKeywords(String cnSeoKeywords) {
		this.cnSeoKeywords = cnSeoKeywords;
	}
	/**
	 * @return the hsCodeId
	 */
	public Integer getHsCodeId() {
		return hsCodeId;
	}
	/**
	 * @param hsCodeId the hsCodeId to set
	 */
	public void setHsCodeId(Integer hsCodeId) {
		this.hsCodeId = hsCodeId;
	}
	/**
	 * @return the hsCodePuId
	 */
	public Integer getHsCodePuId() {
		return hsCodePuId;
	}
	/**
	 * @param hsCodePuId the hsCodePuId to set
	 */
	public void setHsCodePuId(Integer hsCodePuId) {
		this.hsCodePuId = hsCodePuId;
	}
	/**
	 * @return the sizeChartId
	 */
	public String getSizeChartId() {
		return sizeChartId;
	}
	/**
	 * @param sizeChartId the sizeChartId to set
	 */
	public void setSizeChartId(String sizeChartId) {
		this.sizeChartId = sizeChartId;
	}
    
	/**
	 * @return the tmCategoryId
	 */
	public String getTmCategoryId() {
		return tmCategoryId;
	}
	/**
	 * @param tmCategoryId the tmCategoryId to set
	 */
	public void setTmCategoryId(String tmCategoryId) {
		this.tmCategoryId = tmCategoryId;
	}
	/**
	 * @return the jdCategoryId
	 */
	public String getJdCategoryId() {
		return jdCategoryId;
	}
	/**
	 * @param jdCategoryId the jdCategoryId to set
	 */
	public void setJdCategoryId(String jdCategoryId) {
		this.jdCategoryId = jdCategoryId;
	}
	public boolean isCnIsPublished() {
		return cnIsPublished;
	}
	public void setCnIsPublished(boolean cnIsPublished) {
		this.cnIsPublished = cnIsPublished;
	}
	public boolean isCnIsEnableFilter() {
		return cnIsEnableFilter;
	}
	public void setCnIsEnableFilter(boolean cnIsEnableFilter) {
		this.cnIsEnableFilter = cnIsEnableFilter;
	}
	public String getMainCategoryId() {
		return mainCategoryId;
	}
	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}
	/**
	 * @return the publishDatetime
	 */
	public String getPublishDatetime() {
		return publishDatetime;
	}
	/**
	 * @param publishDatetime the publishDatetime to set
	 */
	public void setPublishDatetime(String publishDatetime) {
		this.publishDatetime = publishDatetime;
	}
	/**
	 * @return the sizeChartName
	 */
	public String getSizeChartName() {
		return sizeChartName;
	}
	/**
	 * @param sizeChartName the sizeChartName to set
	 */
	public void setSizeChartName(String sizeChartName) {
		this.sizeChartName = sizeChartName;
	}
	/**
	 * @return the sizeChartUrl
	 */
	public String getSizeChartUrl() {
		return sizeChartUrl;
	}
	/**
	 * @param sizeChartUrl the sizeChartUrl to set
	 */
	public void setSizeChartUrl(String sizeChartUrl) {
		this.sizeChartUrl = sizeChartUrl;
	}
	/**
	 * @return the mainCategoryName
	 */
	public String getMainCategoryName() {
		return mainCategoryName;
	}
	/**
	 * @param mainCategoryName the mainCategoryName to set
	 */
	public void setMainCategoryName(String mainCategoryName) {
		this.mainCategoryName = mainCategoryName;
	}
	/**
	 * @return the mainParentCategoryId
	 */
	public String getMainParentCategoryId() {
		return mainParentCategoryId;
	}
	/**
	 * @param mainParentCategoryId the mainParentCategoryId to set
	 */
	public void setMainParentCategoryId(String mainParentCategoryId) {
		this.mainParentCategoryId = mainParentCategoryId;
	}
	/**
	 * @return the mainParentCategoryTypeId
	 */
	public String getMainParentCategoryTypeId() {
		return mainParentCategoryTypeId;
	}
	/**
	 * @param mainParentCategoryTypeId the mainParentCategoryTypeId to set
	 */
	public void setMainParentCategoryTypeId(String mainParentCategoryTypeId) {
		this.mainParentCategoryTypeId = mainParentCategoryTypeId;
	}
	/**
	 * @return the tmCategoryName
	 */
	public String getTmCategoryName() {
		return tmCategoryName;
	}
	/**
	 * @param tmCategoryName the tmCategoryName to set
	 */
	public void setTmCategoryName(String tmCategoryName) {
		this.tmCategoryName = tmCategoryName;
	}
	/**
	 * @return the jdCategoryName
	 */
	public String getJdCategoryName() {
		return jdCategoryName;
	}
	/**
	 * @param jdCategoryName the jdCategoryName to set
	 */
	public void setJdCategoryName(String jdCategoryName) {
		this.jdCategoryName = jdCategoryName;
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
	
    
	
	
	
}
