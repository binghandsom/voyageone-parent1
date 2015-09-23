package com.voyageone.batch.ims.bean;

import com.taobao.top.schema.field.Field;
import com.taobao.top.schema.field.InputField;
import com.voyageone.batch.ims.modelbean.CmsModelPropBean;

import java.util.List;
import java.util.Map;

/**
 * Created by Leo on 15-6-10.
 */
public class UploadItemRunState {
    private CmsModelPropBean cmsModelProp;
    private String productCode;
    private Long categoryCode;
    private List<Field> itemFields;
    private Map<Field, Field> imageUrlFieldMap;

    public Map<Field, Field> getImageUrlFieldMap() {
        return imageUrlFieldMap;
    }

    public void setImageUrlFieldMap(Map<Field, Field> imageUrlFieldMap) {
        this.imageUrlFieldMap = imageUrlFieldMap;
    }

    public List<Field> getItemFields() {
        return itemFields;
    }

    public void setItemFields(List<Field> itemFields) {
        this.itemFields = itemFields;
    }

    public Long getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(Long categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public CmsModelPropBean getCmsModelProp() {
        return cmsModelProp;
    }

    public void setCmsModelProp(CmsModelPropBean cmsModelProp) {
        this.cmsModelProp = cmsModelProp;
    }
}
