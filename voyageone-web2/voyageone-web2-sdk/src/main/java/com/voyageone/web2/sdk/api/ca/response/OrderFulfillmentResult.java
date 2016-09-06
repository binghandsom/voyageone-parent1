package com.voyageone.web2.sdk.api.ca.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.ca.enums.RequestResultEnum;
import com.voyageone.web2.sdk.api.ca.exception.ErrorModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderFulfillmentResult {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Result")
    private RequestResultEnum result;

    @JsonProperty("Errors")
    private List<ErrorModel> errors;

    @JsonProperty("HasErrors")
    private boolean hasErrors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestResultEnum getResult() {
        return result;
    }

    public void setResult(RequestResultEnum result) {
        this.result = result;
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
