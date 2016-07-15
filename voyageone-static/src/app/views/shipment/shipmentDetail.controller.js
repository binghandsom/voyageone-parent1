/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentDetailController', (function () {
        function ShipmentDetailController() {

        }

        ShipmentDetailController.prototype = {
            back: function () {
                window.location.href = "#/shipment/shipment_info";
            }
        };
        return ShipmentDetailController;
    })());
});