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
public class CmsBtProductModel_Feed extends BaseMongoMap<String, Object> {

    public BaseMongoMap<String, Object> getOrgAtts() {
        if (!this.containsKey("orgAtts")) {
            this.put("orgAtts", new BaseMongoMap<String, Object>());
        }
        return getAttribute("orgAtts");
    }

    public void setOrgAtts(BaseMongoMap<String, Object> orgAtts) {
        setAttribute("orgAtts", orgAtts);
    }

    public BaseMongoMap<String, Object> getCnAtts() {
        if (!this.containsKey("cnAtts")) {
            this.put("cnAtts", new BaseMongoMap<String, Object>());
        }
        return getAttribute("cnAtts");
    }

    public void setCnAtts(BaseMongoMap<String, Object> cnAtts) {
        setAttribute("cnAtts", cnAtts);
    }

    public List<String> getCustomIds() {
        if (!this.containsKey("customIds")) {
            this.put("customIds", new ArrayList<String>());
        }
        return getAttribute("customIds");
    }

    public void setCustomIds(List<String> customIds) {
        setAttribute("customIds", customIds);
    }
}