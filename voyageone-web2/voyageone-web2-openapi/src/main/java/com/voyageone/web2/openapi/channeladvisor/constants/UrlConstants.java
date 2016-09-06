package com.voyageone.web2.openapi.channeladvisor.constants;

/**
 * 定义所有 BI API 的地址定义
 *
 * @author chuanyu.liang, 16/08/25
 * @version 2.0.0
 * @since 2.0.0
 */
public interface UrlConstants {

    interface PRODUCTS {
        String ROOT = "/rest/channeladvisor";
        String GET_PRODUCTS = "products";
        String UPDATE_PRODUCTS = "products";
        String UPDATE_QUANTITY_PRICE = "products/quantityprice";
        String UPDATE_STATUS = "products/status";
    }
}
