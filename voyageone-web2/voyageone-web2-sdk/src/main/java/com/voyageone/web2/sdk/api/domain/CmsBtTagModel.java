package com.voyageone.web2.sdk.api.domain;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
public class CmsBtTagModel extends BaseModel {

    private int tagId;
    private String channelId;
    private String tagName;
    private String tagPath;
    private String tagPathName;
    private int tagType;
    private int tagStatus;
    private int sortOrder;
    private int parentTagId;

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

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

    public int getTagType() {
        return tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }

    public int getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(int tagStatus) {
        this.tagStatus = tagStatus;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getParentTagId() {
        return parentTagId;
    }

    public void setParentTagId(int parentTagId) {
        this.parentTagId = parentTagId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
