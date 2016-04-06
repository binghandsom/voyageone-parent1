package com.voyageone.common.components.gilt.base;

import com.voyageone.common.components.gilt.bean.GiltErrorResult;
import com.voyageone.common.components.gilt.exceptions.GiltException;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.HttpUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * GiltBase
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0.
 *   查看JmBase com.voyageone.common.components.jumei.base.JmBase
 */
public abstract class GiltBase {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

//    public static final int C_MAX_API_ERROR = 3;

    public static final int TRY_WAIT_TIME = 5000;

    /**
     * 发送请求Api
     * @param api_url api路径
     * @param params 应用参数
     * @return Json String
     * @throws Exception
     */
    protected String reqGiltApi(String api_url,Map<String, String> params) throws Exception {
        String api_url_root = ThirdPartyConfigs.getVal1(ChannelConfigEnums.Channel.GILT.getId(), "api_url");
        String app_key = ThirdPartyConfigs.getVal1(ChannelConfigEnums.Channel.GILT.getId(), "app_key");

        String call_url = api_url_root + api_url;

        if (StringUtils.isNullOrBlank2(app_key)) {
            throw new IllegalArgumentException("authorization Key不能为空");
        }

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

        String result = HttpUtils.get(call_url, parm_url.toString(), app_key);

        /* 如果包含message  表示错误*/
        if(StringUtils.isNullOrBlank2(result)||result.contains("message")){
            //转换错误信息
            GiltErrorResult res = JacksonUtil.json2Bean(result, GiltErrorResult.class);
            if (res.getType() != null){
                throw new Exception("调用Gilt API错误：" + result);
            }
        }
        return result;
    }

    /**
     * 发送请求Api
     * @param api_url api路径
     * @return Json String
     * @throws Exception
     */
    protected String reqPutGiltApi(String api_url,String jsonString) throws Exception {
        return reqGiltApi(api_url,jsonString,"put");
    }

    /**
     * 发送请求Api
     * @param api_url api路径
     * @return Json String
     * @throws Exception
     */
    protected String reqPatchGiltApi(String api_url,String jsonString) throws Exception {
        return reqGiltApi(api_url,jsonString,"patch");
    }

    /**
     *  put Or patch req
     * @param api_url api_url
     * @param jsonString jsonString
     * @param reqType reqType
     * @return String
     * @throws Exception
     */
    private String reqGiltApi(String api_url,String jsonString,String reqType) throws Exception {
        String api_url_root = ThirdPartyConfigs.getVal1(ChannelConfigEnums.Channel.GILT.getId(), "api_url");
        String app_key = ThirdPartyConfigs.getVal1(ChannelConfigEnums.Channel.GILT.getId(), "app_key");

        StringBuilder post_url = new StringBuilder();
        String call_url = api_url_root + api_url;
        post_url.append(call_url);

        if (StringUtils.isNullOrBlank2(app_key))
            throw new IllegalArgumentException("authorization Key不能为空");

        String result=null;
        if("put".equals(reqType)){
             result = HttpUtils.put(post_url.toString(), jsonString, app_key);
          //  System.out.println(result);
        }else if("patch".equals(reqType)){
             result = HttpUtils.patch(post_url.toString(), jsonString,app_key);
        }

        /* 如果包含message  表示错误*/
        if(StringUtils.isNullOrBlank2(result) || result.contains("message")){
            //转换错误信息
            List<GiltErrorResult> res = JacksonUtil.jsonToBeanList(result, GiltErrorResult.class);
            if (!res.isEmpty()){
                throw new GiltException(result);
            }
        }
        return result;
    }

    /**
     * 重试发送请求Api
     * @param maxRetry 重试次数
     * @param api_url api路径
     * @param params 应用参数
     * @return Json String
     * @throws Exception
     */
    protected String reqGiltApi(String api_url,int maxRetry,Map<String, String> params) throws Exception {
        for (int intApiErrorCount = 0; intApiErrorCount < maxRetry; intApiErrorCount++) {
            try {
                return reqGiltApi(api_url,params);
            } catch (Exception e) {
                if (maxRetry - intApiErrorCount == 1)
                    throw e;
                else{
                    try {
                        Thread.sleep(TRY_WAIT_TIME);
                    } catch (Exception ignore) {
                    }
                }
            }
        }
        throw new Exception("调用Gilt API错误：");
    }

}
