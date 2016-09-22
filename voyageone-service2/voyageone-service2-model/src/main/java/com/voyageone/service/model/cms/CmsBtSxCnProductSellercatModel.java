package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtSxCnProductSellercatModel extends BaseModel {
    private String channelId;

    private String catId;

    private String updFlg; // 0:未处理, 1:已处理

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getUpdFlg() {
        return updFlg;
    }

    public void setUpdFlg(String updFlg) {
        this.updFlg = updFlg;
    }
}