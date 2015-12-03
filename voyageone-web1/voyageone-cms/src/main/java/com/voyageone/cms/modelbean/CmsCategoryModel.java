package com.voyageone.cms.modelbean;

import java.util.List;

public class CmsCategoryModel  extends BaseModel{
	/**
	 * CMS類目id.
	 */
	private int categoryId;
	/**
	 * CMS渠道id.
	 */
	private String channelId;
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
	 * IMS主类目id.
	 */
	private int extendMainCategoryId;
	/**
	 * CMS当前类目的路径.
	 */
	private String cmsCategoryPath;
	/**
	 * IMS主类目路径.
	 */
	private String mainCategoryPath;
	/**
	 * 属性匹配完成状态.
	 */
	private int propMatchStatus;
	
	private List<CmsCategoryModel> children;

	public int getExtendMainCategoryId() {
		return extendMainCategoryId;
	}

	public int getPropMatchStatus() {
		return propMatchStatus;
	}

	public void setPropMatchStatus(int propMatchStatus) {
		this.propMatchStatus = propMatchStatus;
	}

	public void setExtendMainCategoryId(int extendMainCategoryId) {
		this.extendMainCategoryId = extendMainCategoryId;
	}

	public String getMainCategoryPath() {
		return mainCategoryPath;
	}

	public void setMainCategoryPath(String mainCategoryPath) {
		this.mainCategoryPath = mainCategoryPath;
	}

	public String getCmsCategoryPath() {
		return cmsCategoryPath;
	}

	public void setCmsCategoryPath(String cmsCategoryPath) {
		this.cmsCategoryPath = cmsCategoryPath;
	}

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
	public List<CmsCategoryModel> getChildren() {
		return children;
	}
	public void setChildren(List<CmsCategoryModel> children) {
		this.children = children;
	}
	
}
