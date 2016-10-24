package com.voyageone.service.model.cms.mongo.jm.promotion;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/10/19.
 */
public class CmsMtJmConfigModel  extends BaseMongoModel {
    private Integer type;
    private String  name;
    private List<Map<String,Object>> values;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getValues() {
        return values;
    }

    public void setValues(List<Map<String, Object>> values) {
        this.values = values;
    }
}
