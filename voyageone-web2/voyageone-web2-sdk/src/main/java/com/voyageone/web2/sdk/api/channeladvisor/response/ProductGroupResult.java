package com.voyageone.web2.sdk.api.channeladvisor.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductGroupResult {

    @JsonProperty("BuyableProductResults")
    private List<BuyableProductResult> BuyableProductResults;

    @JsonProperty("SellerSKU")
    private String sellerSKU;

    @JsonProperty("Errors")
    private List<ErrorModel> errors;

    @JsonProperty("HasErrors")
    private boolean hasErrors;

    public List<BuyableProductResult> getBuyableProductResults() {
        return BuyableProductResults;
    }

    public void setBuyableProductResults(List<BuyableProductResult> buyableProductResults) {
        BuyableProductResults = buyableProductResults;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
