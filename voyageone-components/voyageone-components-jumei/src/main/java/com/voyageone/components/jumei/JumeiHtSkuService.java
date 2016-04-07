package com.voyageone.components.jumei;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.Reponse.HtSkuAddResponse;
import com.voyageone.components.jumei.Request.HtSkuAddRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
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
