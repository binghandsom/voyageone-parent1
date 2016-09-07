package com.voyageone.web2.sdk.api.channeladvisor.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.exception.ErrorModel;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ActionResponse {

    @JsonProperty("Status")
    private ResponseStatusEnum status;

    @JsonProperty("PendingUri")
    private String pendingUri;

    @JsonProperty("Errors")
    private List<ErrorModel> errors;

    @JsonProperty("HasErrors")
    private boolean hasErrors;

    @JsonProperty("ResponseBody")
    private Object responseBody;

    public ResponseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ResponseStatusEnum status) {
        this.status = status;
    }

    public String getPendingUri() {
        return pendingUri;
    }

    public void setPendingUri(String pendingUri) {
        this.pendingUri = pendingUri;
    }

    public List<ErrorModel> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorModel> errors) {
        this.errors = errors;
    }

    public void addError(ErrorModel error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public static ActionResponse createEmpty(boolean isHasErrors) {
        ActionResponse result = new ActionResponse();

        result.setResponseBody(new HashMap());
        result.setStatus(ResponseStatusEnum.Complete);
        result.setPendingUri(null);
        result.setErrors(new ArrayList<>());
        result.setHasErrors(isHasErrors);

        return result;
    }
}
