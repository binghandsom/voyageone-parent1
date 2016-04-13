package com.voyageone.components.intltarget;

import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
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

    /**
     * 调用Target api 失败重试
     *
     * @param api_url api路径
     * @param mapBody api请求body
     * @return 返回jsonstring
     * @throws Exception
     */
    @Retryable
    public String callTargetApi(String api_url, Map mapBody, boolean isNeedToken) throws Exception {
        if (isNeedToken && StringUtils.isEmpty(api_token)) {
            refreshToken();//校验token
        }
        String result = HttpUtils.post(getConfigApiUrl("api_url") + api_url,
                JacksonUtil.bean2Json(mapBody), HTTP_HEAD_ACCEPT, api_token);

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
        String url = getConfigApiUrl("api_url") + "/guests/v3/auth?key=" + getConfigApiUrl("app_key");
        String body = JacksonUtil.bean2Json(new HashMap<String, String>() {{
            put("logonId", getConfigApiUrl("logonId"));
            put("logonPassword", getConfigApiUrl("logonPassword"));
        }});
        String reponseStr  = HttpUtils.post(
                url,
                body,
                "application/json",
                null
                );

        //重新赋值
        api_token = (String)JacksonUtil.jsonToMap(reponseStr).get("accessToken");
    }

    private String getConfigApiUrl(String key) {
        return ThirdPartyConfigs.getVal1("018", key);
    }
}
