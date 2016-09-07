package com.voyageone.web2.openapi.channeladvisor.constants;

/**
 * 定义所有 BI API 的地址定义
 *
 * @author chuanyu.liang, 16/08/25
 * @version 2.0.0
 * @since 2.0.0
 */
public interface UrlConstants {

    String ROOT = "/rest/channeladvisor";

    interface PRODUCTS {
        String GET_PRODUCTS = "products";
        String UPDATE_PRODUCTS = "products";
        String UPDATE_QUANTITY_PRICE = "products/quantityprice";
        String UPDATE_STATUS = "products/status";
    }

    interface ORDERS {
        String GET_ORDERS = "orders";
        String GET_ORDER = "orders/{id}";
        String ACKNOWLEDGE_ORDER = "orders/{id}/acknowledge";
        String SHIP_ORDER = "orders/{id}/ship";
        String CANCEL_ORDER = "orders/{id}/cancel";
        String REFUND_ORDER = "orders/{id}/refund";
    }
}
