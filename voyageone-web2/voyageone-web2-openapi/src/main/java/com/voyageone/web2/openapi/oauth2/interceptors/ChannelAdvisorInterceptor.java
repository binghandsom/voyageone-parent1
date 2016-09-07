package com.voyageone.web2.openapi.oauth2.interceptors;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.oauth2.service.OAuthService;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ErrorIDEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 对 ChannelAdvisor API 进行认证
 *
 * @author chuanyu.liang
 * @version 2.0.0, 16/09/06
 */
@Component
class ChannelAdvisorInterceptor {


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
            throw new CAApiException(ErrorIDEnum.InvalidSellerID);
        }

        //check SellerToken
        String sellerToken = request.getHeader(SELLER_TOKEN);
        if (StringUtil.isEmpty(sellerToken)) {
            //4001 (InvalidToken)	Authorization failed. Invalid SellerToken
            throw new CAApiException(ErrorIDEnum.InvalidToken);
        }

        // 检查客户端客户端id, 安全KEY是否正确
        if (oAuthService.getClientSecretAndSetCurrentThread(sellerID, sellerToken) == null) {
            //4001 (InvalidToken)	Authorization failed. Invalid SellerToken
            throw new CAApiException(ErrorIDEnum.InvalidToken);
        }

        return true;
    }
}
