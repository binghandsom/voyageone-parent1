/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('PopupShipmentController', (function () {
        function NewShipmentController(orderInfoController) {
            this.orderInfoController = orderInfoController;
        }

        NewShipmentController.prototype = function () {

        };
        return NewShipmentController;
    })());
});