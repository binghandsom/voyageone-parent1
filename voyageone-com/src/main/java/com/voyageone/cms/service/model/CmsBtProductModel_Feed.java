package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

/**
 * 的商品Model feedAtts
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Feed extends BaseMongoMap<String, Object> {

    public String getCatId() {
        return getAttribute("catId");
    }

    public void setCatId(String catId) {
        setAttribute("catId", catId);
    }

    public String getModelCode() {
        return getAttribute("modelCode");
    }

    public void setModelCode(String modelCode) {
        setAttribute("modelCode", modelCode);
    }

}