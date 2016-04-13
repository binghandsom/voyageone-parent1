package com.voyageone.components.intltarget;


import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.components.ComponentBase;
import org.springframework.beans.factory.annotation.Autowired;

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

}
