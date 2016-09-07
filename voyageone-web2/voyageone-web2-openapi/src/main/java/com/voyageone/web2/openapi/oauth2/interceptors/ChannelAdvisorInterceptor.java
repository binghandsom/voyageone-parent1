package com.voyageone.web2.openapi.oauth2.interceptors;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.web2.openapi.oauth2.OAuth2Constants;
import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对 ChannelAdvisor API 进行认证
 *
 * @author chuanyu.liang
 * @version 2.0.0, 16/09/06
 */
@Component
public class ChannelAdvisorInterceptor {

    private static final String SELLER_ID = "SellerID";
    private static final String SELLER_TOKEN = "SellerToken";

    @Autowired
    private OAuthService oAuthService;

    boolean preHandle(HttpServletRequest request) throws Exception {

        // TODO 开发阶段跳过检查
        if (true) return true;

        //check SellerID
        String sellerID = request.getHeader(SELLER_ID);
        if (StringUtil.isEmpty(sellerID)) {
            //4002 (InvalidSellerID)	Authorization failed. Invalid SellerID
        }

        //check SellerToken
        String sellerToken = request.getHeader(SELLER_TOKEN);
        if (StringUtil.isEmpty(sellerToken)) {
            //4001 (InvalidToken)	Authorization failed. Invalid SellerToken
        }

        // 检查客户端客户端id, 安全KEY是否正确
        if (!oAuthService.checkClientSecret(sellerID, sellerToken)) {
            //4001 (InvalidToken)	Authorization failed. Invalid SellerToken
        }

        return true;
    }
}
