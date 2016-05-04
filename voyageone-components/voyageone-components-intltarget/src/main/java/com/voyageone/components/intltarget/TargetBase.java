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
    protected String reqTargetApi(String api_url, Map mapBody, boolean isUrlAppendKey, boolean isNeedToken, String httpType) throws Exception {
        if (isUrlAppendKey)
            api_url += (api_url.contains("?") ? "&" : "?") + "key=" + targetBaseHelper.getConfigApiKey(targetBaseHelper.isInvenTory(api_url) + "api_key");
        return targetBaseHelper.callTargetApi(api_url, mapBody, isNeedToken, httpType);
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
        return convertResult(url, JacksonUtil.jsonToMap(JacksonUtil.bean2Json(reqdata)), clazz, true, isNeedToken, "get");
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
    protected <E> E postApiResponseWithKey(String url, Object reqdata, Class<E> clazz, boolean isNeedToken) throws Exception {
        return convertResult(url, JacksonUtil.jsonToMap(JacksonUtil.bean2Json(reqdata)), clazz, true, isNeedToken, "post");
    }

    /**
     * 转化结果为Bean
     *
     * @param api_url        目标url
     * @param mapBody        数据体
     * @param clazz          类
     * @param isUrlAppendKey 是否需要key
     * @param isNeedToken    是否需要token
     * @param httpType       http请求类型
     * @param <E>            响应对象
     * @return 对象
     * @throws Exception
     */
    private <E> E convertResult(String api_url, Map mapBody, Class<E> clazz, boolean isUrlAppendKey, boolean isNeedToken, String httpType) throws Exception {
        String result = reqTargetApi(api_url, mapBody, isUrlAppendKey, isNeedToken, httpType);
        if (result.toUpperCase().contains("ERROR")) {
            logger.warn("URL:" + api_url + "\nreqData:" + mapBody + "\nclazz:" + clazz + "\nisNeedToken:" + isNeedToken);
            logger.warn("Error Info:" + result);
        }
        return StringUtils.isEmpty(result) ? null : JacksonUtil.json2Bean(result, clazz);
    }

}
