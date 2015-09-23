/**
 * @Name:    orderSearch.service.js
 * @Date:    2015/5/11
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');

    omsApp.service ('searchOrderService', ['omsSessionStorageType',
        function (omsSessionStorageType) {

            this.setSearchCondition = function (value) {
                sessionStorage.setItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION, JSON.stringify (value));
            };

            this.getSearchCondition = function () {
                if (!_.isUndefined (sessionStorage.getItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION)))
                    return JSON.parse (sessionStorage.getItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION));
                else
                    return null;
            };

            this.setSearchConditionFlag = function (value) {
                sessionStorage.setItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION_FLAG, JSON.stringify (value));
            };

            this.getSearchConditionFlag = function () {
                if (!_.isUndefined (sessionStorage.getItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION_FLAG)))
                    return sessionStorage.getItem (omsSessionStorageType.ORDER_SEARCH_SELECT_CONDITION_FLAG);
                else
                    return null;
            };
        }]);
});
