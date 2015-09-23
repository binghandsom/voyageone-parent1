package com.voyageone.cms.formbean;

public class CmsCategoryBean {
	/**
	 * CMS渠道id.
	 */
	private String channelId;
	/**
	 * CMS類目id.
	 */
	private int categoryId;
	/**
	 * CMS当前类目的路径.
	 */
	private String cmsCategoryPath;
	/**
	 * CMS父类目id.
	 */
	private int parentCategoryId;
	/**
	 * CMS类目英文名称.
	 */
	private String enName;
	/**
	 * CMS中文名称.
	 */
	private String cnName;
	/**
	 * IMS主类目id.
	 */
	private int mainCategoryId;
	/**
	 * IMS主类目路径.
	 */
	private String mainCategoryPath;
	
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

	public int getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(int parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public int getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(int mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}


	public String getCmsCategoryPath() {
		return cmsCategoryPath;
	}

	public void setCmsCategoryPath(String cmsCategoryPath) {
		this.cmsCategoryPath = cmsCategoryPath;
	}

	public String getMainCategoryPath() {
		return mainCategoryPath;
	}

	public void setMainCategoryPath(String mainCategoryPath) {
		this.mainCategoryPath = mainCategoryPath;
	}
	
}
