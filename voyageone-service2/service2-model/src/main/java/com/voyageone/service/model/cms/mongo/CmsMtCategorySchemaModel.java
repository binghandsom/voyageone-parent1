package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;

/**
 * Created by lewis on 15-12-9.
 */
public class CmsMtCategorySchemaModel extends BaseMongoModel {

    private String catId;
    private String catFullPath;
    private List<Field> fields;
    private Field sku;

    public CmsMtCategorySchemaModel() {
    }

    public CmsMtCategorySchemaModel(String catId, String catFullPath, List<Field> fields) {
        this.catId = catId;
        this.catFullPath = catFullPath;
        this.fields = fields;
    }

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

    public Field getSku() {
        return sku;
    }

    public void setSku(Field sku) {
        this.sku = sku;
    }
}
