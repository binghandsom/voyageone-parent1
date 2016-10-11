package com.voyageone.web2.openapi.oauth2.validator;

import org.apache.oltu.oauth2.common.validators.AbstractValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * ClientIdSecretValidator
 * Created on 2016-08-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public class ClientIdSecretValidator extends AbstractValidator<HttpServletRequest> {

    public ClientIdSecretValidator() {
        this.requiredParams.add("grant_type");
        this.requiredParams.add("client_id");
        this.requiredParams.add("client_secret");
        this.enforceClientAuthentication = true;
    }
}
