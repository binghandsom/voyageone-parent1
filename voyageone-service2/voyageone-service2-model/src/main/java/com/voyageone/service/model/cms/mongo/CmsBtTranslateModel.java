package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.masterdate.schema.utils.StringUtil;

/**
 * Created by james on 2017/2/27.
 */
public class CmsBtTranslateModel extends BaseMongoModel {

    private String channelId;

    private Integer type;

    private String name;

    private String valueEn;

    private String valueCn;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueEn() {
        return valueEn;
    }

    public void setValueEn(String valueEn) {
        this.valueEn = valueEn;
    }

    public String getValueCn() {
        return valueCn;
    }

    public void setValueCn(String valueCn) {
        this.valueCn = valueCn;
    }
}
