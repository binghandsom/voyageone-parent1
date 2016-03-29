package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;

import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class JumeiHtSkuService extends JmBase {
    public HtProductUpdateResponse copyDeal(ShopBean shopBean, HtProductUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("国际POP-修改商品属性返回：" + reqResult);
        HtProductUpdateResponse response = new HtProductUpdateResponse();
        response.setBody(reqResult);
        return response;
    }

}
