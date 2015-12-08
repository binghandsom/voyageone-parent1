package com.voyageone.cms.service.model;


import com.voyageone.base.dao.mongodb.model.BaseMongoMap;

import java.util.List;
import java.util.Map;

public class CmsBtProductModel_Field_Image extends BaseMongoMap {

    public CmsBtProductModel_Field_Image() {
    }

    public CmsBtProductModel_Field_Image(String name, String type) {
        setAttribute("name", name);
        setAttribute("type", type);
    }

    public CmsBtProductModel_Field_Image(Map map) {
        this.putAll(map);
    }

    public String getName() {
        return (String) getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getType() {
        return (String) getAttribute("type");
    }

    public void setType(String type) {
        setAttribute("type", type);
    }

}