package com.voyageone.web2.openapi.oauth2.interceptors;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.redis.RedisRateLimiterHelper;
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

    @Autowired
    private RedisRateLimiterHelper redisRateLimiterHelper;

    boolean preHandle(HttpServletRequest request) throws Exception {

        // TODO 开发阶段跳过检查
//        if (true) return true;

        //check SellerID
        String sellerID = request.getHeader(SELLER_ID);
        if (StringUtil.isEmpty(sellerID)) {
            //4002 (InvalidSellerID)	Authorization failed. Invalid SellerID
            throw new CAApiException(ErrorIDEnum.InvalidSellerID);
        }

        // 1000 propert
        Long rateNum = redisRateLimiterHelper.aquire(120,
                Integer.parseInt(Properties.readValue("com.voyageone.web2.openapi.channeladvisor.maxrate"))
                , System.currentTimeMillis() / 60000 + "_" +  sellerID);
        System.out.println("获取到令牌号："+rateNum);
        if (rateNum < 0) {
            // rateLimiter
            throw new CAApiException(ErrorIDEnum.RateLimitExceeded);
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
