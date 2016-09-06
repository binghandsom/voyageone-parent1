package com.voyageone.web2.sdk.api.ca.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.ca.enums.ListingStatusEnum;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BuyableProductModel {

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
}
