package com.voyageone.components.intltarget.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.guest.TargetGuestAccount;
import com.voyageone.components.intltarget.bean.guest.TargetGuestAccountRequest;
import com.voyageone.components.intltarget.bean.guest.TargetGuestShippingAddress;
import com.voyageone.components.intltarget.bean.guest.TargetGuestShippingAddressRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        return getApiResponseWithKey("/v3/addresses",request,TargetGuestShippingAddress.class,true);
    }

    /**
     * 创建Guest账户
     * @param request 请求信息
     * @return 账户信息
     * @throws Exception
     */
    public TargetGuestAccount createGuestAccount(TargetGuestAccountRequest request) throws Exception {
        return getApiResponseWithKey("/v3",request,TargetGuestAccount.class,false);
    }

    /**
     * 获取api响应包含key
     * @param url api路径
     * @param reqdata 请求数据
     * @param clazz 返回对象类型
     * @param <E> 返回对象
     * @return response
     */
    private <E> E getApiResponseWithKey(String url,Object reqdata,Class<E> clazz,boolean isNeedToken) throws Exception {
        String result=reqTargetApi(Url+url,JacksonUtil.jsonToMap(JacksonUtil.bean2Json(reqdata)),true,isNeedToken);
        return StringUtils.isEmpty(result)?null: JacksonUtil.json2Bean(result,clazz);
    }

}
