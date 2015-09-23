/**
 * @Name:    orderDetailService.js
 * @Date:    2015/3/10
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');

    omsApp.factory ('orderDetailInfoService',
        ['omsSessionStorageType',
            function (omsSessionStorageType) {

                return {
                    setOrderInfoList: function (values) {
                        sessionStorage.setItem (omsSessionStorageType.ORDER_DETAIL_INFO_LIST, JSON.stringify (values));
                    },
                    getOrderInfoList: function () {
                        if (!_.isUndefined (sessionStorage.getItem (omsSessionStorageType.ORDER_DETAIL_INFO_LIST)))
                            return JSON.parse (sessionStorage.getItem (omsSessionStorageType.ORDER_DETAIL_INFO_LIST));
                        else
                            return null;
                    },
                    setOrderBeforePageUrl: function (value) {
                        sessionStorage.setItem (omsSessionStorageType.ORDER_DETAIL_BEFORE_PAGE_URL, value);
                    },
                    getOrderBeforePageUrl: function () {
                        if (!_.isUndefined (sessionStorage.getItem (omsSessionStorageType.ORDER_DETAIL_BEFORE_PAGE_URL)))
                            return sessionStorage.getItem (omsSessionStorageType.ORDER_DETAIL_BEFORE_PAGE_URL);
                        else
                            return null;
                    }
                };
            }]);
});