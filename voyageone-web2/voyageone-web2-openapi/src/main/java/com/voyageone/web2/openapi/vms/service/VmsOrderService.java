package com.voyageone.web2.openapi.vms.service;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.VmsChannelConfigs;
import com.voyageone.common.configs.beans.VmsChannelConfigBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.vms.order.OrderDetailService;
import com.voyageone.service.impl.vms.shipment.ShipmentService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtShipmentModel;
import com.voyageone.web2.openapi.OpenApiBaseService;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.vms.*;
import com.voyageone.web2.openapi.vms.VmsConstants;
import com.voyageone.web2.sdk.api.response.vms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Timestamp;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 */
@Service
public class VmsOrderService extends OpenApiBaseService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private FeedInfoService feedInfoService;

    public String getClassName() {
        return "VmsOrderService";
    }

    /**
     * 增加一条OrderDetail信息
     * @param request VmsOrderAddRequest
     * @return VmsOrderAddResponse
     *
     */
    @VOTransactional
    public VmsOrderAddResponse addOrderInfo(VmsOrderAddRequest request) {
        $info("/vms/order/addOrderInfo is start");
        VmsOrderAddResponse response = new VmsOrderAddResponse();
        checkCommRequest(request);
        request.check();

        List<Map<String, Object>> itemList = request.getItemList();

        if (itemList != null && itemList.size() > 0) {
            for (Map<String, Object> item : itemList) {
                // channelId + reservationId如果已经存在那么久跳过
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", (String) item.get("channelId"));
                param.put("reservationId", (String) item.get("reservationId"));
                List<VmsBtOrderDetailModel> models = orderDetailService.select(param);
                if (models.size() > 0) {
                    continue;
                }

                // 建立Model
                VmsBtOrderDetailModel model = new VmsBtOrderDetailModel();
                model.setReservationId((String) item.get("reservationId"));
                model.setChannelId((String) item.get("channelId"));
                model.setConsolidationOrderId((String) item.get("consolidationOrderId"));
                model.setConsolidationOrderTime(new Date((Long) item.get("consolidationOrderTime")));
                model.setOrderId((String) item.get("orderId"));
                model.setOrderTime(new Date((Long) item.get("orderTime")));
                model.setCartId((Integer) item.get("cartId"));
                model.setClientSku((String) item.get("clientSku"));
                model.setBarcode((String) item.get("barcode"));
                model.setName((String) item.get("name"));
                model.setClientMsrp(new BigDecimal((Double) item.get("clientMsrp")));
                model.setClientNetPrice(new BigDecimal((Double) item.get("clientNetPrice")));
                model.setClientRetailPrice(new BigDecimal((Double) item.get("clientRetailPrice")));
                model.setClientPromotionPrice(new BigDecimal((Double) item.get("clientNetPrice")));
                model.setRetailPrice(new BigDecimal((Double) item.get("retailPrice")));
                model.setStatus(VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN);
                model.setCreater(getClassName());
                model.setModifier(getClassName());
                setDynamicAttribute((String) item.get("channelId"), (String) item.get("clientSku"), model);
                orderDetailService.insertOrderInfo(model);
                $info("addOrderInfo: reservationId = " + (String) item.get("reservationId") + ", channelId = " + (String) item.get("channelId"));
            }
        }

        $info("/vms/order/addOrderInfo is end");
        return response;
    }


    /**
     * 设定用户指定的动态Attribute
     * @param channelId 渠道id
     * @param clientSku SKU
     * @param model VmsBtOrderDetailModel
     *
     */
    private void setDynamicAttribute(String channelId, String clientSku,  VmsBtOrderDetailModel model) {
        VmsChannelConfigBean vmsChannelConfigBean = VmsChannelConfigs.getConfigBean(channelId,
                VmsConstants.ChannelConfig.ADDITIONAL_ATTRIBUTES, VmsConstants.ChannelConfig.COMMON_CONFIG_CODE);
        if (vmsChannelConfigBean != null) {
            JongoQuery queryObject = new JongoQuery();
            queryObject.setQuery("{\"skus.clientSku\":\"" + clientSku + "\"}");
            List<CmsBtFeedInfoModel> feeds = feedInfoService.getList(channelId, queryObject);
            if (feeds != null && feeds.size() > 0) {
                String value = vmsChannelConfigBean.getConfigValue1();
                String[] dynamicAttributes = value.split(",");
                // 目前最多支持3个动态属性
                int to = dynamicAttributes.length;
                if (to > 3) {
                    to = 3;
                }
                for (int i = 0; i < to; i++) {
                    String attributeValue = "";
                    String[] attributeGroup = dynamicAttributes[i].split("\\.");
                    if (attributeGroup != null && attributeGroup.length == 2) {
                        // 取得sku中Attribute的场合
                        if ("sku".equals(attributeGroup[0].toLowerCase())) {
                            CmsBtFeedInfoModel_Sku skuModel = null;
                            for (CmsBtFeedInfoModel_Sku sku : feeds.get(0).getSkus()) {
                                if (clientSku.equals(sku.getClientSku())) {
                                    skuModel = sku;
                                    break;
                                }
                            }
                            if (skuModel != null && skuModel.getAttribute() != null) {
                                attributeValue = skuModel.getAttribute().get(attributeGroup[1]);
                            }
                        } else {
                            // 取得code中Attribute的场合
                            if (feeds.get(0).getAttribute() != null) {
                                List<String> attributeList = feeds.get(0).getAttribute().get(attributeGroup[1]);
                                if (attributeList != null && attributeList.size() > 0) {
                                    attributeValue = attributeList.get(0);
                                }
                            }
                        }
                    }
                    if (!StringUtils.isEmpty(attributeValue)) {
                        if (i == 0) {
                            model.setAttribute1(attributeValue);
                        } else if (i == 1) {
                            model.setAttribute2(attributeValue);
                        } else if (i == 2) {
                            model.setAttribute3(attributeValue);
                        }
                    }
                }
            }
        }
    }

    /**
     * 取消物品/订单
     * @param request VmsOrderCancelRequest
     * @return VmsOrderCancelResponse
     *
     */
    @VOTransactional
    public VmsOrderCancelResponse cancelOrder(VmsOrderCancelRequest request) {
        $info("/vms/order/cancelOrder is start");
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
                    // 如果状态已经是7：Cancel的话，那么按成功处理
                    if (VmsConstants.STATUS_VALUE.PRODUCT_STATUS.CANCEL.equals(models.get(0).getStatus())) {
                        successReservationIdList.add(reservationId);
                        continue;
                    }
                    // 如果状态已经是1：Open以外的话（非7:Cancel），那么按失败处理
                    if (!VmsConstants.STATUS_VALUE.PRODUCT_STATUS.OPEN.equals(models.get(0).getStatus())) {
                        failReservationIdList.add(reservationId);
                        continue;
                    }
                } else {
                    // 如果不存在，按失败处理
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
        if (successReservationIdList.size() > 0) {
            response.getSuccessReservationIdList().stream().forEach(item -> System.out.println("cancel success: reservationId = " + item));
        }
        if (failReservationIdList.size() > 0) {
            response.getFailReservationIdList().stream().forEach(item -> System.out.println("cancel fail: reservationId = " + item));
        }
        $info("/vms/order/cancelOrder is end");
        return response;
    }

    /**
     * 取得OrderInfo信息
     * @param request VmsOrderAddRequest
     * @return VmsOrderAddResponse
     *
     */
    public VmsOrderInfoGetResponse getOrderInfo(VmsOrderInfoGetRequest request) {
        $info("/vms/order/getOrderInfo is start");
        VmsOrderInfoGetResponse response = new VmsOrderInfoGetResponse();
        List<Map<String, Object>> itemList = new ArrayList<>();
        response.setItemList(itemList);

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        String reservationId = request.getReservationId();
        Long timeFrom = request.getTimeFrom();
        Long timeTo = request.getTimeTo();
        String type = request.getType();

        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        // 按照shipmentTime取数据
        if (StringUtils.isEmpty(reservationId)) {
            if (VmsConstants.GET_ORDER_INFO_TYPE_VALUE.SHIPPED.equals(type)) {
                if (timeFrom != null) {
                    param.put("shipmentTimeFrom", new Date(timeFrom));
                }
                if (timeTo != null) {
                    param.put("shipmentTimeTo", new Date(timeTo));
                }
                param.put("status", VmsConstants.STATUS_VALUE.PRODUCT_STATUS.SHIPPED);
            } else if (VmsConstants.GET_ORDER_INFO_TYPE_VALUE.CANCELED.equals(type)) {
                if (timeFrom != null) {
                    param.put("cancelTimeFrom", new Date(timeFrom));
                }
                if (timeTo != null) {
                    param.put("cancelTimeTo", new Date(timeTo));
                }
                param.put("status", VmsConstants.STATUS_VALUE.PRODUCT_STATUS.CANCEL);
            } else {
                throw new ApiException("99", "param[type] must be 1 or 2.");
            }
        } else {
            // 按照reservationId取数据
            param.put("reservationId", reservationId);
        }
        // 有条件不为空白才执行
        if (param.get("reservationId") != null
                || param.get("shipmentTimeFrom") != null
                || param.get("shipmentTimeTo") != null
                || param.get("cancelTimeFrom") != null
                || param.get("cancelTimeTo") != null) {
            List<Map<String, Object>> items = orderDetailService.getOrderInfo(param);
            for (Map item : items) {
                Map<String, Object> newItem = new HashMap<>();
                newItem.put("channelId", item.get("channel_id"));
                newItem.put("reservationId", item.get("reservation_id"));
                newItem.put("status", item.get("status"));
                if (item.get("shipment_id") != null) {
                    newItem.put("shipmentId", String.valueOf(item.get("shipment_id")));
                }
                if (item.get("shipment_time") != null) {
                    newItem.put("shipmentTime", ((Timestamp) item.get("shipment_time")).getTime());
                }
                if (item.get("express_company") != null) {
                    newItem.put("expressCompany", item.get("express_company"));
                }
                if (item.get("tracking_no") != null) {
                    newItem.put("trackingNo", item.get("tracking_no"));
                }
                itemList.add(newItem);
            }
        } else {
            throw new ApiException("99", "param is not correct.");
        }

        $info("/vms/order/getOrderInfo is end");
        return response;
    }


    /**
     * 更新某个物品的状态为5：Received；或者 6：Receive Error
     * @param request VmsOrderStatusUpdateRequest
     * @return VmsOrderStatusUpdateResponse
     *
     */
    public VmsOrderStatusUpdateResponse updateOrderStatus(VmsOrderStatusUpdateRequest request) {
        $info("/vms/order/updateOrderStatus is start");
        VmsOrderStatusUpdateResponse response = new VmsOrderStatusUpdateResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        String reservationId = request.getReservationId();
        String status = request.getStatus();
        String receiver = request.getReceiver();

        int count = 0;
        // 更新为6：Receive Error的情况
        if(VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVE_ERROR.equals(status)) {
            count = orderDetailService.updateReservationStatus(channelId, reservationId,
                    VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVE_ERROR, getClassName());
        } else if (VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED.equals(status)) {
            // 因为会更新receiveTime，关系到出财务报表，所以以防万一，处理多次更新
            Map<String, Object> param = new HashMap<>();
            param.put("channelId", channelId);
            param.put("reservationId", reservationId);
            List<VmsBtOrderDetailModel> models = orderDetailService.selectOrderList(param);
            if (models.size() > 0) {
                if (!VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED.equals(models.get(0).getStatus())) {
                    // 更新为5：Received的情况
                    count = orderDetailService.updateReservationStatus(channelId, reservationId,
                            VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED, getClassName(), new Date(), receiver);
                } else {
                    // 当做已经更新处理
                    count = 1;
                }
            }
        } else {
            throw new ApiException("99", "This Status is not allowed to be update.");
        }

        if (count > 0) {
            $info("updateOrderStatus success:reservationId = " + reservationId + ",channelId = " + channelId + ",status = " + status);
        } else {
            throw new ApiException("99", "channelId:" + channelId + ",reservationId:" + reservationId + " is not exist.");
        }

        $info("/vms/order/updateOrderStatus is end");
        return response;
    }


    /**
     * 更新某个Shipment的状态为4：Arrived；5：Received；6：Receive Error
     * @param request VmsShipmentStatusUpdateRequest
     * @return VmsShipmentStatusUpdateResponse
     *
     */
    public VmsShipmentStatusUpdateResponse updateShipmentStatus(VmsShipmentStatusUpdateRequest request) {
        $info("/vms/order/updateShipmentStatus is start");
        VmsShipmentStatusUpdateResponse response = new VmsShipmentStatusUpdateResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        Integer shipmentId = request.getShipmentId();
        String status = request.getStatus();
        Long operateTime = request.getOperateTime();
        String operator = request.getOperator();
        String comment = request.getComment();
        int count = 0;
        if(VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.ARRIVED.equals(status)
                || VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVED.equals(status)
                || VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVE_ERROR.equals(status)) {
            VmsBtShipmentModel model = new VmsBtShipmentModel();
            model.setChannelId(channelId);
            model.setId(shipmentId);
            model.setStatus(status);
            model.setModifier(getClassName());
            if (VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.ARRIVED.equals(status)) {
                // 取得shipment原先的状态
                Map<String, Object> param = new HashMap<>();
                param.put("channelId", channelId);
                param.put("shipmentId", shipmentId);
                List<VmsBtShipmentModel> oldModels = shipmentService.select(param);
                if (oldModels.size() > 0) {
                    String oldStatus = oldModels.get(0).getStatus();
                    // 状态为5：Received；6：Receive Error；以外的才能更新为4：ARRIVED
                    if (VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVED.equals(oldStatus)
                            ||VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVE_ERROR.equals(oldStatus)) {
                        throw new ApiException("98", "previous status is not correct.channelId:" + channelId + ",shipmentId:" + shipmentId);
                    }
                } else {
                    throw new ApiException("99", "channelId:" + channelId + ",shipmentId:" + shipmentId + " is not exist.");
                }
                model.setArrivedTime(new Date(operateTime));
                model.setArriver(operator);
            } else if (VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.RECEIVED.equals(status)) {
                model.setReceivedTime(new Date(operateTime));
                model.setReceiver(operator);
            } else {
                // 更新状态为ReceiveError的情况下，叠加Comment
                if (!StringUtils.isEmpty(comment)) {
                    VmsBtShipmentModel oldModel = shipmentService.select(shipmentId);
                    if (oldModel != null && !StringUtils.isEmpty(oldModel.getComment())) {
                        model.setComment(oldModel.getComment() + "\r\nAdd By Vo：" + comment);
                    } else {
                        model.setComment("Add By Vo：" + comment);
                    }
                }
            }
            count = shipmentService.update(model);
        } else {
            throw new ApiException("97", "This Status is not allowed to be update.");
        }

        if (count > 0) {
            $info("updateShipmentStatus success:shipmentId = " + shipmentId + ",channelId = " + channelId + ",status = " + status);
        } else {
            throw new ApiException("99", "channelId:" + channelId + ",shipmentId:" + shipmentId + " is not exist.");
        }

        $info("/vms/order/updateShipmentStatus is end");
        return response;
    }

    /**
     * 更新不使用vms进行发货的物品状态（3：shipped）及物流信息
     * @param request VmsShipmentStatusUpdateRequest
     * @return VmsShipmentStatusUpdateResponse
     *
     */
    @VOTransactional
    public VmsOrderShipmentSynResponse synOrderShipment(VmsOrderShipmentSynRequest request) {
        $info("/vms/order/synOrderShipment is start");
        VmsOrderShipmentSynResponse response = new VmsOrderShipmentSynResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        String reservationId = request.getReservationId();
        String expressCompany = request.getExpressCompany();
        String trackingNo = request.getTrackingNo();
        Long shippedTime = request.getShippedTime();
        Integer shipmentId = null;

        // 先看看如果trackingNo不存在那么去生成shipment
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("expressCompany", expressCompany);
        param.put("trackingNo", trackingNo);
        List<VmsBtShipmentModel> models = shipmentService.selectList(param);
        // 不存在shipment，那么新建一个shipment
        if (models.size() == 0) {
            VmsBtShipmentModel model = new VmsBtShipmentModel();
            model.setChannelId(channelId);
            model.setShipmentName("Shipment_" + DateTimeUtil.getNow("yyyyMMdd_HHmmss"));
            model.setShippedDate(new Date(shippedTime));
            model.setExpressCompany(expressCompany);
            model.setTrackingNo(trackingNo);
            model.setStatus(VmsConstants.STATUS_VALUE.SHIPMENT_STATUS.SHIPPED);
            model.setCreater(getClassName());
            shipmentService.insert(model);
            shipmentId = model.getId();
        } else {
            shipmentId = models.get(0).getId();
        }

        // 取得物品原先的状态
        Map<String, Object> param1 = new HashMap<>();
        param1.put("channelId", channelId);
        param1.put("reservationId", reservationId);
        List<VmsBtOrderDetailModel> oldModels = orderDetailService.select(param1);
        if (oldModels.size() > 0) {
            String oldStatus = oldModels.get(0).getStatus();
            // 状态为5：Received；6：Receive Error；以外的才能更新为3：SHIPPED
            if (VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVED.equals(oldStatus)
                    ||VmsConstants.STATUS_VALUE.PRODUCT_STATUS.RECEIVE_ERROR.equals(oldStatus)) {
                throw new ApiException("98", "previous status is not correct.channelId:" + channelId + ",reservationId:" + reservationId);
            }
        } else {
            throw new ApiException("99", "channelId:" + channelId + ",reservationId:" + reservationId + " is not exist.");
        }

        // 更新物品的状态为3：shipped
        int count = orderDetailService.updateReservationStatusToShipped(channelId, reservationId, shipmentId, getClassName(), new Date(shippedTime), null);

        if (count > 0) {
            response.setShipmentId(shipmentId);
            $info("synOrderShipment success:reservationId = " + reservationId + ",channelId = " + channelId
                    + ",shipmentId = " + shipmentId);
        } else {
            throw new ApiException("99", "channelId:" + channelId + ",reservationId:" + reservationId + " is not exist.");
        }

        $info("/vms/order/synOrderShipment is end");
        return response;
    }

}
