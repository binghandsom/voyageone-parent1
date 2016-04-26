package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtTagModel extends BaseModel {
    /**

     */
    private String channelId;
    /**

     */
    private String tagName;
    /**

     */
    private String tagPath;
    /**

     */
    private String tagPathName;
    /**
     * 1:店铺内分类 2:活动标签 3:货位标签
     */
    private int tagType;
    /**
     * 0:Open  1:Close
     */
    private int tagStatus;
    /**

     */
    private int sortOrder;
    /**
     * 父类目编号，值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目
     */
    private int parentTagId;
    /**

     */
    private int active;

    /**

     */
    public String getChannelId() {

        return this.channelId;
    }

    public void setChannelId(String channelId) {
        if (channelId != null) {
            this.channelId = channelId;
        } else {
            this.channelId = "";
        }

    }


    /**

     */
    public String getTagName() {

        return this.tagName;
    }

    public void setTagName(String tagName) {
        if (tagName != null) {
            this.tagName = tagName;
        } else {
            this.tagName = "";
        }

    }


    /**

     */
    public String getTagPath() {

        return this.tagPath;
    }

    public void setTagPath(String tagPath) {
        if (tagPath != null) {
            this.tagPath = tagPath;
        } else {
            this.tagPath = "";
        }

    }


    /**

     */
    public String getTagPathName() {

        return this.tagPathName;
    }

    public void setTagPathName(String tagPathName) {
        if (tagPathName != null) {
            this.tagPathName = tagPathName;
        } else {
            this.tagPathName = "";
        }

    }


    /**
     * 1:店铺内分类 2:活动标签 3:货位标签
     */
    public int getTagType() {

        return this.tagType;
    }

    public void setTagType(int tagType) {
        this.tagType = tagType;
    }


    /**
     * 0:Open  1:Close
     */
    public int getTagStatus() {

        return this.tagStatus;
    }

    public void setTagStatus(int tagStatus) {
        this.tagStatus = tagStatus;
    }


    /**

     */
    public int getSortOrder() {

        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }


    /**
     * 父类目编号，值等于0：表示此类目为店铺下的一级类目，值不等于0：表示此类目有父类目
     */
    public int getParentTagId() {

        return this.parentTagId;
    }

    public void setParentTagId(int parentTagId) {
        this.parentTagId = parentTagId;
    }


    /**

     */
    public int getActive() {

        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}