package com.voyageone.service.bean.cms;

/**
 * Created by DELL on 2016/3/11.
 */
public class CmsTagInfoBean {

    /**
     * channelId
     */
    private String channelId;

    /**
     * tagName
     */
    private String tagName;

    /**
     * tagType
     */
    private Integer tagType;

    /**
     * tagStatus
     */
    private Integer tagStatus;

    /**
     * sortOrder
     */
    private Integer sortOrder;

    /**
     * parentTagId
     */
    private Integer parentTagId;

    /**
     * creater
     */
    private String creater;

    /**
     *modifier
     */
    private String modifier;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public Integer getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(Integer tagStatus) {
        this.tagStatus = tagStatus;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getParentTagId() {
        return parentTagId;
    }

    public void setParentTagId(Integer parentTagId) {
        this.parentTagId = parentTagId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
