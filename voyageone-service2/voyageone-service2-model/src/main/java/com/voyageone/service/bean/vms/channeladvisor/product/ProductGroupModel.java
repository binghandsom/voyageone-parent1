package com.voyageone.service.bean.vms.channeladvisor.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductGroupModel extends CABaseModel {

    @JsonProperty("BuyableProducts")
    private List<BuyableProductModel> buyableProducts;

    @JsonProperty("SellerSKU")
    private String sellerSKU;

    @JsonProperty("Fields")
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
