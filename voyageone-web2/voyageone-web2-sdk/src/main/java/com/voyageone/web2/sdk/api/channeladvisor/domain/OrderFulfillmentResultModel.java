package com.voyageone.web2.sdk.api.channeladvisor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.enums.RequestResultEnum;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class OrderFulfillmentResultModel extends CABaseModel {

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
        return !CollectionUtils.isEmpty(errors);
    }
}
