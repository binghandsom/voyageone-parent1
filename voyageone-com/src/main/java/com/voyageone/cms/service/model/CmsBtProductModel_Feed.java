package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.ArrayList;
import java.util.List;

/**
 * 的商品Model feed
 * @author chuanyu.liang, 12/11/15
 * @version 2.0.0
 * @since 2.0.0
 */
public class CmsBtProductModel_Feed  {

    private BaseMongoMap<String, Object> orgAtts = new BaseMongoMap<String, Object>();

    private BaseMongoMap<String, Object> cnAtts = new BaseMongoMap<String, Object>();

    private List<String> customIds = new ArrayList<String>();

    public BaseMongoMap<String, Object> getOrgAtts() {
        return orgAtts;
    }

    public void setOrgAtts(BaseMongoMap<String, Object> orgAtts) {
        this.orgAtts = orgAtts;
    }

    public BaseMongoMap<String, Object> getCnAtts() {
        return cnAtts;
    }

    public void setCnAtts(BaseMongoMap<String, Object> cnAtts) {
        this.cnAtts = cnAtts;
    }

    public List<String> getCustomIds() {
        return customIds;
    }

    public void setCustomIds(List<String> customIds) {
        this.customIds = customIds;
    }
}