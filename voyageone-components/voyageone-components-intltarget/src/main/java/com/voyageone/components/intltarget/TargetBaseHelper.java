package com.voyageone.components.intltarget;

import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.intltarget.bean.guest.TargetGuestV3AuthResponse;
import com.voyageone.components.intltarget.service.TargetGuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
@EnableRetry
public class TargetBaseHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final static String HTTP_HEAD_ACCEPT = "application/json";

    private static String api_token;

    @Autowired
    private TargetGuestService targetGuestService;

    /**
     * 调用Target api 失败重试
     *
     * @param api_url api路径
     * @param mapBody api请求body
     * @return 返回jsonstring
     * @throws Exception
     */
    @Retryable
    public String callTargetApi(String api_url, Map mapBody, boolean isNeedToken,String httpType) throws Exception {
        if (isNeedToken && StringUtils.isEmpty(api_token)) {
            refreshToken();//校验token
        }
        String result=null;
        if(httpType.equals("post"))
            result = HttpUtils.post(getConfigApiUrl("api_url") + api_url,JacksonUtil.bean2Json(mapBody), HTTP_HEAD_ACCEPT, api_token);
        else if(httpType.equals("get"))
            result = HttpUtils.targetGet(getConfigApiUrl("api_url") + api_url,JacksonUtil.bean2Json(mapBody), HTTP_HEAD_ACCEPT, api_token);
        //检查结果
        checkResult(result, isNeedToken);
        return result;
    }

    /**
     * 检查返回结果是否合法
     *
     * @param result      结果jsonString
     * @param isNeedToken 是否需要token
     * @throws Exception
     */
    private void checkResult(String result, boolean isNeedToken) throws Exception {
        //需要token 包含Error
        if (isNeedToken && result.contains("Error")) {
            logger.warn("checkResult:=" + result);
            /*第一次获取到的token Forbidden*/
            if (result.contains("Forbidden") && result.contains("SCOPE_1_ACCESS_LEVEL_REQUIRED")) {
                refreshToken();
                throw new RuntimeException("刷新token");
            /*其他无效token*/
            } else if (result.contains("Unauthorized") && result.contains("Invalid access token")) {
                refreshToken();
                throw new RuntimeException("刷新token");
            }
        }
    }

    /**
     * 刷新token
     *
     * @throws Exception
     */
    private void refreshToken() throws Exception {
        //重新赋值
        api_token = targetGuestService.v3Auth().getAccessToken();
    }

    private String getConfigApiUrl(String key) {
        if(key.equals("app_key"))
        return "wrQCAYQjwG6t9konlHC9ManaPPhgHHfS";
        else
        return "https://stage-api.target.com";//ThirdPartyConfigs.getVal1("018", key);
    }
}
