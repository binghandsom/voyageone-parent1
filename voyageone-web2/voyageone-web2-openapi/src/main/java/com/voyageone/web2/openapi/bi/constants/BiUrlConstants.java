package com.voyageone.web2.openapi.bi.constants;

/**
 * 定义所有 BI API 的地址定义
 *
 * @author chuanyu.liang, 16/08/25
 * @version 2.0.0
 * @since 2.0.0
 */
public interface BiUrlConstants {

    interface URL {
        interface LIST {
            String ROOT = "/rest/bi/url";
            String CHECK_URL_LIST = "check";
            String SAVE_SHOP_URL_DATA = "saveShopUrlData";
            String SAVE_PRODUCT_FILE_DATA = "saveProductFileData";
        }
    }
}
