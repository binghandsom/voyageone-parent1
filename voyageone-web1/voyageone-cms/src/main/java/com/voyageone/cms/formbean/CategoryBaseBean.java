package com.voyageone.cms.formbean;

public class CategoryBaseBean extends BaseBean implements IRecursion{
	private int categoryId;
	private String channelId;
	private int parentCategoryId;
	
	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
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
	 * @return the parentCategoryId
	 */
	public int getParentCategoryId() {
		return parentCategoryId;
	}
	/**
	 * @param parentCategoryId the parentCategoryId to set
	 */
	public void setParentCategoryId(int parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return getCategoryId();
	}
	@Override
	public int getParentId() {
		// TODO Auto-generated method stub
		return getParentCategoryId();
	}

}
