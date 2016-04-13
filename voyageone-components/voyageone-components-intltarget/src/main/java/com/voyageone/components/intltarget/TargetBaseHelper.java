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

    private static String api_token;

    /**
     * 调用Target api 失败重试
     * @param api_url api路径
     * @param mapBody api请求body
     * @return 返回jsonstring
     * @throws Exception
     */
    @Retryable
    public String callTargetApi(String api_url,Map mapBody,boolean isNeedToken) throws Exception {
        if(isNeedToken&&StringUtils.isEmpty(api_token)) refreshToken();//校验token
        String result = HttpUtils.post(ThirdPartyConfigs.getVal1("018", "api_url") + api_url, JacksonUtil.bean2Json(mapBody), "application/json", api_token);
        checkResult(result,isNeedToken); //检查结果
        return result;
    }

    /**
     * 检查返回结果是否合法
     * @param result 结果jsonString
     * @param isNeedToken 是否需要token
     * @throws Exception
     */
    private void checkResult(String result,boolean isNeedToken) throws Exception {
        if(     /*需要token*/isNeedToken
                &&/*包含Error，尝试进行异常解析*/result.contains("Error")
                &&(/*第一次获取到的token*/(result.contains("Forbidden")
                &&result.contains("SCOPE_1_ACCESS_LEVEL_REQUIRED"))
                ||/*其他无效token*/(result.contains("Unauthorized")
                &&result.contains("Invalid access token")))) refreshToken();
    }

    /**
     * 刷新token
     * @throws Exception
     */
    private void refreshToken() throws Exception {
        //重新赋值
        api_token = (String) JacksonUtil
                .jsonToMap(
                        HttpUtils.post(
                                ThirdPartyConfigs.getVal1("018", "api_url") + "/guests/v3/auth?key=" + ThirdPartyConfigs.getVal1("018", "app_key"),
                                JacksonUtil.bean2Json(new HashMap<String, String>() {{
                                    put("logonId", ThirdPartyConfigs.getVal1("018", "logonId"));
                                    put("logonPassword", ThirdPartyConfigs.getVal1("018", "logonPassword"));
                                }}),
                                "application/json",
                                null
                        )
                ).get("accessToken");
        throw new RuntimeException("刷新token");
    }
}
