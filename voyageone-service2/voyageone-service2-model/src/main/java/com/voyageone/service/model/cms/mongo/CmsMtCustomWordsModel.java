package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.ArrayList;
import java.util.List;

public class CmsMtCustomWordsModel extends BaseMongoModel {

    private String name = null;
    private String description = null;
    private List<CmsMtCustomWordsModel_Params> params = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CmsMtCustomWordsModel_Params> getParams() {
        return params;
    }

    public void setParams(List<CmsMtCustomWordsModel_Params> params) {
        this.params = params;
    }

}
