package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.guest.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author aooer 2016/4/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetGuestService extends TargetBase{

    private static final String Url="/guests";

    /**
     * 获取shipping 运输地址
     * @return 简单地址信息
     * @throws Exception
     */
    public TargetGuestShippingAddress getGuestShippingAddress(TargetGuestShippingAddressRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v3/addresses",request,TargetGuestShippingAddress.class,true);
    }

    /**
     * 创建Guest账户
     * @param request 请求信息
     * @return 账户信息
     * @throws Exception
     */
    public TargetGuestAccount createGuestAccount(TargetGuestAccountRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v3",request,TargetGuestAccount.class,false);
    }

    /**
     * 添加支付卡
     * @param request 请求信息
     * @return 支付卡信息
     * @throws Exception
     */
    public TargetGuestPaymentTender addGuestPaymentTender(TargetGuestPaymentTenderRequest request) throws Exception {
        String result=reqTargetApi(Url+"/v3/tenders?type="+request.getType(),JacksonUtil.jsonToMap(JacksonUtil.bean2Json(request)),true,true);
        TargetGuestPaymentTender tender=new TargetGuestPaymentTender();
        if(result.contains("CheckoutProfile"))
            tender.setCheckoutProfile((List<TargetGuestPaymentProtocolData>) JacksonUtil.jsonToMap(result).get("CheckoutProfile"));
        if(result.contains("contact"))
            tender.setContact((List<TargetGuestPaymentContactAddress>) JacksonUtil.jsonToMap(result).get("contact"));
        return tender;
    }



}
