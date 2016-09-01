package com.voyageone.web2.openapi.oauth2.request;

import com.voyageone.web2.openapi.oauth2.validator.ClientIdSecretValidator;
import org.apache.oltu.oauth2.as.request.AbstractOAuthTokenRequest;
import org.apache.oltu.oauth2.as.validator.AuthorizationCodeValidator;
import org.apache.oltu.oauth2.as.validator.ClientCredentialValidator;
import org.apache.oltu.oauth2.as.validator.RefreshTokenValidator;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.validators.OAuthValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * OAuthTokenClientIdSecretRequest
 * Created on 2016-08-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public class OAuthTokenClientIdSecretRequest extends AbstractOAuthTokenRequest {
    public OAuthTokenClientIdSecretRequest(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }

    protected OAuthValidator<HttpServletRequest> initValidator() throws OAuthProblemException, OAuthSystemException {
        //this.validators.put(GrantType.PASSWORD.toString(), PasswordValidator.class);
        this.validators.put(GrantType.PASSWORD.toString(), ClientIdSecretValidator.class);
        this.validators.put(GrantType.CLIENT_CREDENTIALS.toString(), ClientCredentialValidator.class);
        this.validators.put(GrantType.AUTHORIZATION_CODE.toString(), AuthorizationCodeValidator.class);
        this.validators.put(GrantType.REFRESH_TOKEN.toString(), RefreshTokenValidator.class);
        return super.initValidator();
    }
}
