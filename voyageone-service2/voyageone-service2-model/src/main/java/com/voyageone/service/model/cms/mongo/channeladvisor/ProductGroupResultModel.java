package com.voyageone.service.model.cms.mongo.channeladvisor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ProductGroupResultModel extends CABaseModel {

    @JsonProperty("BuyableProductResults")
    private List<BuyableProductResultModel> buyableProductResults;

    @JsonProperty("SellerSKU")
    private String sellerSKU;

    @JsonProperty("Errors")
    private List<ErrorModel> errors;

    @JsonProperty("HasErrors")
    private boolean hasErrors;

    public List<BuyableProductResultModel> getBuyableProductResults() {
        return buyableProductResults;
    }

    public void setBuyableProductResults(List<BuyableProductResultModel> buyableProductResults) {
        this.buyableProductResults = buyableProductResults;
    }

    public String getSellerSKU() {
        return sellerSKU;
    }

    public void setSellerSKU(String sellerSKU) {
        this.sellerSKU = sellerSKU;
    }

    public List<ErrorModel> getErrors() {
        if(errors == null) errors = new ArrayList<>();
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }

    @JsonIgnore
    public void addError(ErrorModel error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public boolean isHasErrors() {
        return !CollectionUtils.isEmpty(errors);
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    @Override
    @JsonIgnore
    public boolean hasErrors() {
        boolean haveError = !CollectionUtils.isEmpty(errors);
        if (!haveError) {
            if (buyableProductResults != null && !buyableProductResults.isEmpty()) {
                for (BuyableProductResultModel model : buyableProductResults) {
                    if (model != null && model.hasErrors()) {
                        haveError = true;
                        break;
                    }
                }
            }
        }
        return haveError;
    }
}
