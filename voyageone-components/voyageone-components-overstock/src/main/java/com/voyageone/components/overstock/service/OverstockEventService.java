package com.voyageone.components.overstock.service;

import com.overstock.mp.mpc.externalclient.api.ErrorDetails;
import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.EventStatusType;
import com.overstock.mp.mpc.externalclient.model.EventType;
import com.overstock.mp.mpc.externalclient.model.EventTypeType;
import com.overstock.mp.mpc.externalclient.model.EventsType;
import com.voyageone.components.overstock.OverstockBase;
import com.voyageone.components.overstock.bean.event.OverstockEventTypeUpdateRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aooer 2016/6/8.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class OverstockEventService extends OverstockBase {


    /**
     * 修改event状态
     * @param request 请求体
     * @return 结果
     * @throws Exception
     */
    @Retryable
    public Result<EventType> updatingEventStatus(OverstockEventTypeUpdateRequest request) throws Exception {
        final EventType event = new EventType();
        event.setStatus(request.getEventStatusType());
        return getClientFactory().forEvents()
                .patch(request.getEventId(), event)
                .execute(getCredentials());
    }

    /**
     * 获取发货信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewShipment() throws Exception {
        return queryingForNewEvents(EventTypeType.SHIPMENT);
    }

    /**
     * 获取BO 订单信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewOrderLineCanceled() throws Exception {
        return queryingForNewEvents(EventTypeType.ORDER_LINE_CANCELED);
    }

    /**
     * 获取增量库存信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewVariationInventory() throws Exception {
        return queryingForNewEvents(EventTypeType.VARIATION_INVENTORY);
    }

    /**
     * 获取event详细信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventType> queryingForEventDetail(String eventId) throws Exception {
        return getClientFactory().forEvents()
                .getOne(eventId)
                .withExpand("detail")
                .build()
                .execute(getCredentials());
    }

    /**
     * 获取产品变化信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewVariation() throws Exception {
        return queryingForNewEvents(EventTypeType.VARIATION);
    }

    /**
     * 获取新产品追加信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewVariationNew() throws Exception {
        return queryingForNewEvents(EventTypeType.VARIATION_NEW);
    }

    @Retryable
    public Result<EventsType> queryingForNewProductNew() throws Exception {
        return queryingForNewEvents(EventTypeType.PRODUCT);
    }


    /**
     * 获取价格变化信息
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewVariationPrice() throws Exception {
        return queryingForNewEvents(EventTypeType.VARIATION_PRICE);
    }

    /**
     * query new Order Line Credit
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewOrderLineCredit() throws Exception {
        return queryingForNewEvents(EventTypeType.ORDER_LINE_CREDIT);
    }

    /**
     * query new return
     *
     * @return 结果集
     * @throws Exception
     */
    @Retryable
    public Result<EventsType> queryingForNewReturn() throws Exception {
        return queryingForNewEvents(EventTypeType.RETURN);
    }

    /**
     * @description OverStock Event 状态更新
     */
    public boolean updateHandledEvents(List<EventType> eventTypeList) throws Exception {
        boolean ret = true;

        OverstockEventTypeUpdateRequest request = new OverstockEventTypeUpdateRequest();

        for (int i = 0; i < eventTypeList.size(); i++) {
            request.setEventId(String.valueOf(eventTypeList.get(i).getId()));
            request.setEventStatusType(EventStatusType.ACKNOWLEDGED);
            Result<EventType> result = updatingEventStatus(request);

            int statusCode = result.getStatusCode();
            ErrorDetails errMsg = result.getErrorDetails();

            if (statusCode != 200) {
                ret = false;
                logger.warn("OverStock updatingEventStatus 调用异常 event_id = " + eventTypeList.get(i).getId() + " statusCode = " + statusCode + " msg = " + errMsg.getErrorMessage());
            }
        }

        return ret;
    }


}
