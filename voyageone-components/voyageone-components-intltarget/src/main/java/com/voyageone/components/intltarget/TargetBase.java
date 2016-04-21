package com.voyageone.components.intltarget;


import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.ComponentBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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
     *
     * @param api_url        api路径
     * @param mapBody        应用参数
     * @param isUrlAppendKey url是否需要key
     * @return json String
     * @throws Exception
     */
    protected String reqTargetApi(String api_url, Map mapBody, boolean isUrlAppendKey, boolean isNeedToken) throws Exception {
        if (isUrlAppendKey)
            api_url += (api_url.contains("?") ? "&" : "?") + "key=" + ThirdPartyConfigs.getVal1("018", "app_key");
        return targetBaseHelper.callTargetApi(api_url, mapBody, isNeedToken);
    }

    /**
     * 获取api响应包含key
     *
     * @param url     api路径
     * @param reqdata 请求数据
     * @param clazz   返回对象类型
     * @param <E>     返回对象
     * @return response
     */
    protected <E> E getApiResponseWithKey(String url, Object reqdata, Class<E> clazz, boolean isNeedToken) throws Exception {
        String result = reqTargetApi(url, JacksonUtil.jsonToMap(JacksonUtil.bean2Json(reqdata)), true, isNeedToken);
        if(result.toUpperCase().contains("ERROR")){
            logger.warn("URL:"+url+"\nreqData:"+reqdata+"\nclazz:"+clazz+"\nisNeedToken:"+isNeedToken);
            logger.warn("Error Info:"+result);
        }
        return StringUtils.isEmpty(result) ? null : JacksonUtil.json2Bean(result, clazz);
    }

}
