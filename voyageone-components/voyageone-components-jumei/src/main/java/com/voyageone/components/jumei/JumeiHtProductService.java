package com.voyageone.components.jumei;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.reponse.HtProductAddResponse;
import com.voyageone.components.jumei.reponse.HtProductUpdateResponse;
import com.voyageone.components.jumei.request.HtProductAddRequest;
import com.voyageone.components.jumei.request.HtProductUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Ethan Shi
 * @version 2.1.0
 *
 */

@Service
public class JumeiHtProductService extends JmBase {

    public HtProductAddResponse addProductAndDeal(ShopBean shopBean, HtProductAddRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("国际POP - 创建商品并同时创建Deal返回：" + reqResult);
        HtProductAddResponse response = new HtProductAddResponse();
        response.setBody(reqResult);
        return response;
    }

    public HtProductUpdateResponse update(ShopBean shopBean, HtProductUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult = reqJmApi(shopBean, request.getUrl(), params);
        logger.info("国际POP-修改商品属性返回：" + reqResult);
        HtProductUpdateResponse response = new HtProductUpdateResponse();
        response.setBody(reqResult);
        return response;
    }
}
