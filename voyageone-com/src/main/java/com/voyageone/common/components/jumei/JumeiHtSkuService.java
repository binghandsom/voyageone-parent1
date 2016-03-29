package com.voyageone.common.components.jumei;

import com.voyageone.common.components.jumei.Reponse.HtProductUpdateResponse;
import com.voyageone.common.components.jumei.Reponse.HtSkuAddResponse;
import com.voyageone.common.components.jumei.Request.HtProductUpdateRequest;
import com.voyageone.common.components.jumei.Request.HtSkuAddRequest;
import com.voyageone.common.components.jumei.base.JmBase;
import com.voyageone.common.configs.beans.ShopBean;

import java.util.Map;

/**
 * Created by dell on 2016/3/29.
 */
public class JumeiHtSkuService extends JmBase {
    public HtSkuAddResponse add(ShopBean shopBean, HtSkuAddRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("添加Sku信息返回：" + reqResult);
        HtSkuAddResponse response = new HtSkuAddResponse();
        response.setBody(reqResult);
        return response;
    }
}
