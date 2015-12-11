package com.voyageone.base.dao.mongodb.model;

import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;

/**
 * Created by lewis on 15-12-9.
 */
public class CmsMtCategorySchemaModel extends BaseMongoModel {

    private String catId;
    private String catFullPath;
    private List<Field> fields;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatFullPath() {
        return catFullPath;
    }

    public void setCatFullPath(String catFullPath) {
        this.catFullPath = catFullPath;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
