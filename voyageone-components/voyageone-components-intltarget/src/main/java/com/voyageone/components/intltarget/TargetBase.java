package com.voyageone.components.intltarget;


import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.ComponentBase;
import org.apache.http.client.utils.HttpClientUtils;

import java.util.Map;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetBase extends ComponentBase {

    public static final String TARGETHOST="https://secure-api.target.com";

    /**
     * 发送请求Api
     * @param api_url api路径
     * @param params 应用参数
     * @return Json String
     * @throws Exception
     */
    protected String reqGiltApi(String api_url,Map<String, String> params) throws Exception {
        //String api_url_root = ThirdPartyConfigs.getVal1(ChannelConfigEnums.Channel.GILT.getId(), "api_url");
        String api_url_root=TARGETHOST;
        String app_key = "";

        String call_url = TARGETHOST + api_url;

        StringBuilder parm_url = new StringBuilder();
        //拼接URL
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if(!StringUtils.isEmpty(entry.getKey())){
                parm_url.append("&").append(entry.getKey()).append("=");
                parm_url.append(entry.getValue());
            }
        }
        if (parm_url.length() != 0){
            parm_url.delete(0,1);
        }

        String result = HttpUtils.targetGet(call_url, parm_url.toString(), "application/json");

        return result;
    }

}
