/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('NewShipmentController', (function () {
        function NewShipmentController(context) {

            // 用搜索条件中的选项
            this.searchOrderStatus = context;
        }

        NewShipmentController.prototype = function () {

        };
        return NewShipmentController;
    })());
});