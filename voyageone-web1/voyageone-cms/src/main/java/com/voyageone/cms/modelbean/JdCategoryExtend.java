package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.annotation.Extends;

public class JdCategoryExtend  {
	
	private int categoryId;
	
	private String channelId;
	@Extends
    private Integer jdCategoryId;

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

	public Integer getJdCategoryId() {
        return jdCategoryId;
    }

    public void setJdCategoryId(Integer jdCategoryId) {
        this.jdCategoryId = jdCategoryId;
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