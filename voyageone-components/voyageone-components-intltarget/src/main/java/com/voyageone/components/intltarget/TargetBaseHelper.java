package com.voyageone.components.intltarget;

import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.intltarget.bean.guest.TargetGuestV3AuthResponse;
import com.voyageone.components.intltarget.service.TargetGuestService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
@Component
@EnableRetry
public class TargetBaseHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HTTP_HEAD_ACCEPT = "application/json";

    private static final String INVENTORY_URL = "/available_to_promise/v2/";

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
    public String callTargetApi(String api_url, Map mapBody, boolean isNeedToken, String httpType) throws Exception {
        if (isNeedToken && StringUtils.isEmpty(api_token)) {
            refreshToken();//校验token
        }
        String result = null;
        if (httpType.equals("post"))
            result = HttpUtils.post(getConfigApiUrl(isInvenTory(api_url) + "api_url") + api_url, MapUtils.isEmpty(mapBody) ? "" : JacksonUtil.bean2Json(mapBody), HTTP_HEAD_ACCEPT, api_token);
        else if (httpType.equals("get"))
            result = HttpUtils.targetGet(getConfigApiUrl(isInvenTory(api_url) + "api_url") + api_url, MapUtils.isEmpty(mapBody) ? "" : JacksonUtil.bean2Json(mapBody), HTTP_HEAD_ACCEPT, api_token);
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
        String result = HttpUtils.post(getConfigApiUrl("api_url") + "/guests/v3/auth?key=" + getConfigApiKey("api_key"), "", HTTP_HEAD_ACCEPT, "");
        Assert.notNull(result);
        TargetGuestV3AuthResponse response = JacksonUtil.json2Bean(result, TargetGuestV3AuthResponse.class);
        Assert.notNull(response);
        //重新赋值
        api_token = response.getAccessToken();
    }

    /**
     * 获取ApiUrl，库存单独url，其他同一个url
     *
     * @param key key
     * @return url
     */
    private String getConfigApiUrl(String key) {
        return getConfigApiKey(key);//
    }

    /**
     * 获取ApiKey，库存单独key，其他同一个key
     *
     * @param key key
     * @return key
     */
    public String getConfigApiKey(String key) {
        return ThirdPartyConfigs.getVal1("018", key);
    }

    /**
     * 是否库存
     *
     * @param api_url 根据url判定
     * @return 库存配置前缀
     */
    public String isInvenTory(String api_url) {
        return api_url.startsWith(INVENTORY_URL) ? "inventory_" : "";
    }
}
