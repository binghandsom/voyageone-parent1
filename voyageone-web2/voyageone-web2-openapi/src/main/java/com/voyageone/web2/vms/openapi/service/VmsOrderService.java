package com.voyageone.web2.vms.openapi.service;

import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.web2.cms.openapi.OpenApiCmsBaseService;
import com.voyageone.web2.sdk.api.request.VmsOrderAddGetRequest;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class VmsOrderService extends OpenApiCmsBaseService {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * get the product info from wms's request
     */
    public VmsOrderAddGetResponse addOrderInfo(VmsOrderAddGetRequest request) {
        VmsOrderAddGetResponse response = new VmsOrderAddGetResponse();

//        checkCommRequest(request);
//        //ChannelId
//        String channelId = request.getChannelId();
//        checkRequestChannelId(channelId);
//
//        request.check();
//
//        // set fields
//        String[] projection = getProjection(request);
//
//        //getProductByCode
//        String productSku = request.getSku();
//
//        if (!StringUtils.isEmpty(productSku)) {
//            ProductForWmsBean bean = productService.getWmsProductsInfo(channelId, productSku, projection);
//            response.setResultInfo(bean);
//        }
        return response;
    }
}
