package com.voyageone.web2.openapi.oauth2.interceptors;

import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 对access_token进行认证
 *
 * @author chuanyu.liang
 * @version 2.0.0, 16/8/14
 */
@Component
class AccessTokenInterceptor {

    @Autowired
    private OAuthService oAuthService;

    boolean preHandle(HttpServletRequest request) throws Exception {

//        // TODO 开发阶段跳过检查
//        if (true) return true;

        OAuthAccessResourceRequest oauthRequest;

        //构建OAuth资源请求
        try {
            oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
        } catch (Exception ex) {
            // 如果不存在/过期了，返回未验证错误，需重新验证
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70099;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //获取Access Token
        String accessToken = oauthRequest.getAccessToken();

        //验证Access Token
        if (oAuthService.checkAccessToken(accessToken)) {
            // 如果不存在/过期了，返回未验证错误，需重新验证
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70099;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        return true;
    }
}
