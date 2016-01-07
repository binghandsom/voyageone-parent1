package com.voyageone.cms.service.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;

/**
 * Created by lewis on 16-1-6.
 */
public class CmsMtComSchemaModel extends BaseMongoModel{

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
