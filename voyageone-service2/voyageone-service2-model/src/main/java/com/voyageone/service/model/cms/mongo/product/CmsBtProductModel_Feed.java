package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Product 的商品Model feed
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Feed  {

    private String catId;

    private String catPath;

    private BaseMongoMap<String, Object> orgAtts = new BaseMongoMap<>();

    private BaseMongoMap<String, Object> cnAtts = new BaseMongoMap<>();

    private List<String> customIds = new ArrayList<>();

    private List<String> customIdsCn = new ArrayList<>();

    //catId
    public String getCatPath() {
        return catPath;
    }
    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }

    //catPath
    public String getCatId() {
        return catId;
    }
    public void setCatId(String catId) {
        this.catId = catId;
    }

    //orgAtts
    public BaseMongoMap<String, Object> getOrgAtts() {
        return orgAtts;
    }
    public void setOrgAtts(BaseMongoMap<String, Object> orgAtts) {
        this.orgAtts = orgAtts;
    }

    //cnAtts
    public BaseMongoMap<String, Object> getCnAtts() {
        return cnAtts;
    }
    public void setCnAtts(BaseMongoMap<String, Object> cnAtts) {
        this.cnAtts = cnAtts;
    }

    //customIds
    public List<String> getCustomIds() {
        return customIds;
    }
    public void setCustomIds(List<String> customIds) {
        this.customIds = customIds;
    }

    //customIdsCn
    public List<String> getCustomIdsCn() {
        return customIdsCn;
    }
    public void setCustomIdsCn(List<String> customIdsCn) {
        this.customIdsCn = customIdsCn;
    }
}