package com.voyageone.web2.openapi.oauth2.control;

import com.voyageone.web2.openapi.oauth2.OAuth2Constants;
import com.voyageone.web2.openapi.oauth2.request.OAuthTokenClientIdSecretRequest;
import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.AbstractOAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

/**
 * AccessTokenController
 * Created on 2016-08-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */

@RestController
@RequestMapping(
        value = "/rest",
        method = RequestMethod.POST
)
public class AccessTokenController {

    @Autowired
    private OAuthService oAuthService;

    @RequestMapping("/accessToken")
    public ResponseEntity<String> token(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth请求
            AbstractOAuthTokenRequest oauthRequest = new OAuthTokenClientIdSecretRequest(request);

            GrantType grantType = getGrantType(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE));
            OAuthResponse response = null;
            switch (grantType) {
                case PASSWORD:
                    // 检查客户端客户端id, 安全KEY是否正确
                    if (!oAuthService.checkClientSecret(oauthRequest.getClientId(), oauthRequest.getClientSecret())) {
                        response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                                        .setErrorDescription(OAuth2Constants.INVALID_CLIENT_DESCRIPTION)
                                        .buildJSONMessage();
                    }
                    break;
                case REFRESH_TOKEN:
                    // 检查客户端客户端id, RefreshToken是否正确
                    if (!oAuthService.checkRefreshToken(oauthRequest.getClientId(), oauthRequest.getRefreshToken())) {
                        response = OAuthASResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                                        .setErrorDescription(OAuth2Constants.INVALID_CLIENT_DESCRIPTION)
                                        .buildJSONMessage();
                    }
                    break;
                default:
                    response = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("grant_type Support password or refresh_token.")
                            .buildJSONMessage();
            }

            if (response == null) {
                //生成Access Token
                OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                String accessToken = oAuthIssuer.accessToken();
                oAuthService.addAccessToken(accessToken, oauthRequest.getClientId());

                //生成OAuth响应
                response = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK)
                        .setAccessToken(accessToken)
                        .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                        .buildJSONMessage();
            }

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            e.printStackTrace();
            //构建错误响应
            OAuthResponse res = OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            return new ResponseEntity<>(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
        }
    }

    private GrantType getGrantType(String grantTypeStr) {
        GrantType[] grantTypes = GrantType.values();
        for (GrantType grantType : grantTypes) {
            if (grantType.toString().equals(grantTypeStr)) {
                return grantType;
            }
        }
        return GrantType.JWT_BEARER;
    }

}
