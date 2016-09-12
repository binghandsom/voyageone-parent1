package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.vms.order.CAClientService;
import com.voyageone.service.model.vms.VmsBtClientOrderDetailsModel;
import com.voyageone.service.model.vms.VmsBtClientOrdersModel;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import com.voyageone.web2.sdk.api.channeladvisor.domain.OrderAddressModel;
import com.voyageone.web2.sdk.api.channeladvisor.domain.OrderItemCancellationModel;
import com.voyageone.web2.sdk.api.channeladvisor.domain.OrderItemModel;
import com.voyageone.web2.sdk.api.channeladvisor.domain.OrderModel;
import com.voyageone.web2.sdk.api.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.web2.sdk.api.channeladvisor.enums.OrderStatusEnum;
import com.voyageone.web2.sdk.api.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.web2.sdk.api.channeladvisor.request.ShipRequest;
import com.voyageone.web2.sdk.api.channeladvisor.response.ActionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aooer 2016/9/6.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@Profile("product")
@VOTransactional
public class CAOrderServiceImpl extends CAOpenApiBaseService implements CAOrderService {

    @Autowired
    private CAClientService caClientService;

    @Autowired
    private MqSender mqSender;

    @Autowired
    private IssueLog issueLog;

    public ActionResponse getOrders(String status, String limit) {
        String channelId = getClientChannelId();

        //根据CA请求参数【status】、【limit】， 检索【品牌方订单一览】vms_bt_client_orders

        List<VmsBtClientOrdersModel> ordersModels = caClientService.getClientOrderList(channelId, status, limit);
        List<String> orderIds = ordersModels.stream().map(VmsBtClientOrdersModel::getClientOrderId).collect(Collectors.toList());

        //根据取得的client_order_id，检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> orderDetailsModels = caClientService.getClientOrderDetailList(channelId, orderIds);

        List<OrderModel> responseBody = new ArrayList<>();

        //将取得的订单明细，按照client_order_id，放入取得的订单信息中
        for (VmsBtClientOrdersModel m : ordersModels) {
            OrderModel orderModel = new OrderModel();
            orderModel.setId(m.getOrderChannelId());
            OrderAddressModel buyerOrderAddressModel = new OrderAddressModel();
            buyerOrderAddressModel.setAddressLine1(m.getBuyerAddressLine1());
            buyerOrderAddressModel.setAddressLine2(m.getBuyerAddressLine2());
            buyerOrderAddressModel.setCity(m.getBuyerCity());
            buyerOrderAddressModel.setCompanyName(m.getBuyerCompanyName());
            buyerOrderAddressModel.setCountry(m.getBuyerCountry());
            buyerOrderAddressModel.setDaytimePhone(m.getBuyerDaytimePhone());
            buyerOrderAddressModel.setEmailAddress(m.getBuyerEmailAddress());
            buyerOrderAddressModel.setEveningPhone(m.getBuyerEveningPhone());
            buyerOrderAddressModel.setFirstName(m.getBuyerFirstName());
            buyerOrderAddressModel.setLastName(m.getBuyerLastName());
            buyerOrderAddressModel.setNameSuffix(m.getBuyerNameSuffix());
            buyerOrderAddressModel.setPostalCode(m.getBuyerPostalCode());
            buyerOrderAddressModel.setStateOrProvince(m.getBuyerStateOrProvince());
            orderModel.setBuyerAddress(buyerOrderAddressModel);
            orderModel.setCurrency(m.getCurrency());
            orderModel.setDeliverByDateUtc(DateTimeUtil.parse(m.getDeliverByDate()));

            List<OrderItemModel> orderItemsModes = new ArrayList<>();
            for (VmsBtClientOrderDetailsModel orderDetailsModel : orderDetailsModels) {
                if (m.getClientOrderId().equals(orderDetailsModel.getClientOrderId())) {
                    OrderItemModel orderItemModel = new OrderItemModel();
                    orderItemModel.setId(orderDetailsModel.getOrderItemId());
                    orderItemModel.setQuantity(orderDetailsModel.getQuantity());
                    orderItemModel.setSellerSku(orderDetailsModel.getSellerSku());
                    orderItemModel.setUnitPrice(orderDetailsModel.getUnitPrice());
                }
            }

            orderModel.setItems(orderItemsModes);
            orderModel.setOrderDateUtc(DateTimeUtil.parse(m.getOrderDate()));
            orderModel.setOrderStatus(OrderStatusEnum.getInstance(m.getOrderStatus()));
            orderModel.setRequestedShippingMethod(m.getRequestedShippingMethod());
            orderModel.setTotalFees(m.getTotalFees());
            orderModel.setTotalGiftOptionPrice(m.getTotalGiftOptionPrice());
            orderModel.setTotalGiftOptionTaxPrice(m.getTotalGiftOptionTaxPrice());
            orderModel.setTotalPrice(m.getTotalPrice());
            orderModel.setTotalShippingPrice(m.getTotalShippingPrice());
            orderModel.setTotalShippingTaxPrice(m.getTotalShippingTaxPrice());
            orderModel.setTotalTaxPrice(m.getTotalTaxPrice());
            orderModel.setVatInclusive(m.getVatInclusive());

            responseBody.add(orderModel);
        }

        //将订单信息返回

        return success(responseBody);
    }

