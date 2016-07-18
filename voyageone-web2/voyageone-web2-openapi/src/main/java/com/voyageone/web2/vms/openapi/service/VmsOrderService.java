package com.voyageone.web2.vms.openapi.service;

import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.web2.cms.openapi.OpenApiCmsBaseService;
import com.voyageone.web2.sdk.api.request.VmsOrderAddGetRequest;
import com.voyageone.web2.sdk.api.response.VmsOrderAddGetResponse;
import com.voyageone.web2.vms.openapi.VmsConstants;
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
     * 增加一条OrderDetail信息
     * @param request VmsOrderAddGetRequest
     * @return VmsOrderAddGetResponse
     *
     */
    public VmsOrderAddGetResponse addOrderInfo(VmsOrderAddGetRequest request) {
        VmsOrderAddGetResponse response = new VmsOrderAddGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        // 建立Model
        VmsBtOrderDetailModel model = new VmsBtOrderDetailModel();
        model.setReservationId(request.getReservationId());
        model.setChannelId(request.getChannelId());
        model.setConsolidationOrderId(request.getConsolidationOrderId());
        model.setConsolidationOrderTime(request.getConsolidationOrderTime());
        model.setOrderId(request.getOrderId());
        model.setOrderTime(request.getOrderTime());
        model.setCartId(request.getCartId());
        model.setClientSku(request.getClientSku());
        model.setBarcode(request.getBarcode());
        model.setDescription(request.getDescription());
        model.setClientMsrp(request.getClientMsrp());
        model.setClientNetPrice(request.getClientNetPrice());
        model.setClientRetailPrice(request.getRetailPrice());
//        model.setMsrp(request.getMsrp());
//        model.setRetailPrice(request.getRetailPrice());
//        model.setSalePrice（request.getSalePrice());
        model.setStatus(VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN);
        model.setCreater(this.getClass().getName());
        model.setModifier(this.getClass().getName());

        int count = orderDetailService.insertOrderInfo(model);
        // 是否成功
        if (count == 1) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }

        return response;
    }
}
