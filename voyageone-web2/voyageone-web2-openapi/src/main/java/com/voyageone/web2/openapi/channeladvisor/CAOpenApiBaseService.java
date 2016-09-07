package com.voyageone.web2.openapi.channeladvisor;

import com.voyageone.web2.openapi.OpenApiBaseService;
import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.CABaseModel;
import com.voyageone.web2.sdk.api.channeladvisor.domain.EmptyObject;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Rest webservice Service 层提供基类
 * Created by chuanyu.liang on 15/6/26.
 * @author chuanyu.liang
 */
public abstract class CAOpenApiBaseService extends OpenApiBaseService {

    @Autowired
    private OAuthService oAuthService;

    protected String getClientChannelId() {
        if (oAuthService.getCurrentThreadClientModel() != null) {
            return oAuthService.getCurrentThreadClientModel().getChannelId();
        }
        return null;
    }

    protected String getClientUserId() {
        if (oAuthService.getCurrentThreadClientModel() != null) {
            return oAuthService.getCurrentThreadClientModel().getUserAccount();
        }
        return null;
    }

    protected ActionResponse success(CABaseModel responseBody) {
        ActionResponse response = new ActionResponse();
        response.setResponseBody(responseBody);
        if (responseBody.hasErrors()) {
            response.setStatus(ResponseStatusEnum.CompleteWithErrors);
        } else {
            response.setStatus(ResponseStatusEnum.Complete);
        }

        response.setPendingUri(null);
        response.setHasErrors(responseBody.hasErrors());
        response.setErrors(new ArrayList<>());
        return response;
    }

    protected ActionResponse success(List responseBody) {
        ActionResponse response = new ActionResponse();
        response.setResponseBody(responseBody);
        boolean hasErrors = false;
        for (Object obj : responseBody) {
            CABaseModel model = (CABaseModel)obj;
            if (model.hasErrors()) {
                hasErrors = true;
                break;
            }
        }
        if (hasErrors) {
            response.setStatus(ResponseStatusEnum.CompleteWithErrors);
        } else {
            response.setStatus(ResponseStatusEnum.Complete);
        }

        response.setPendingUri(null);
        response.setHasErrors(hasErrors);
        response.setErrors(new ArrayList<>());
        return response;
    }

    protected ActionResponse success() {
        ActionResponse response = new ActionResponse();
        response.setResponseBody(new EmptyObject());
        response.setStatus(ResponseStatusEnum.Complete);
        response.setPendingUri(null);
        response.setHasErrors(false);
        response.setErrors(new ArrayList<>());
        return response;
    }

}
