package com.voyageone.web2.openapi.channeladvisor.service.impl;

import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.vms.channeladvisor.enums.CancellationReasonEnum;
import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.service.bean.vms.channeladvisor.enums.OrderStatusEnum;
import com.voyageone.service.bean.vms.channeladvisor.order.*;
import com.voyageone.service.bean.vms.channeladvisor.request.OrderCancellationRequest;
import com.voyageone.service.bean.vms.channeladvisor.request.ShipRequest;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.service.impl.vms.order.CAClientService;
import com.voyageone.service.model.vms.VmsBtClientOrderDetailsModel;
import com.voyageone.service.model.vms.VmsBtClientOrdersModel;
import com.voyageone.web2.openapi.channeladvisor.CAOpenApiBaseService;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiExceptions;
import com.voyageone.web2.openapi.channeladvisor.service.CAOrderService;
import org.apache.commons.collections.MapUtils;
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

    private static final String AcknowledgedBySeller = "AcknowledgedBySeller";

    private static final String PartiallyShipped = "PartiallyShipped";

    private static final String Shipped = "Shipped";

    private static final String Canceled = "Canceled";

    @Autowired
    private CAClientService caClientService;

    @Autowired
    private MqSender mqSender;

    @Autowired
    private IssueLog issueLog;

    public ActionResponse getOrders(String status, String limit) {
        String channelId = getClientChannelId();

        //校验limit
        if (!StringUtils.isEmpty(limit) && !StringUtils.isDigit(limit)) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The value for the limit is not valid.");
        }

        //根据CA请求参数【status】、【limit】， 检索【品牌方订单一览】vms_bt_client_orders

        List<VmsBtClientOrdersModel> ordersModels = caClientService.getClientOrderList(channelId, status, limit);

        //如检索的订单件数为0，无需检索订单明细信息。直接空数组返回。
        if (CollectionUtils.isEmpty(ordersModels)) {
            //throw new CAApiException(ErrorIDEnum.InvalidOrderStatus);
            return success(new ArrayList<OrderModel>());
        }


        List<String> orderIds = ordersModels.stream().map(VmsBtClientOrdersModel::getClientOrderId).collect(Collectors.toList());

        //根据取得的client_order_id，检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsGroupModel> orderDetailsModels = caClientService.getClientOrderDetailList(channelId, orderIds);

        List<OrderModel> responseBody = new ArrayList<>();

        //将取得的订单明细，按照client_order_id，放入取得的订单信息中
        for (VmsBtClientOrdersModel m : ordersModels) {
            OrderModel orderModel = new OrderModel();
            orderModel.setId(m.getClientOrderId());
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
            //shiping
            OrderAddressModel shipAddressModel = new OrderAddressModel();
            shipAddressModel.setAddressLine1(m.getShippingAddressLine1());
            shipAddressModel.setAddressLine2(m.getShippingAddressLine2());
            shipAddressModel.setCity(m.getShippingCity());
            shipAddressModel.setCompanyName(m.getShippingCompanyName());
            shipAddressModel.setCountry(m.getShippingCountry());
            shipAddressModel.setDaytimePhone(m.getShippingDaytimePhone());
            shipAddressModel.setEmailAddress(m.getShippingEmailAddress());
            shipAddressModel.setEveningPhone(m.getShippingEveningPhone());
            shipAddressModel.setFirstName(m.getShippingFirstName());
            shipAddressModel.setLastName(m.getShippingLastName());
            shipAddressModel.setNameSuffix(m.getShippingNameSuffix());
            shipAddressModel.setPostalCode(m.getShippingPostalCode());
            shipAddressModel.setStateOrProvince(m.getShippingStateOrProvince());
            orderModel.setShippingAddress(shipAddressModel);
            orderModel.setShippingLabelURL(m.getShippingLabelUrl());
            orderModel.setCurrency(m.getCurrency());
            orderModel.setDeliverByDateUtc(m.getDeliverByDate());

            List<OrderItemModel> orderItemsModes = new ArrayList<>();
            for (VmsBtClientOrderDetailsGroupModel orderDetailsModel : orderDetailsModels) {
                if (m.getClientOrderId().equals(orderDetailsModel.getClientOrderId())) {
                    OrderItemModel orderItemModel = new OrderItemModel();
                    orderItemModel.setId(orderDetailsModel.getOrderItemId());
                    orderItemModel.setQuantity(orderDetailsModel.getQuantity());
                    orderItemModel.setSellerSku(orderDetailsModel.getSellerSku());
                    orderItemModel.setUnitPrice(orderDetailsModel.getUnitPrice());

                    orderItemsModes.add(orderItemModel);
                }
            }

            orderModel.setItems(orderItemsModes);
            orderModel.setOrderDateUtc(m.getOrderDate());
            orderModel.setOrderStatus(OrderStatusEnum.getInstance(m.getOrderStatus()));
            orderModel.setRequestedShippingMethod(m.getRequestedShippingMethod());
            orderModel.setOtherFees(m.getOtherFees());
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
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID in request is missing.");
        }

        String channelId = getClientChannelId();

        //请求参数【订单ID】不为空的场合，检索【品牌方订单一览】vms_bt_client_orders

        VmsBtClientOrdersModel m = caClientService.getClientOrderById(channelId, orderID);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (m == null) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
        }


        //取得的client_order_id，检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsGroupModel> orderDetailsModels = caClientService.getClientOrderDetailList(channelId, Collections.singletonList(m.getClientOrderId()));

        //将取得的订单明细，按照client_order_id，放入取得的订单信息中

        OrderModel orderModel = new OrderModel();
        orderModel.setId(m.getClientOrderId());
        //buyerOrderAddress
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
        //buyerOrderAddress
        OrderAddressModel shipAddressModel = new OrderAddressModel();
        shipAddressModel.setAddressLine1(m.getShippingAddressLine1());
        shipAddressModel.setAddressLine2(m.getShippingAddressLine2());
        shipAddressModel.setCity(m.getShippingCity());
        shipAddressModel.setCompanyName(m.getShippingCompanyName());
        shipAddressModel.setCountry(m.getShippingCountry());
        shipAddressModel.setDaytimePhone(m.getShippingDaytimePhone());
        shipAddressModel.setEmailAddress(m.getShippingEmailAddress());
        shipAddressModel.setEveningPhone(m.getShippingEveningPhone());
        shipAddressModel.setFirstName(m.getShippingFirstName());
        shipAddressModel.setLastName(m.getShippingLastName());
        shipAddressModel.setNameSuffix(m.getShippingNameSuffix());
        shipAddressModel.setPostalCode(m.getShippingPostalCode());
        shipAddressModel.setStateOrProvince(m.getShippingStateOrProvince());
        orderModel.setShippingAddress(shipAddressModel);
        orderModel.setShippingLabelURL(m.getShippingLabelUrl());
        orderModel.setCurrency(m.getCurrency());
        orderModel.setDeliverByDateUtc(m.getDeliverByDate());

        List<OrderItemModel> orderItemsModes = new ArrayList<>();
        for (VmsBtClientOrderDetailsGroupModel orderDetailsModel : orderDetailsModels) {
            if (m.getClientOrderId().equals(orderDetailsModel.getClientOrderId())) {
                OrderItemModel orderItemModel = new OrderItemModel();
                orderItemModel.setId(orderDetailsModel.getOrderItemId());
                orderItemModel.setQuantity(orderDetailsModel.getQuantity());
                orderItemModel.setSellerSku(orderDetailsModel.getSellerSku());
                orderItemModel.setUnitPrice(orderDetailsModel.getUnitPrice());

                orderItemsModes.add(orderItemModel);
            }
        }

        orderModel.setItems(orderItemsModes);
        orderModel.setOrderDateUtc(m.getOrderDate());
        orderModel.setOrderStatus(OrderStatusEnum.getInstance(m.getOrderStatus()));
        orderModel.setRequestedShippingMethod(m.getRequestedShippingMethod());
        orderModel.setOtherFees(m.getOtherFees());
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

    @VOTransactional
    public ActionResponse acknowledgeOrder(String orderID) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderID in request is missing.");
        }

        String channelId = getClientChannelId();

        //检索【品牌方订单一览】vms_bt_client_orders

        VmsBtClientOrdersModel m = caClientService.getClientOrderById(channelId, orderID);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (m == null) {
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
        }

        //判断订单状态
        if (!StringUtils.isEmpty(m.getOrderStatus())) {
            /*Pending(1),ReleasedForShipment(2),AcknowledgedBySeller(3),PartiallyShipped(4),Shipped(5),Canceled(6);*/
            /*if (m.getOrderStatus().equals(Canceled) || m.getOrderStatus().equals(Shipped)) {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus);
            }*/
            if (!m.getOrderStatus().equals("ReleasedForShipment")) {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus,
                        "Status is " + m.getOrderStatus() + ", and this is not a valid status option for this call.");
            }
        }
        // 更新【品牌方订单一览】vms_bt_client_orders

        caClientService.updateClientOrderStatusWithDetails(channelId, orderID, AcknowledgedBySeller, "acknowledgeOrder", true);

        //消息生成
        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("client_order_id", orderID);
        mqMessageMap.put("order_channel_id", channelId);

        logger.info("发送mq消息：" + JacksonUtil.bean2Json(mqMessageMap));

        mqSender.sendMessage("voyageone_vms_wsdl_mq_acknowledge_order_queue", mqMessageMap);

        return success();
    }

    @VOTransactional
    public ActionResponse shipOrder(String orderID, ShipRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderID in request is missing.");
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Request body is missing.");
        }

        if (StringUtils.isEmpty(request.getTrackingNumber())) {
            throw new CAApiException(ErrorIDEnum.InvalidTrackingNumber, "TrackingNumber is not provided.");
        }
        if (StringUtils.isEmpty(request.getShippingCarrier())) {
            throw new CAApiException(ErrorIDEnum.InvalidShippingCarrier, "ShippingCarrier is not provided.");
        }
        if (StringUtils.isEmpty(request.getShippingClass())) {
            throw new CAApiException(ErrorIDEnum.InvalidShippingClass, "ShippingClass is not provided.");
        }
        if (MapUtils.isEmpty(request.getItems())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Items is not provided.");
        }
        if (null == request.getShippedDateUtc()) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "ShippedDateUtc is not provided.");
        }

        List<CAApiException> exceptionList = new ArrayList<>();
        for (Map.Entry<String, Integer> kv : request.getItems().entrySet()) {
            if (StringUtils.isEmpty(kv.getKey())) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SellerSku is missing."));
                continue;
            }
            if (kv.getValue() == null) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity of SellerSku=" + kv.getKey() + " is missing."));
                continue;
            }
            if (kv.getValue() <= 0) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity of SellerSku=" + kv.getKey() + " is invalid."));
            }
        }
        if (!CollectionUtils.isEmpty(exceptionList)) {
            throw new CAApiExceptions(exceptionList);
        }

        String channelId = getClientChannelId();

        // 检索订单是否存在
        VmsBtClientOrdersModel vmsBtClientOrdersModel = caClientService.getClientOrderById(channelId, orderID);
        if (vmsBtClientOrdersModel != null) {
            //校验状态是否正确
            if (!vmsBtClientOrdersModel.getOrderStatus().equals(AcknowledgedBySeller)) {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus,
                        "OrderId=" + orderID + " status is " + vmsBtClientOrdersModel.getOrderStatus() + " which is invalid.");
            }
        } else {
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, AcknowledgedBySeller);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            //查订单是否存在
            List<VmsBtClientOrderDetailsModel> mOrderList = caClientService.getClientOrderDetailById(channelId, orderID, null);
            if (CollectionUtils.isEmpty(mOrderList)) {
                throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
            } else {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus, "OrderId=" + orderID + " status is " + mOrderList.get(0).getStatus() + " which is invalid.");
            }
        }

        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        //根据请求参数中Items.SellerSku， 匹配 品牌方订单明细中的 seller_sku，找出对应的明细。
        List<Long> tempReservationId = new ArrayList<>();

        Map<String, Integer> tempSkuQtyMap = request.getItems();
        Set<String> issueSkuNotExistList = new HashSet<>(tempSkuQtyMap.keySet());
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();
            if (tempSkuQtyMap.get(sku) != null && tempSkuQtyMap.get(sku) > 0) {
                tempSkuQtyMap.put(sku, tempSkuQtyMap.get(sku) - 1);
                issueSkuNotExistList.remove(sku);

                VmsBtClientOrderDetailsModel tempUpdateModel = new VmsBtClientOrderDetailsModel();
                tempUpdateModel.setShippedDate(request.getShippedDateUtc());
                tempUpdateModel.setTrackingNumber(request.getTrackingNumber());
                tempUpdateModel.setShippingCarrier(request.getShippingCarrier());
                tempUpdateModel.setShippingClass(request.getShippingClass());
                tempUpdateModel.setStatus(Shipped);
                tempUpdateModel.setId(vmsBtClientOrderDetailsModel.getId());

                matchModelList.add(tempUpdateModel);
                tempReservationId.add(vmsBtClientOrderDetailsModel.getReservationId());
            }
        }

        //根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList, "shipOrder");

        //全部sku不存在
        if (tempSkuQtyMap.keySet().size() == issueSkuNotExistList.size()) {
            tempSkuQtyMap.keySet().stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                        "SellerSku=" + key + " is not provided."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
            /*throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "SellerSku=[" + tempSkuQtyMap.keySet().stream().reduce((a, b) -> a + "," + b).get() + "] is not provided.");*/
        }

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (!CollectionUtils.isEmpty(issueSkuNotExistList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在 请求订单号：orderId=" + orderID +
                    " skus=" + JacksonUtil.bean2Json(issueSkuNotExistList)), ErrorType.OpenAPI, SubSystem.VMS);

            issueSkuNotExistList.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                        "SellerSku=" + key + " is not provided."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
            /*throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "SellerSku=[" + issueSkuNotExistList.stream().reduce((a, b) -> a + "," + b).get() + "] is not provided.");*/
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        List<String> issueSkuQtyNotComplete = tempSkuQtyMap.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(issueSkuQtyNotComplete)) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量 请求订单号：orderId=" + orderID +
                    " skus=" + JacksonUtil.bean2Json(issueSkuQtyNotComplete)), ErrorType.OpenAPI, SubSystem.VMS);
            issueSkuQtyNotComplete.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                        "The quantity of SellerSku=" + key + " is invalid."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
            /*throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "The quantity of SellerSku=[" + issueSkuQtyNotComplete.stream().reduce((a, b) -> a + "," + b).get() + "] is invalid.");*/
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = Shipped;

        boolean hasShipped = false;
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (vmsBtClientOrderDetailsModel.getStatus().equals(Shipped)) {
                hasShipped = true;
            } else {
                tempStatus = PartiallyShipped;
            }
        }

        if (hasShipped) {
            // 更新 【品牌方订单一览】vms_bt_client_orders
            caClientService.updateClientOrderStatusWithDetails(channelId, orderID, tempStatus, "shipOrder", false);
        } else {
            logger.warn("未找到shipped订单明细");
        }

        // 消息生成
        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);
        mqMessageMap.put("shipped_date", DateTimeUtil.format(request.getShippedDateUtc(), null));
        mqMessageMap.put("tracking_number", request.getTrackingNumber());
        mqMessageMap.put("shipping_carrier", request.getShippingCarrier());
        mqMessageMap.put("shipping_class", request.getShippingClass());

