package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;

public class CmsMtCategoryTreeAllModel_Platform {
    private String platformId;
    private String catId;
    private String catPath;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) { this.platformId = platformId; }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatPath() {
        return catPath;
    }

    public void setCatPath(String catPath) {
        this.catPath = catPath;
    }
}