    public ActionResponse getOrderById(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        String channelId = getClientChannelId();

        //请求参数【订单ID】不为空的场合，检索【品牌方订单一览】vms_bt_client_orders

        VmsBtClientOrdersModel m = caClientService.getClientOrderById(channelId, orderID);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (m == null) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }


        //取得的client_order_id，检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> orderDetailsModels = caClientService.getClientOrderDetailList(channelId, Collections.singletonList(m.getClientOrderId()));

        //将取得的订单明细，按照client_order_id，放入取得的订单信息中

        OrderModel orderModel = new OrderModel();
        orderModel.setId(m.getOrderChannelId());
        OrderAddressModel buyerOrderAddressModel = new OrderAddressModel();
        buyerOrderAddressModel.setAddressLine1(m.getBuyerAddressLine1());
        buyerOrderAddressModel.setAddressLine2(m.getBuyerAddressLine2());
        buyerOrderAddressModel.setCity(m.getBuyerCity());
        buyerOrderAddressModel.setCompanyName(m.getBuyerCompanyName());
        buyerOrderAddressModel.setCountry(m.getBuyerCountry());
        buyerOrderAddressModel.setDaytimePhone(m.getBuyerDaytimePhone());
        buyerOrderAddressModel.setEmailAddress(m.getBuyerEmailAddress());
        buyerOrderAddressModel.setEveningPhone(m.getBuyerEveningPhone());
        buyerOrderAddressModel.setFirstName(m.getBuyerFirstName());
        buyerOrderAddressModel.setLastName(m.getBuyerLastName());
        buyerOrderAddressModel.setNameSuffix(m.getBuyerNameSuffix());
        buyerOrderAddressModel.setPostalCode(m.getBuyerPostalCode());
        buyerOrderAddressModel.setStateOrProvince(m.getBuyerStateOrProvince());
        orderModel.setBuyerAddress(buyerOrderAddressModel);
        orderModel.setCurrency(m.getCurrency());
        orderModel.setDeliverByDateUtc(DateTimeUtil.parse(m.getDeliverByDate()));

        List<OrderItemModel> orderItemsModes = new ArrayList<>();
        for (VmsBtClientOrderDetailsModel orderDetailsModel : orderDetailsModels) {
            if (m.getClientOrderId().equals(orderDetailsModel.getClientOrderId())) {
                OrderItemModel orderItemModel = new OrderItemModel();
                orderItemModel.setId(orderDetailsModel.getOrderItemId());
                orderItemModel.setQuantity(orderDetailsModel.getQuantity());
                orderItemModel.setSellerSku(orderDetailsModel.getSellerSku());
                orderItemModel.setUnitPrice(orderDetailsModel.getUnitPrice());
            }
        }

