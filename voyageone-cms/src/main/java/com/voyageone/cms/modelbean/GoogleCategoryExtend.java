package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.annotation.Extends;

public class GoogleCategoryExtend  {
	
	private Integer categoryId;

    private String channelId;
    @Extends
    private String googleCategoryId;

    private Date created;

    private String creater;

    private Date modified;

    private String modifier;
    
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }
    public String getGoogleCategoryId() {
		return googleCategoryId;
	}

	public void setGoogleCategoryId(String googleCategoryId) {
		this.googleCategoryId = googleCategoryId;
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