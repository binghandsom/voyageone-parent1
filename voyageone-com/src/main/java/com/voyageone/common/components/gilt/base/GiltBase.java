package com.voyageone.common.components.gilt.base;

import com.taobao.api.ApiException;
import com.taobao.top.schema.Util.StringUtil;
import com.voyageone.common.components.gilt.bean.GiltErrorResult;
import com.voyageone.common.components.gilt.bean.GiltErrorType;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * GiltBase
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0.
 *   查看JmBase com.voyageone.common.components.jumei.base.JmBase
 */
public abstract class GiltBase {

    protected final Log logger = LogFactory.getLog(getClass());

    public static final int C_MAX_API_ERROR = 3;

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

        StringBuilder post_url = new StringBuilder();
        String call_url = api_url_root + api_url;
        post_url.append(call_url);

        if (StringUtils.isNullOrBlank2(app_key))
        throw new IllegalArgumentException("authorization Key不能为空");

        //临时map
        Map<String, String> tempParm=params;

        StringBuilder parm_url = new StringBuilder();
        //拼接URL
        for (String key : tempParm.keySet()) {
            if(!StringUtils.isEmpty(tempParm.get(key))){
                parm_url.append("&"  + key + "=");
                parm_url.append(tempParm.get(key));
            }
        }
        if (parm_url.length() != 0){
            parm_url.delete(0,1);
        }

        String result = HttpUtils.get(post_url.toString(), parm_url.toString(),app_key);

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
        if(reqType.equals("put")){
             result = HttpUtils.put(post_url.toString(), jsonString,app_key);
            System.out.println(result);
        }else if(reqType.equals("patch")){
             result = HttpUtils.patch(post_url.toString(), jsonString,app_key);
        }

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
                    } catch (InterruptedException ignore) {

                    }
                }
            }
        }
        throw new Exception("调用Gilt API错误：");
    }

}
