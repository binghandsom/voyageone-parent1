package com.voyageone.service.bean.vms.channeladvisor.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.service.bean.vms.channeladvisor.CABaseModel;
import com.voyageone.service.bean.vms.channeladvisor.ErrorModel;
import com.voyageone.service.bean.vms.channeladvisor.enums.RequestResultEnum;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BuyableProductResultModel extends CABaseModel {

    @JsonProperty("RequestResult")
    private RequestResultEnum requestResult;

    @JsonProperty("SellerSKU")
    private String sellerSKU;

    @JsonProperty("Errors")
    private List<ErrorModel> errors;

    @JsonProperty("HasErrors")
    private boolean hasErrors;

    @JsonProperty("MarketPlaceItemID")
    private String marketPlaceItemID;

    @JsonProperty("URL")
    private String url;

    public RequestResultEnum getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(RequestResultEnum requestResult) {
        this.requestResult = requestResult;
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

    public boolean isHasErrors() {
        return !CollectionUtils.isEmpty(errors);
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    @JsonIgnore
    public void addError(ErrorModel error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public String getMarketPlaceItemID() {
        return marketPlaceItemID;
    }

    public void setMarketPlaceItemID(String marketPlaceItemID) {
        this.marketPlaceItemID = marketPlaceItemID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    @JsonIgnore
    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(errors);
    }
}
