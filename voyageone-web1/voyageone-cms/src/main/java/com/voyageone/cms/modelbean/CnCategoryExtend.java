package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.annotation.Extends;

public class CnCategoryExtend {
	
	private int categoryId;
	
	private String channelId;
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
    private Integer sizeChartId;
	@Extends
	private String mainCategoryId;

    private Date created;

    private String creater;

    private Date modified;

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

	public String getCnSeoTitle() {
        return cnSeoTitle;
    }

    public void setCnSeoTitle(String cnSeoTitle) {
        this.cnSeoTitle = cnSeoTitle == null ? null : cnSeoTitle.trim();
    }

    public String getCnSeoDescription() {
        return cnSeoDescription;
    }

    public void setCnSeoDescription(String cnSeoDescription) {
        this.cnSeoDescription = cnSeoDescription == null ? null : cnSeoDescription.trim();
    }

    public String getCnSeoKeywords() {
        return cnSeoKeywords;
    }

    public void setCnSeoKeywords(String cnSeoKeywords) {
        this.cnSeoKeywords = cnSeoKeywords == null ? null : cnSeoKeywords.trim();
    }

    public Integer getHsCodeId() {
        return hsCodeId;
    }

    public void setHsCodeId(Integer hsCodeId) {
        this.hsCodeId = hsCodeId;
    }

    public Integer getHsCodePuId() {
        return hsCodePuId;
    }

    public void setHsCodePuId(Integer hsCodePuId) {
        this.hsCodePuId = hsCodePuId;
    }

    public Integer getSizeChartId() {
        return sizeChartId;
    }

    public void setSizeChartId(Integer sizeChartId) {
        this.sizeChartId = sizeChartId;
    }

    /**
	 * @return the mainCategoryId
	 */
	public String getMainCategoryId() {
		return mainCategoryId;
	}

	/**
	 * @param mainCategoryId the mainCategoryId to set
	 */
	public void setMainCategoryId(String mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier == null ? null : modifier.trim();
    }
}