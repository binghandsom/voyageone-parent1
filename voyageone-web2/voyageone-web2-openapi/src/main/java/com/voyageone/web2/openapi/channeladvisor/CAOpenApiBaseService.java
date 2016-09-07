package com.voyageone.web2.openapi.channeladvisor;

import com.voyageone.web2.openapi.OpenApiBaseService;
import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;


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

}
