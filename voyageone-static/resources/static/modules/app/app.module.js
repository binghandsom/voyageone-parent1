/**
 * @Name:    app.module.js
 * @Date:    2015/4/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    require ('components/services/ajax.service');
    require ('components/services/cookie.service');
    require ('components/services/alert.service');
    require ('components/services/language.service');
    require ('components/services/message.service');
    require ('components/services/permission.service');
    require ('components/services/translate.service');

    var mainApp = require ('components/app');

    mainApp.constant ('commonAction', {
        'common_doLogout': '/core/account/login/doLogout',
        'common_doGetUserInfo': '/core/account/login/doGetUserInfo',
        'common_doGetCompany': '/core/account/company/doGetCompany',
        'common_doSelectCompany': '/core/account/company/doSelectCompany',
        'common_doChgPassword':'/core/setting/user/doChgPassword'
    });

    /**
     * 放到sessionStorage的类型.
     */
    mainApp.constant ('omsSessionStorageType', {
        ORDER_DETAIL_INFO_LIST: 'orderDetail.searchOrderInfoList',
        ORDER_DETAIL_BEFORE_PAGE_URL: 'orderDetail.beforePageUrl',
        ORDER_NEW_CUSTOMER_INFO: 'addNewOrder.orderInfo',
        ORDER_NEW_BEFORE_PAGE_URL: 'addNewOrder.beforePageUrl',
        ORDER_SEARCH_SELECT_CONDITION: 'searchOrder.condition',
        ORDER_SEARCH_SELECT_CONDITION_FLAG: 'searchOrder.searchFlag',
        SETTLEMENT_FILE_SEARCH_SELECT_CONDITION: 'searchSettlementFile.condition',
        SETTLEMENT_FILE_SEARCH_SELECT_CONDITION_FLAG: 'searchSettlementFile.searchFlag'
    });

    /**
     * oms的所有popup画面一览.
     */
    mainApp.constant ("appPopupPages", {
        "popBigImage": {
            "page": "/VoyageOne/modules/app/popup/popBigImage.dialog.tpl.html",
            "controller": "popBigImageController"
        }
    });

    return mainApp;
});
