/**
 * @Name:    addNewOrderService.js
 * @Date:    2015/3/21
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');

    omsApp.service ('newOrderService', ['omsSessionStorageType',
        function (omsSessionStorageType) {
            var _ = require ('underscore');

            this.setCurrentOrderInfo = function (value) {
                sessionStorage.setItem (omsSessionStorageType.ORDER_NEW_CUSTOMER_INFO, JSON.stringify (value));
            };

            this.getCurrentOrderInfo = function () {
                if (!_.isUndefined(sessionStorage.getItem (omsSessionStorageType.ORDER_NEW_CUSTOMER_INFO)))
                    return JSON.parse (sessionStorage.getItem (omsSessionStorageType.ORDER_NEW_CUSTOMER_INFO));
                else
                    return null;
            };

            this.setOrderBeforePageUrl = function (value) {
                sessionStorage.setItem (omsSessionStorageType.ORDER_NEW_BEFORE_PAGE_URL, value);
            };

            this.getOrderBeforePageUrl = function () {
                if (!_.isUndefined(sessionStorage.getItem (omsSessionStorageType.ORDER_NEW_BEFORE_PAGE_URL)))
                    return sessionStorage.getItem (omsSessionStorageType.ORDER_NEW_BEFORE_PAGE_URL);
                else
                    return null;
            };
        }]);
});
