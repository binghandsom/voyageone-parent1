/*
 * CmsBtSizeChartImageGroupModel.java
 * Copyright(C) 2001-2016 VoyageOne Group Inc.
 * All rights reserved.
 * This class was generated by code generator, please don't modify it.
 * -----------------------------------------------
 */
package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * 尺码表和尺码图片组关联表 一一对应
 */
public class CmsBtSizeChartImageGroupModel extends BaseModel {
    protected String channelId;

    /**
     * 尺码表
     */
    protected Integer cmsBtSizeChartId;

    /**
     * 尺码图片组
     */
    protected Long cmsBtImageGroupId;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getCmsBtSizeChartId() {
        return cmsBtSizeChartId;
    }

    public void setCmsBtSizeChartId(Integer cmsBtSizeChartId) {
        this.cmsBtSizeChartId = cmsBtSizeChartId;
    }

    public Long getCmsBtImageGroupId() {
        return cmsBtImageGroupId;
    }

    public void setCmsBtImageGroupId(Long cmsBtImageGroupId) {
        this.cmsBtImageGroupId = cmsBtImageGroupId;
    }
}