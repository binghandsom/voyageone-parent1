package com.voyageone.service.bean.vms.channeladvisor.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.ListingStatusEnum;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BuyableProductModel extends CABaseModel {

    @JsonProperty("ListingStatus")
    private ListingStatusEnum listingStatus;

    @JsonProperty("SellerSKU")
    private String sellerSKU;

    @JsonProperty("Quantity")
    private Integer quantity;

    @JsonProperty("Fields")
    private List<FieldModel> fields;

    public ListingStatusEnum getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(ListingStatusEnum listingStatus) {
        this.listingStatus = listingStatus;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<FieldModel> getFields() {
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }

    @JsonIgnore
    public FieldModel price(){
        for(FieldModel fieldModel:this.fields){
            if("price".equalsIgnoreCase(fieldModel.getName())){
                return fieldModel;
            }
        }
        return null;
    }
}
