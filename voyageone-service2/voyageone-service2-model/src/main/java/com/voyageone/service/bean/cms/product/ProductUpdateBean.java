package com.voyageone.service.bean.cms.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;

/**
 * Created by DELL on 2016/3/14.
 */
public class ProductUpdateBean {

    private CmsBtProductModel productModel = null;

    private Boolean isCheckModifed = true;

    public CmsBtProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(CmsBtProductModel productModel) {
        this.productModel = productModel;
    }

    public Boolean getIsCheckModifed() {
        return isCheckModifed;
    }

    public void setIsCheckModifed(Boolean isCheckModifed) {
        this.isCheckModifed = isCheckModifed;
    }

    /**
     *modifier
     */
    protected String modifier;
    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * modified
     */
    protected String modified;
    public String getModified() {
        return modified;
    }
    public void setModified(String modified) {
        this.modified = modified;
    }
}
