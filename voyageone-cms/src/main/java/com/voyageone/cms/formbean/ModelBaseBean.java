package com.voyageone.cms.formbean;

public class ModelBaseBean extends BaseBean{

	private Integer categoryId;
	private Integer modelId;
	private String channelId;
    private Integer primaryCategoryId;
    private Integer isPrimaryCategory;
    private Integer cnIsPrimaryCategory;
    
	/**
	 * @return the modelId
	 */
	public Integer getModelId() {
		return modelId;
	}
	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	/**
	 * @return the primaryCategoryId
	 */
	public Integer getPrimaryCategoryId() {
		return primaryCategoryId;
	}
	/**
	 * @param primaryCategoryId the primaryCategoryId to set
	 */
	public void setPrimaryCategoryId(Integer primaryCategoryId) {
		this.primaryCategoryId = primaryCategoryId;
	}
	/**
	 * @return the cnIsPrimaryCategory
	 */
	public Integer getCnIsPrimaryCategory() {
		return cnIsPrimaryCategory;
	}
	/**
	 * @param cnIsPrimaryCategory the cnIsPrimaryCategory to set
	 */
	public void setCnIsPrimaryCategory(Integer cnIsPrimaryCategory) {
		this.cnIsPrimaryCategory = cnIsPrimaryCategory;
	}    
	/**
	 * @return the isPrimaryCategory
	 */
	public Integer getIsPrimaryCategory() {
		return isPrimaryCategory;
	}

	/**
	 * @param isPrimaryCategory the isPrimaryCategory to set
	 */
	public void setIsPrimaryCategory(Integer isPrimaryCategory) {
		this.isPrimaryCategory = isPrimaryCategory;
	}
	/**
	 * @return the categoryId
	 */
	public Integer getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
}
