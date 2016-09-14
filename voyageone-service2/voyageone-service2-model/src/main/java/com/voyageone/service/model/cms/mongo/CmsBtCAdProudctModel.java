package com.voyageone.service.model.cms.mongo;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;
import com.voyageone.service.bean.vms.channeladvisor.product.BuyableProductModel;
import com.voyageone.service.bean.vms.channeladvisor.product.FieldModel;

import java.util.List;

/**
 * @author james.li on 2016/9/12.
 * @version 2.0.0
 */
public class CmsBtCAdProudctModel extends BaseMongoModel {

    private List<BuyableProductModel> buyableProducts;

    private String sellerSKU;

    private List<FieldModel> fields;

    public List<BuyableProductModel> getBuyableProducts() {
        return buyableProducts;
    }

    public void setBuyableProducts(List<BuyableProductModel> buyableProducts) {
        this.buyableProducts = buyableProducts;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }

}
