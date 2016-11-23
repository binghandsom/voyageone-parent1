package com.voyageone.components.jumei;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.reponse.HtSkuAddResponse;
import com.voyageone.components.jumei.reponse.HtSkuUpdateResponse;
import com.voyageone.components.jumei.request.HtSkuAddRequest;
import com.voyageone.components.jumei.request.HtSkuUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JumeiHtSkuService extends JmBase {
    public HtSkuAddResponse add(ShopBean shopBean, HtSkuAddRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), params);
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length>0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info("添加Sku信息返回：" + reqResult);
        HtSkuAddResponse response = new HtSkuAddResponse();
        response.setBody(reqResult);
        return response;
    }
    public HtSkuUpdateResponse update(ShopBean shopBean, HtSkuUpdateRequest request) throws Exception {
        Map<String, Object> params = request.getParameter();
        String reqResult;
        try {
            reqResult = reqJmApi(shopBean, request.getUrl(), params);
        } catch (BusinessException bex) {
            if (bex.getInfo() != null && bex.getInfo().length>0) {
                reqResult = (String) bex.getInfo()[0];
            } else {
                throw bex;
            }
        }
        logger.info("更新Sku信息返回：" + reqResult);
        HtSkuUpdateResponse response = new HtSkuUpdateResponse();
        response.setBody(reqResult);
        return response;
    }
}
