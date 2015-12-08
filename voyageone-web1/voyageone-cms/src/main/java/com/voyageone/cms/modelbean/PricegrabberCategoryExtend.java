package com.voyageone.cms.modelbean;

import java.util.Date;

public class PricegrabberCategoryExtend  {
	
	private Integer categoryId;

    private String channelId;
    
    private String priceGrabberCategoryId;

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
	public String getPriceGrabberCategoryId() {
		return priceGrabberCategoryId;
	}

	public void setPriceGrabberCategoryId(String priceGrabberCategoryId) {
		this.priceGrabberCategoryId = priceGrabberCategoryId;
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