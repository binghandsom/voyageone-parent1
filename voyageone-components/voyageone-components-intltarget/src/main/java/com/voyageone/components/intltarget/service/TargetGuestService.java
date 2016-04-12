package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.TargetGuestShippingAddress;
import com.voyageone.components.intltarget.bean.TargetGuestShippingAddressRequest;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@EnableRetry
public class TargetGuestService extends TargetBase{

    private static final String Url="/guests/v3/addresses";

    private static final String TARGETKEY= "0a7ALbLaqKCPOF24CNexpawOMX8AamY3";//ThirdPartyConfigs.getVal1("018", "api_key");

    /**
     * 获取shipping 运输地址
     * @return 简单地址信息
     * @throws Exception
     */
    @Retryable
    public TargetGuestShippingAddress getGuestShippingAddress(TargetGuestShippingAddressRequest request) throws Exception {
        String result=reqGiltApi(Url+"?key="+TARGETKEY,JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request)));
        return StringUtils.isEmpty(result)?null: JacksonUtil.json2Bean(result,TargetGuestShippingAddress.class);
    }

}
