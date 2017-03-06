package com.voyageone.service.model.cms.mongo.product;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product 的商品Model feed
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Feed  {

    private String catId = "";

    private String catPath = "";

    private String brand = "";

    private BaseMongoMap<String, Object> orgAtts = new BaseMongoMap<>();

    private BaseMongoMap<String, Object> cnAtts = new BaseMongoMap<>();

    private List<String> customIds = new ArrayList<>();

    private List<String> customIdsCn = new ArrayList<>();

    private Map<String, String> attsName = new HashMap<>();

    private List<String> subCategories = new ArrayList<>();

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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<String> subCategories) {
        this.subCategories = subCategories;
    }

    public Map<String, String> getAttsName() {
        return attsName;
    }

    public void setAttsName(Map<String, String> attsName) {
        this.attsName = attsName;
    }
}