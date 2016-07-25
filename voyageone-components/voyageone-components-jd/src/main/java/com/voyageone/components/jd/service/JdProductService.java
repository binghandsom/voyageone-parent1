package com.voyageone.components.jd.service;

import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.request.ware.WareDeleteRequest;
import com.jd.open.api.sdk.response.ware.WareDeleteResponse;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jd.JdBase;
import org.springframework.stereotype.Component;

/**
 * @author james.li on 2016/7/21.
 * @version 2.0.0
 */
@Component
public class JdProductService extends JdBase {

    public Boolean delItem(ShopBean config, String numId) throws  JdException {
        WareDeleteRequest request=new WareDeleteRequest();
        request.setWareId(numId);
        request.setTradeNo(DateTimeUtil.getNowTimeStamp());
        WareDeleteResponse response = reqApi(config,request);
        if ("0".equalsIgnoreCase(response.getCode())) {
            throw new BusinessException("京东删除商品失败 京东错误码：" + response.getCode());
        }
        return true;
    }
}