        orderModel.setItems(orderItemsModes);
        orderModel.setOrderDateUtc(DateTimeUtil.parse(m.getOrderDate()));
        orderModel.setOrderStatus(OrderStatusEnum.getInstance(m.getOrderStatus()));
        orderModel.setRequestedShippingMethod(m.getRequestedShippingMethod());
        orderModel.setTotalFees(m.getTotalFees());
        orderModel.setTotalGiftOptionPrice(m.getTotalGiftOptionPrice());
        orderModel.setTotalGiftOptionTaxPrice(m.getTotalGiftOptionTaxPrice());
        orderModel.setTotalPrice(m.getTotalPrice());
        orderModel.setTotalShippingPrice(m.getTotalShippingPrice());
        orderModel.setTotalShippingTaxPrice(m.getTotalShippingTaxPrice());
        orderModel.setTotalTaxPrice(m.getTotalTaxPrice());
        orderModel.setVatInclusive(m.getVatInclusive());

        //将订单信息返回

        return success(orderModel);
    }

    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        String channelId = getClientChannelId();

        //检索【品牌方订单一览】vms_bt_client_orders

        VmsBtClientOrdersModel m = caClientService.getClientOrderById(channelId, orderID);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (m == null) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        // 更新【品牌方订单一览】vms_bt_client_orders

        caClientService.updateClientOrderStatus(channelId, orderID, "AcknowledgedBySeller");

        //消息生成
        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("client_order_id", m.getClientOrderId());
        mqMessageMap.put("order_channel_id", m.getOrderChannelId());
        mqSender.sendMessage("voyageone_vms_wsdl_mq_acknowledge_order_queue", mqMessageMap);

        return success();
    }

    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "ShipRequest not found.");
        }

        String channelId = getClientChannelId();

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, "AcknowledgedBySeller");

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        //根据请求参数中Items.SellerSku， 匹配 品牌方订单明细中的 seller_sku，找出对应的明细。
        List<Long> tempReservationId = new ArrayList<>();
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();
            if (request.getItems().get(sku) != null) {

                vmsBtClientOrderDetailsModel.setShippedDate(DateTimeUtil.format(request.getShippedDateUtc(), null));
                vmsBtClientOrderDetailsModel.setTrackingNumber(request.getTrackingNumber());
                vmsBtClientOrderDetailsModel.setShippingCarrier(request.getShippingCarrier());
                vmsBtClientOrderDetailsModel.setShippingClass(request.getShippingClass());
                vmsBtClientOrderDetailsModel.setStatus("Shipped");

                matchModelList.add(vmsBtClientOrderDetailsModel);
                tempReservationId.add(vmsBtClientOrderDetailsModel.getReservationId());
            }
        }

        //根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList);

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (CollectionUtils.isEmpty(matchModelList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        if (request.getItems().size() > matchModelList.size()) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = "Shipped";

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (!vmsBtClientOrderDetailsModel.getStatus().equals("Shipped")) {
                tempStatus = "PartiallyShipped";
            }
        }

        // 更新 【品牌方订单一览】vms_bt_client_orders
        caClientService.updateClientOrderStatus(channelId, orderID, tempStatus);

        // 消息生成
        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);
        mqMessageMap.put("shipped_date", DateTimeUtil.format(request.getShippedDateUtc(), null));
        mqMessageMap.put("tracking_number", request.getTrackingNumber());
        mqMessageMap.put("shipping_carrier", request.getShippingCarrier());
        mqMessageMap.put("shipping_class", request.getShippingClass());

        String reservationIds = "";

        for (Long aLong : tempReservationId) {
            reservationIds += aLong + ",";
        }

        mqMessageMap.put("items", reservationIds.substring(0, reservationIds.length() - 1));

        mqSender.sendMessage("voyageone_vms_wsdl_mq_ship_order_queue", mqMessageMap);

        return success();
    }

    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderCancellationRequest not found.");
        }

        String channelId = getClientChannelId();

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, "AcknowledgedBySeller");

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        // 根据请求参数中Items.SellerSku， 匹配品牌方订单明细中的 seller_sku，找出对应的明细。

        Map<String, Object> tempRecord = new HashMap<>();
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();

            boolean hasItem = false;

            String cancelReason = "";
            for (OrderItemCancellationModel orderItemCancellationModel : request.getItems()) {
                if (orderItemCancellationModel.getId().equals(sku)) {
                    hasItem = true;
                    cancelReason = orderItemCancellationModel.getReason().name();
                }
            }

            if (hasItem) {
                vmsBtClientOrderDetailsModel.setCancelReason(cancelReason);
                vmsBtClientOrderDetailsModel.setStatus("Canceled");

                matchModelList.add(vmsBtClientOrderDetailsModel);
                tempRecord.put("reservation_id", vmsBtClientOrderDetailsModel.getReservationId());
                tempRecord.put("reason", vmsBtClientOrderDetailsModel.getCancelReason());
            }
        }

        // 根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList);

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (CollectionUtils.isEmpty(matchModelList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        if (request.getItems().size() > matchModelList.size()) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = "Canceled";

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (!vmsBtClientOrderDetailsModel.getStatus().equals("Canceled")) {
                tempStatus = "";
            }
        }

        // 更新 【品牌方订单一览】vms_bt_client_orders

        if (tempStatus.equals("Canceled")) {
            caClientService.updateClientOrderStatus(channelId, orderID, tempStatus);
        }

        // 消息生成

        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);

        mqMessageMap.put("items", tempRecord);

        mqSender.sendMessage("voyageone_vms_wsdl_mq_cancel_order_queue", mqMessageMap);

        return success();
    }

    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderCancellationRequest not found.");
        }

        String channelId = getClientChannelId();

        //检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, "Shipped");

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound);
        }


        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        //根据请求参数中Items.SellerSku， 匹配品牌方订单明细中的 seller_sku，找出对应的明细。

        Map<String, Object> tempRecord = new HashMap<>();
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();

            boolean hasItem = false;

            String cancelReason = "";
            for (OrderItemCancellationModel orderItemCancellationModel : request.getItems()) {
                if (orderItemCancellationModel.getId().equals(sku)) {
                    hasItem = true;
                    cancelReason = orderItemCancellationModel.getReason().name();
                }
            }

            if (hasItem) {
                vmsBtClientOrderDetailsModel.setCancelReason(cancelReason);
                vmsBtClientOrderDetailsModel.setStatus("Canceled");
                vmsBtClientOrderDetailsModel.setRefundFlg("1");

                matchModelList.add(vmsBtClientOrderDetailsModel);
                tempRecord.put("reservation_id", vmsBtClientOrderDetailsModel.getReservationId());
                tempRecord.put("reason", vmsBtClientOrderDetailsModel.getCancelReason());
            }
        }

        //根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList);

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (CollectionUtils.isEmpty(matchModelList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        if (request.getItems().size() > matchModelList.size()) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量"), ErrorType.OpenAPI, SubSystem.VMS);
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = "Canceled";

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (!vmsBtClientOrderDetailsModel.getStatus().equals("Canceled")) {
                tempStatus = "";
            }
        }

        // 更新 【品牌方订单一览】vms_bt_client_orders

        if (tempStatus.equals("Canceled")) {
            caClientService.updateClientOrderStatus(channelId, orderID, tempStatus);
        }


        // 消息生成

        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);

        mqMessageMap.put("items", tempRecord);

        mqSender.sendMessage("voyageone_vms_wsdl_mq_refund_order_queue", mqMessageMap);

        return success();
    }


}
