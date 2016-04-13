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

    @Autowired
    private TargetBaseHelper targetBaseHelper;

    /**
     * 发送请求Api 公开方法
     * @param api_url api路径
     * @param mapBody 应用参数
     * @param isUrlAppendKey url是否需要key
     * @return json String
     * @throws Exception
     */
    protected String reqTargetApi(String api_url,Map mapBody,boolean isUrlAppendKey,boolean isNeedToken) throws Exception {
        if(isUrlAppendKey) api_url+="?key="+ThirdPartyConfigs.getVal1("018", "app_key");
        return targetBaseHelper.callTargetApi(api_url,mapBody,isNeedToken);
    }

    public static String targetPost(String url, String jsonBody,String accept,String token) throws Exception {
        HttpPost post=new HttpPost(new URI(url));

        // setHeader Accept
        post.setHeader("Accept",StringUtils.isEmpty(accept)?"application/json":accept);
        // setHeader Authorization
        if(!StringUtils.isEmpty(token)) post.setHeader("Authorization","Bearer " + token);

        //setBody
        post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

        //测试启用代理
        post.setConfig(RequestConfig.custom().setProxy(new HttpHost("192.168.1.146",808)).build());

        //post request
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpResponse response = httpclient.execute(post);

        //从服务器获得输入流
        BufferedReader buffer = new BufferedReader(new InputStreamReader(response.getEntity().getContent()),10*1024);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = buffer.readLine()) != null) {
            sb.append(line);
        }

        //关闭流
        buffer.close();
        return sb.toString();
    }

}
