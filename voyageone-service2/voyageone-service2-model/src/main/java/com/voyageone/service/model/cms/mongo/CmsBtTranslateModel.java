package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * Created by james on 2017/2/27.
 */

public class CmsBtTranslateModel extends BaseMongoModel {

    private String channelId;

    // 1: '共通属性', 2: '主类目属性',3: '供货商属性',4: '自定义属性'
    private Integer type;

    private String name;           //属性英文名

    private String valueEn;        //英文值

    private String valueCn;        //中文值

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
