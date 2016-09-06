package com.voyageone.web2.sdk.api.ca.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.web2.sdk.api.ca.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.ca.exception.ErrorModel;

import java.util.List;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
public class ActionResponse {

    public static void main(String[] args) {
        ActionResponse response=new ActionResponse();
        response.setHasErrors(true);
        response.setPendingUri("Http://xxx.com/api");
        response.setStatus(ResponseStatusEnum.Complete);

        System.out.println(JacksonUtil.bean2Json(response));


    }

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
}