/*        String reservationIds = "";

        for (Long aLong : tempReservationId) {
            reservationIds += aLong + ",";
        }*/

        mqMessageMap.put("items", tempReservationId);

        Map<String, Object> mqMessageBody = new HashMap<>();
        mqMessageBody.put("body", JacksonUtil.bean2Json(mqMessageMap));

        logger.info("发送mq消息：" + JacksonUtil.bean2Json(mqMessageBody));

        mqSender.sendMessage("voyageone_vms_wsdl_mq_ship_order_queue", mqMessageBody);

        return success();
    }

    @VOTransactional
    public ActionResponse cancelOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderID in request is missing.");
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Request body is missing.");
        }

        if (StringUtils.isEmpty(request.getOrderId())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "OrderID is not provided.");
        }

        if (!orderID.equals(request.getOrderId())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "OrderID is inconsistent.");
        }

        if (CollectionUtils.isEmpty(request.getItems())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Items in the cancel request are not provided.");
        }

        List<CAApiException> exceptionList = new ArrayList<>();
        for (OrderItemCancellationModel item : request.getItems()) {
            if (StringUtils.isEmpty(item.getId())) {
                if (StringUtils.isEmpty(item.getSellerSku())) {
                    exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SkuID and SellerSku are not provided."));
                } else {
                    exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SkuID of SellerSku=" + item.getSellerSku() + " is not provided."));
                }
                continue;
            }
            if (StringUtils.isEmpty(item.getSellerSku())) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SellerSku of SkuID=" + item.getId() + " is not provided."));
                continue;
            }
            /*if (!item.getId().equals(item.getSellerSku())) {
                throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Cannot find SellerSku=" + item.getSellerSku() + " with id=" + item.getId()+".");
            }*/
            if (item.getQuantity() == null) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Quantity of SellerSku=" + item.getSellerSku() + " is not provided."));
                continue;
            }
            if (item.getQuantity() <= 0) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity of SellerSku=" + item.getSellerSku() + " is invalid."));
                continue;
            }

            try {
                if (!StringUtils.isEmpty(item.getReason())) {
                    CancellationReasonEnum.valueOf(item.getReason());
                }
            } catch (Exception ex) {
                exceptionList.add(new CAApiException(ErrorIDEnum.UnsupportedCancellationReason, "Reason of SellerSku=" + item.getSellerSku() + " is invalid."));
            }
        }
        if (!CollectionUtils.isEmpty(exceptionList)) {
            throw new CAApiExceptions(exceptionList);
        }

        String channelId = getClientChannelId();

        // 检索订单是否存在
        VmsBtClientOrdersModel vmsBtClientOrdersModel = caClientService.getClientOrderById(channelId, orderID);
        if (vmsBtClientOrdersModel != null) {
            //校验状态是否正确
            if (!vmsBtClientOrdersModel.getOrderStatus().equals(AcknowledgedBySeller)) {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus,
                        "OrderID=" + orderID + " status " + vmsBtClientOrdersModel.getOrderStatus() + " is not valid for this request.");
            }
        } else {
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, AcknowledgedBySeller);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            //查订单是否存在
            List<VmsBtClientOrderDetailsModel> mOrderList = caClientService.getClientOrderDetailById(channelId, orderID, null);
            if (CollectionUtils.isEmpty(mOrderList)) {
                throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
            } else {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus, "Status " + mOrderList.get(0).getStatus() + " is not valid for this request.");
            }
        }

        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        // 根据请求参数中Items.SellerSku， 匹配品牌方订单明细中的 seller_sku，找出对应的明细。

        List<Map<String, Object>> tempRecords = new ArrayList<>();

        //Map<String, Integer> tempSkuQtyMap = request.getItems().stream().collect(Collectors.toMap(OrderItemCancellationModel::getSellerSku, OrderItemCancellationModel::getQuantity));
        //Map<String, String> tempSkuReasonMap = request.getItems().stream().collect(Collectors.toMap(OrderItemCancellationModel::getSellerSku, OrderItemCancellationModel::getReason));

        //sku数量map
        Map<String, Integer> tempSkuQtyMap = new HashMap<>();
        request.getItems().forEach(m -> {
            Integer already = tempSkuQtyMap.get(m.getSellerSku());
            Integer current = m.getQuantity();
            if (already != null && current != null) {
                tempSkuQtyMap.put(m.getSellerSku(), already + current);
            } else if (already == null && current != null) {
                tempSkuQtyMap.put(m.getSellerSku(), current);
            }
        });

        Set<String> issueSkuNotExistList = new HashSet<>(tempSkuQtyMap.keySet());

        //请求items
        List<OrderItemCancellationModel> tempItems = new ArrayList<>(request.getItems());

        //校验order_item_id和seller_sku是否一致
        for (OrderItemCancellationModel m : tempItems) {
            boolean validate = false;
            for (VmsBtClientOrderDetailsModel n : mList) {
                if (n.getSellerSku().equals(m.getSellerSku()) && n.getOrderItemId().equals(m.getId())) {
                    validate = true;
                    break;
                }
            }
            if (!validate) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter
                        , "Cannot find SellerSku=" + m.getSellerSku() + " with id=" + m.getId()));
            }
        }
        if (!CollectionUtils.isEmpty(exceptionList)) {
            throw new CAApiExceptions(exceptionList);
        }

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();

            if (tempSkuQtyMap.get(sku) != null && tempSkuQtyMap.get(sku) > 0) {
                tempSkuQtyMap.put(sku, tempSkuQtyMap.get(sku) - 1);
                issueSkuNotExistList.remove(sku);

                //从requestItems里边寻找相应数据
                int matchIndex = -1;
                for (int i = 0; i < tempItems.size(); i++) {
                    OrderItemCancellationModel item = tempItems.get(i);
                    //仅匹配sku，若匹配到，后续处理
                    if (item.getSellerSku().equals(vmsBtClientOrderDetailsModel.getSellerSku())) {
                        matchIndex = i;
                        VmsBtClientOrderDetailsModel tempUpdateModel = new VmsBtClientOrderDetailsModel();
                        String tempSkuReasonStr = item.getReason();
                        if (!StringUtils.isEmpty(tempSkuReasonStr)) {
                            tempUpdateModel.setCancelReason(CancellationReasonEnum.valueOf(tempSkuReasonStr).name());
                        }
                        tempUpdateModel.setStatus(Canceled);
                        tempUpdateModel.setId(vmsBtClientOrderDetailsModel.getId());
                        matchModelList.add(tempUpdateModel);
                        Map<String, Object> tempRecord = new HashMap<>();
                        tempRecord.put("reservation_id", vmsBtClientOrderDetailsModel.getReservationId());
                        tempRecord.put("reason", tempUpdateModel.getCancelReason());
                        tempRecords.add(tempRecord);
                        break;
                    }
                }
                /*if (matchIndex != -1)
                    tempItems.remove(matchIndex);*/
            }
        }

        // 根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList, "cancelOrder");

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (!CollectionUtils.isEmpty(issueSkuNotExistList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在 请求订单号：orderId=" + orderID +
                    " skuList=" + JacksonUtil.bean2Json(issueSkuNotExistList)), ErrorType.OpenAPI, SubSystem.VMS);
           /* throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "SellerSku=[" + issueSkuNotExistList.stream().reduce((a, b) -> a + "," + b).get() + "] is not provided.");*/

            issueSkuNotExistList.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SellerSku=" + key + " is not provided."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        List<String> issueSkuQtyNotComplete = tempSkuQtyMap.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(issueSkuQtyNotComplete)) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量 请求订单号：orderId=" + orderID +
                            " skus=" + JacksonUtil.bean2Json(issueSkuQtyNotComplete)),
                    ErrorType.OpenAPI, SubSystem.VMS);
           /* throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "The quantity of SellerSku=[" + issueSkuQtyNotComplete.stream().reduce((a, b) -> a + "," + b).get() + "] is invalid.");
*/
            issueSkuQtyNotComplete.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity of SellerSku=" + key + " is invalid."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = Canceled;

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (!vmsBtClientOrderDetailsModel.getStatus().equals(Canceled)) {
                tempStatus = "";
            }
        }

        // 更新 【品牌方订单一览】vms_bt_client_orders

        if (tempStatus.equals(Canceled)) {
            caClientService.updateClientOrderStatusWithDetails(channelId, orderID, tempStatus, "cancelOrder", false);
        }

        // 消息生成

        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);

        mqMessageMap.put("items", tempRecords);

        Map<String, Object> mqMessageBody = new HashMap<>();
        mqMessageBody.put("body", JacksonUtil.bean2Json(mqMessageMap));

        logger.info("发送mq消息：" + JacksonUtil.bean2Json(mqMessageBody));

        mqSender.sendMessage("voyageone_vms_wsdl_mq_cancel_order_queue", mqMessageBody);

        return success();
    }

    @VOTransactional
    public ActionResponse refundOrder(String orderID, OrderCancellationRequest request) {
        if (StringUtils.isEmpty(orderID)) {
            //If there is no order with that ID, return Error ID 6000 (OrderNotFound)
            throw new CAApiException(ErrorIDEnum.InvalidRequest, "OrderID in request is missing.");
        }

        if (request == null) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Request body is missing.");
        }

        if (StringUtils.isEmpty(request.getOrderId())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "OrderID is not provided.");
        }

        if (!orderID.equals(request.getOrderId())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "OrderID is inconsistent.");
        }

        if (CollectionUtils.isEmpty(request.getItems())) {
            throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Items in the refund request are not provided.");
        }
        List<CAApiException> exceptionList = new ArrayList<>();
        for (OrderItemCancellationModel item : request.getItems()) {
            if (StringUtils.isEmpty(item.getId())) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SkuID of SellerSku=" + item.getSellerSku() + " is not provided."));
                continue;
            }
            if (StringUtils.isEmpty(item.getSellerSku())) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SellerSku of SkuId=" + item.getId() + " is not provided."));
                continue;
            }
            /*if (!item.getId().equals(item.getSellerSku())) {
                throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Cannot find SellerSku=" + item.getSellerSku() + " with id=" + item.getId()+".");
            }*/
            if (item.getQuantity() == null) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "Quantity of SellerSku=" + item.getSellerSku() + " is not provided."));
                continue;
            }
            if (item.getQuantity() <= 0) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity " + item.getQuantity() + " of SellerSku=" + item.getSellerSku() + " is not valid."));
                continue;
            }
            try {
                if (!StringUtils.isEmpty(item.getReason())) {
                    CancellationReasonEnum.valueOf(item.getReason());
                }
            } catch (Exception ex) {
                exceptionList.add(new CAApiException(ErrorIDEnum.UnsupportedRefundReason, "Reason of SellerSku=" + item.getSellerSku() + " is invalid."));
            }
        }
        if (!CollectionUtils.isEmpty(exceptionList)) {
            throw new CAApiExceptions(exceptionList);
        }
        String channelId = getClientChannelId();

        // 检索订单是否存在
        VmsBtClientOrdersModel vmsBtClientOrdersModel = caClientService.getClientOrderById(channelId, orderID);
        if (vmsBtClientOrdersModel != null) {
            //校验状态是否正确
            if (!vmsBtClientOrdersModel.getOrderStatus().equals(Shipped)) {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus,
                        "OrderId=" + orderID + " status is " + vmsBtClientOrdersModel.getOrderStatus() + " which is invalid.");
            }
        } else {
            throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
        }

        //检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> mList = caClientService.getClientOrderDetailById(channelId, orderID, Shipped);

        //检索结果不存在的场合，Error ID 6000 的 异常抛出

        if (mList == null || mList.isEmpty()) {
            //查订单是否存在
            List<VmsBtClientOrderDetailsModel> mOrderList = caClientService.getClientOrderDetailById(channelId, orderID, null);
            if (CollectionUtils.isEmpty(mOrderList)) {
                throw new CAApiException(ErrorIDEnum.OrderNotFound, "OrderID=" + orderID + " is not provided.");
            } else {
                throw new CAApiException(ErrorIDEnum.InvalidOrderStatus,
                        "OrderId=" + orderID + " status is " + mOrderList.get(0).getStatus() + " which is invalid.");
            }
        }


        List<VmsBtClientOrderDetailsModel> matchModelList = new ArrayList<>();

        //根据请求参数中Items.SellerSku， 匹配品牌方订单明细中的 seller_sku，找出对应的明细。
        //Map<String, Integer> tempSkuQtyMap = request.getItems().stream().collect(Collectors.toMap(OrderItemCancellationModel::getSellerSku, OrderItemCancellationModel::getQuantity));
        //Map<String, String> tempSkuReasonMap = request.getItems().stream().collect(Collectors.toMap(OrderItemCancellationModel::getSellerSku, OrderItemCancellationModel::getReason));

        //sku数量map
        Map<String, Integer> tempSkuQtyMap = new HashMap<>();
        request.getItems().forEach(m -> {
            Integer already = tempSkuQtyMap.get(m.getSellerSku());
            Integer current = m.getQuantity();
            if (already != null && current != null) {
                tempSkuQtyMap.put(m.getSellerSku(), already + current);
            } else if (already == null && current != null) {
                tempSkuQtyMap.put(m.getSellerSku(), current);
            }
        });

        List<Map<String, Object>> tempRecords = new ArrayList<>();
        Set<String> issueSkuNotExistList = new HashSet<>(tempSkuQtyMap.keySet());

        //请求items
        List<OrderItemCancellationModel> tempItems = new ArrayList<>(request.getItems());

        //校验order_item_id和seller_sku是否一致
        for (OrderItemCancellationModel m : tempItems) {
            boolean validate = false;
            for (VmsBtClientOrderDetailsModel n : mList) {
                if (n.getSellerSku().equals(m.getSellerSku()) && n.getOrderItemId().equals(m.getId())) {
                    validate = true;
                    break;
                }
            }
            if (!validate) {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter
                        , "Cannot find SellerSku=" + m.getSellerSku() + " with ID=" + m.getId()));
            }
        }
        if (!CollectionUtils.isEmpty(exceptionList)) {
            throw new CAApiExceptions(exceptionList);
        }

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : mList) {
            String sku = vmsBtClientOrderDetailsModel.getSellerSku();
            if (tempSkuQtyMap.get(sku) != null && tempSkuQtyMap.get(sku) > 0) {
                tempSkuQtyMap.put(sku, tempSkuQtyMap.get(sku) - 1);
                issueSkuNotExistList.remove(sku);

                //从requestItems里边寻找相应数据
                int matchIndex = -1;
                for (int i = 0; i < tempItems.size(); i++) {
                    OrderItemCancellationModel item = tempItems.get(i);
                    //仅匹配sku，若匹配到，后续处理
                    if (item.getSellerSku().equals(vmsBtClientOrderDetailsModel.getSellerSku())) {
                        matchIndex = i;
                        VmsBtClientOrderDetailsModel tempUpdateModel = new VmsBtClientOrderDetailsModel();
                        String tempSkuReasonStr = item.getReason();
                        if (!StringUtils.isEmpty(tempSkuReasonStr)) {
                            tempUpdateModel.setCancelReason(CancellationReasonEnum.valueOf(tempSkuReasonStr).name());
                        }
                        tempUpdateModel.setStatus(Canceled);
                        tempUpdateModel.setRefundFlg("1");
                        tempUpdateModel.setId(vmsBtClientOrderDetailsModel.getId());
                        matchModelList.add(tempUpdateModel);
                        Map<String, Object> tempRecord = new HashMap<>();
                        tempRecord.put("reservation_id", vmsBtClientOrderDetailsModel.getReservationId());
                        tempRecord.put("reason", tempUpdateModel.getCancelReason());
                        tempRecords.add(tempRecord);
                        break;
                    }
                }
                /*if (matchIndex != -1)
                    tempItems.remove(matchIndex);*/
            }
        }

        //根据Items.SellerSku对应的件数，更新 对应件数的明细。

        caClientService.updateItemsSkuList(matchModelList, "refundOrder");

        //Items.SellerSku 在品牌方订单明细 中不存在，issuelog 输出，处理继续
        if (!CollectionUtils.isEmpty(issueSkuNotExistList)) {
            issueLog.log(new RuntimeException("Items.SellerSku 在品牌方订单明细 中不存在 请求订单号：orderId=" + orderID +
                            " skus=" + JacksonUtil.bean2Json(issueSkuNotExistList)),
                    ErrorType.OpenAPI, SubSystem.VMS);
            /*throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "SellerSku=[" + issueSkuNotExistList.stream().reduce((a, b) -> a + "," + b).get() + "] is not provided.");*/
            issueSkuNotExistList.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "SellerSku=" + key + " is not provided."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
        }

        //Items.SellerSku 对应的数量 > 对应SKU明细件数的数量，issuelog 输出，处理继续
        List<String> issueSkuQtyNotComplete = tempSkuQtyMap.entrySet().stream().filter(e -> e.getValue() > 0).map(Map.Entry::getKey).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(issueSkuQtyNotComplete)) {
            issueLog.log(new RuntimeException("Items.SellerSku 对应的数量 > 对应SKU明细件数的数量 请求订单号：orderId=" + orderID +
                            " skus=" + JacksonUtil.bean2Json(issueSkuQtyNotComplete)),
                    ErrorType.OpenAPI, SubSystem.VMS);
            /*throw new CAApiException(ErrorIDEnum.InvalidRequiredParameter,
                    "The quantity of SellerSku=[" + issueSkuQtyNotComplete.stream().reduce((a, b) -> a + "," + b).get() + "] is not valid.");*/
            issueSkuQtyNotComplete.stream().forEach(key -> {
                exceptionList.add(new CAApiException(ErrorIDEnum.InvalidRequiredParameter, "The quantity of SellerSku=" + key + " is not valid."));
            });
            if (!CollectionUtils.isEmpty(exceptionList)) {
                throw new CAApiExceptions(exceptionList);
            }
        }

        // 检索【品牌方订单明细】vms_bt_client_order_details

        List<VmsBtClientOrderDetailsModel> newList = caClientService.getClientOrderDetailById(channelId, orderID, null);

        String tempStatus = Canceled;

        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : newList) {
            if (!vmsBtClientOrderDetailsModel.getStatus().equals(Canceled)) {
                tempStatus = "";
            }
        }

        // 更新 【品牌方订单一览】vms_bt_client_orders

        if (tempStatus.equals(Canceled)) {
            caClientService.updateClientOrderStatusWithDetails(channelId, orderID, tempStatus, "refundOrder", false);
        }


        // 消息生成

        Map<String, Object> mqMessageMap = new HashMap<>();
        mqMessageMap.put("order_channel_id", channelId);
        mqMessageMap.put("client_order_id", orderID);

        mqMessageMap.put("items", tempRecords);

        Map<String, Object> mqMessageBody = new HashMap<>();
        mqMessageBody.put("body", JacksonUtil.bean2Json(mqMessageMap));
        logger.info("发送mq消息：" + JacksonUtil.bean2Json(mqMessageBody));

        mqSender.sendMessage("voyageone_vms_wsdl_mq_refund_order_queue", mqMessageBody);

        return success();
    }


}
