package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author jerry 15/12/30
 * @version 2.0.0
 */
public class CmsBtTagModel extends BaseModel {

    private Integer tagId;
    private String channelId;
    private String tagName;
    private String tagPath;
    private String tagPathName;
    private Integer tagType;
    private Integer tagStatus;
    private Integer sortOrder;
    private Integer parentTagId;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public String getTagPathName() {
        return tagPathName;
    }

    public void setTagPathName(String tagPathName) {
        this.tagPathName = tagPathName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
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
}
