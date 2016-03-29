package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Reponse.HtSkuAddResponse;
import com.voyageone.common.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.components.jumei.Request.HtSkuAddRequest;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JumeiHtProductService extends JmBase {
    public HtProductUpdateResponse copyDeal(ShopBean shopBean, HtProductUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("国际POP-修改商品属性返回：" + reqResult);
        HtProductUpdateResponse response = new HtProductUpdateResponse();
        response.setBody(reqResult);
        return response;
    }
}
