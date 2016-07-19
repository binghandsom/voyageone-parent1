package com.voyageone.web2.vms.openapi.service;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.web2.cms.openapi.OpenApiCmsBaseService;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.VmsOrderAddRequest;
import com.voyageone.web2.sdk.api.request.VmsOrderCancelRequest;
import com.voyageone.web2.sdk.api.request.VmsOrderInfoGetRequest;
import com.voyageone.web2.sdk.api.response.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderCancelResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderInfoGetResponse;
import com.voyageone.web2.vms.openapi.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public String getClassName() {
        return "VmsOrderService";
    }

    /**
     * 增加一条OrderDetail信息
     * @param request VmsOrderAddRequest
     * @return VmsOrderAddResponse
     *
     */
    public VmsOrderAddResponse addOrderInfo(VmsOrderAddRequest request) {
        VmsOrderAddResponse response = new VmsOrderAddResponse();

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
        model.setConsolidationOrderTime(new Date(request.getConsolidationOrderTime()));
        model.setOrderId(request.getOrderId());
        model.setOrderTime(new Date(request.getOrderTime()));
        model.setCartId(request.getCartId());
        model.setClientSku(request.getClientSku());
        model.setBarcode(request.getBarcode());
        model.setDescription(request.getDescription());
        model.setClientMsrp(new BigDecimal(request.getClientMsrp()));
        model.setClientNetPrice(new BigDecimal(request.getClientNetPrice()));
        model.setClientRetailPrice(new BigDecimal(request.getRetailPrice()));
//        model.setMsrp(new BigDecimal(request.getMsrp()));
//        model.setRetailPrice(new BigDecimal(request.getRetailPrice()));
//        model.setSalePrice（new BigDecimal(request.getSalePrice()));
        model.setStatus(VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN);
        model.setCreater(getClassName());
        model.setModifier(getClassName());

        int count = orderDetailService.insertOrderInfo(model);
        // 是否成功
        if (count == 1) {
            response.setResult(true);
        } else {
            throw new ApiException("99", "Fail to Insert Order Info.");
        }

        return response;
    }

    /**
     * 取消物品/订单
     * @param request VmsOrderCancelRequest
     * @return VmsOrderCancelResponse
     *
     */
    @VOTransactional
    public VmsOrderCancelResponse cancelOrder(VmsOrderCancelRequest request) {
        VmsOrderCancelResponse response = new VmsOrderCancelResponse();
        List<String> successReservationIdList = new ArrayList<>();
        response.setSuccessReservationIdList(successReservationIdList);
        List<String> failReservationIdList = new ArrayList<>();
        response.setFailReservationIdList(failReservationIdList);

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        List<String> reservationIdList = request.getReservationIdList();

        if (reservationIdList != null && reservationIdList.size() > 0) {
            VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channelId,
                    VmsConstants.ChannelConfig.VENDOR_OPERATE_TYPE, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
            if (vmsChannelConfigBean == null) {
                throw new ApiException("99", "ChannelId is not correct.");
            }

            for (String reservationId : reservationIdList) {
                // 已经在返回列表中的过滤掉
                if (successReservationIdList.contains(reservationId) || failReservationIdList.contains(reservationId)) {
                    continue;
                }

                Map<String, Object> param = new HashMap<>();
                param.put("channelId", channelId);
                param.put("reservationId", reservationId);
                List<VmsBtOrderDetailModel> models = orderDetailService.select(param);
                // 只有状态为1：Open的物品才能被删除
                if (models.size() > 0) {
                    if (!VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN.equals(models.get(0).getStatus())) {
                        failReservationIdList.add(reservationId);
                        continue;
                    }
                } else {
                    failReservationIdList.add(reservationId);
                    continue;
                }

                // Order级别（只允许Order级别的取消）
                if (VmsConstants.STATUS_VALUE.VENDOR_OPERATE_TYPE.ORDER.equals(vmsChannelConfigBean.getConfigValue1())) {
                    Map<String, Object> param1 = new HashMap<>();
                    param1.put("channelId", channelId);
                    param1.put("consolidationOrderId", models.get(0).getConsolidationOrderId());
                    List<VmsBtOrderDetailModel> modelsInOrder = orderDetailService.select(param1);

                    List<VmsBtOrderDetailModel> modelsInOrderNotOpen = modelsInOrder.stream()
                            .filter(vmsBtOrderDetailModel -> !VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN
                                    .equals(vmsBtOrderDetailModel.getStatus())).collect(Collectors.toList());
                    // 物品对应的Order下面有状态为1：Open以外的物品
                    if (modelsInOrderNotOpen != null && modelsInOrderNotOpen.size() > 0) {
                        failReservationIdList.add(reservationId);
                    } else {
                        boolean allExist = true;
                        // 确认找到Order下面的每个物品是否都在提供的物品列表中
                        for (VmsBtOrderDetailModel model : modelsInOrder) {
                            if (!reservationIdList.contains(model.getReservationId())) {
                                allExist = false;
                            }
                        }
                        // Order下面物品列表在提供的物品列表中都存在，那么可以取消
                        if (allExist) {
                            orderDetailService.updateOrderStatus(channelId, models.get(0).getConsolidationOrderId(), VmsConstants.STATUS_VALUE.PRODUCT_STATUS.CANCEL, getClassName());
                            for (VmsBtOrderDetailModel model : modelsInOrder) {
                                successReservationIdList.add(model.getReservationId());
                            }
                        } else {
                            failReservationIdList.add(reservationId);
                        }
                    }
                } else {
                    // Sku级别（可以单个物品取消）
                    // 只有状态为1：Open的物品才能被删除
                    orderDetailService.updateReservationStatus(channelId, reservationId, VmsConstants.STATUS_VALUE.PRODUCT_STATUS.CANCEL, getClassName());
                    successReservationIdList.add(reservationId);
                }
            }
        }
        return response;
    }

        /**
         * 取得OrderInfo信息
         * @param request VmsOrderAddRequest
         * @return VmsOrderAddResponse
         *
         */
    public VmsOrderInfoGetResponse getOrderInfo(VmsOrderInfoGetRequest request) {
        VmsOrderInfoGetResponse response = new VmsOrderInfoGetResponse();
        List<Map<String, Object>> itemList = new ArrayList<>();
        response.setItemList(itemList);

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        String reservationId = request.getReservationId();
        Long shipmentTimeFrom = request.getShipmentTimeFrom();
        Long shipmentTimeTo = request.getShipmentTimeTo();

        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        // 按照shipmentTime取数据
        if (StringUtils.isEmpty(reservationId)) {
            if (shipmentTimeFrom != null) {
                param.put("shipmentTimeFrom", shipmentTimeFrom);
            }
            if (shipmentTimeTo != null) {
                param.put("shipmentTimeTo", shipmentTimeTo);
            }

        } else {
            // 按照reservationId取数据
            param.put("reservationId", reservationId);
        }
        // 有条件不为空白才执行
        if (param.get("reservationId") != null || param.get("shipmentTimeFrom") != null || param.get("shipmentTimeTo") != null) {
            List<Map<String, Object>> items = orderDetailService.getOrderInfo(param);
            for (Map item : items) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("channelId", item.get("channel_id"));
                newItem.put("reservationId", item.get("reservation_id"));
                if (item.get("shipment_id") != null) {
                    newItem.put("shipmentId", String.valueOf(item.get("shipment_id")));
                }
                newItem.put("trackingType", item.get("tracking_type"));
                newItem.put("trackingNo", item.get("tracking_no"));
                itemList.add(newItem);
            }
        }
        return response;
    }
}
