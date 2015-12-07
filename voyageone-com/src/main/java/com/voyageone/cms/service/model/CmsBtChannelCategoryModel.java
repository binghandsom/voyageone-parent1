package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mysql.BaseModel;

public class CmsBtChannelCategoryModel extends BaseModel {

    private String channelId;

    private String catId;

    public CmsBtChannelCategoryModel(){
    }

    public CmsBtChannelCategoryModel(String channelId, String catId, String modifier){
        this.channelId = channelId;
        this.catId = catId;
        this.setModifier(modifier);
        this.setCreater(modifier);
    }

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
}
