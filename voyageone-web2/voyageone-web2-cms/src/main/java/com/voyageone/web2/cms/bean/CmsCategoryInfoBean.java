package com.voyageone.web2.cms.bean;

import com.voyageone.cms.service.model.CmsBtProductModel_Field_Image;
import com.voyageone.common.masterdate.schema.field.Field;

import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 16-1-5.
 */
public class CmsCategoryInfoBean {

    private String categoryId;

    private String categoryFullPath;

    private List<Field> masterFields;

    private Field skuFields;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryFullPath() {
        return categoryFullPath;
    }

    public void setCategoryFullPath(String categoryFullPath) {
        this.categoryFullPath = categoryFullPath;
    }

    public List<Field> getMasterFields() {
        return masterFields;
    }

    public void setMasterFields(List<Field> masterFields) {
        this.masterFields = masterFields;
    }

    public Field getSkuFields() {
        return skuFields;
    }

    public void setSkuFields(Field skuFields) {
        this.skuFields = skuFields;
    }
}
