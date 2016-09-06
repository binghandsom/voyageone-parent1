package com.voyageone.web2.sdk.api.channeladvisor.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.enums.RequestResultEnum;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class BuyableProductResult {

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
}
