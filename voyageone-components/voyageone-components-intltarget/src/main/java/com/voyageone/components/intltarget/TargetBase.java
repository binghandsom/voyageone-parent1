package com.voyageone.components.intltarget;


import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ComponentBase;
import com.voyageone.components.intltarget.error.TargetErrorResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TargetBase extends ComponentBase {

    /* host */
    private static final String TARGETHOST="https://secure-api.target.com";//ThirdPartyConfigs.getVal1("018", "api_url");

    /* auth token */
    private static final String authTokenUrl=TARGETHOST+"/guests/v3/auth?key=0a7ALbLaqKCPOF24CNexpawOMX8AamY3";//+ThirdPartyConfigs.getVal1("018", "api_key");
    private static final Map<String,String> authTokenMap=new HashMap<String,String>(){{
        put("logonId","bingbing.gao@voyageone.cn");
        put("logonPassword","voyageone1");
        /*put("logonId",ThirdPartyConfigs.getVal1("018", "logonId"));
        put("logonPassword",ThirdPartyConfigs.getVal1("018", "logonPassword"));*/
    }};

    private static String api_token;

    /**
     * 发送请求Api
     * @param api_url api路径
     * @param mapBody 应用参数
     * @return Json String
     * @throws Exception
     */
    protected String reqGiltApi(String api_url,Map mapBody) throws Exception {
        //校验token
        if(StringUtils.isEmpty(api_token)) refreshToken();
        String call_url = TARGETHOST + api_url;
        String result = HttpUtils.targetPost(call_url, JacksonUtil.bean2Json(mapBody), "application/json",api_token);
        if(result.contains("Error")){//包含Error，尝试进行异常解析
            if(//第一次获取到的token
                    (result.contains("Forbidden")&&result.contains("SCOPE_1_ACCESS_LEVEL_REQUIRED"))
                    //其他token
                    ||(result.contains("Unauthorized")&&result.contains("Invalid access token"))
                    ) refreshToken();
        }
        return result;
    }


    private static void refreshToken() throws Exception {
        //重新赋值
        api_token = (String) JacksonUtil
                .jsonToMap(
                        HttpUtils.targetPost(authTokenUrl, JacksonUtil.bean2Json(authTokenMap), "application/json",null)
                ).get("accessToken");
        throw new RuntimeException("刷新token");
    }

}
