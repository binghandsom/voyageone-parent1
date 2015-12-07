package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

public class CmsBtProductModel_Feed extends BaseMongoMap {

    public String getCatId() {
        return (String) getAttribute("catId");
    }

    public void setCatId(String catId) {
        setAttribute("catId", catId);
    }

}