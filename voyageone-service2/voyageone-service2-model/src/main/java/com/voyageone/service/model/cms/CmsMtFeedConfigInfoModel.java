package com.voyageone.service.model.cms;

import com.voyageone.base.dao.mysql.BaseModel;

/**
 * Created by gjl on 2016/12/27.
 */
public class CmsMtFeedConfigInfoModel extends BaseModel {

    private String orderChannelId;

    private String cfgIsAttribute;

    private String cfgName;

    private String cfgTableName;

    public String getOrderChannelId() {
        return orderChannelId;
    }

    public void setOrderChannelId(String orderChannelId) {
        this.orderChannelId = orderChannelId;
    }

    public String getCfgIsAttribute() {
        return cfgIsAttribute;
    }

    public void setCfgIsAttribute(String cfgIsAttribute) {
        this.cfgIsAttribute = cfgIsAttribute;
    }

    public String getCfgName() {
        return cfgName;
    }

    public void setCfgName(String cfgName) {
        this.cfgName = cfgName;
    }

    public String getCfgTableName() {
        return cfgTableName;
    }

    public void setCfgTableName(String cfgTableName) {
        this.cfgTableName = cfgTableName;
    }
}
