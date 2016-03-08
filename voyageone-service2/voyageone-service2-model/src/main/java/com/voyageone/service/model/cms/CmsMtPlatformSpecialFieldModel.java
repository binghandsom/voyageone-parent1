package com.voyageone.service.model.cms;


import com.voyageone.base.dao.mysql.BaseModel;

public class CmsMtPlatformSpecialFieldModel extends BaseModel {

    private Integer cartId;

    private String catId;

    private String fieldId;

    private String type;

    public CmsMtPlatformSpecialFieldModel(){
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
