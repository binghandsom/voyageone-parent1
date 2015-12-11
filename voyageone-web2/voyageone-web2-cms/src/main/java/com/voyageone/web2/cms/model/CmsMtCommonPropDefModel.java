package com.voyageone.web2.cms.model;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.common.masterdate.schema.field.Field;

/**
 * @author gubuchun 15/12/9
 * @version 2.0.0
 */
public class CmsMtCommonPropDefModel extends BaseMongoModel {

    private Field field;

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
