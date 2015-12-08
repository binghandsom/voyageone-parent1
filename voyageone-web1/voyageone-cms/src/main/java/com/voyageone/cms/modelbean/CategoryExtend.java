package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.annotation.Extends;

public class CategoryExtend  {
	
    private int categoryId;
	
	private String channelId;
	@Extends
    private String seoTitle;
	@Extends
    private String seoDescription;
	@Extends
    private String seoKeywords;
	@Extends
    private String seoCanonical;
	@Extends
	private String mainCategoryId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;
    
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

	public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle == null ? null : seoTitle.trim();
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription == null ? null : seoDescription.trim();
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords == null ? null : seoKeywords.trim();
    }

    public String getSeoCanonical() {
        return seoCanonical;
    }

    public void setSeoCanonical(String seoCanonical) {
        this.seoCanonical = seoCanonical == null ? null : seoCanonical.trim();
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