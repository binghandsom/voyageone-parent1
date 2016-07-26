/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentDetailController', (function () {

        function ShipmentDetailController($routeParams, notify, alert, confirm, shipmentDetailService) {
            this.shipmentId = $routeParams['shipmentId'];
            this.notify = notify;
            this.alert = alert;
            this.confirm = confirm;
            this.shipmentDetailService = shipmentDetailService;
        }

        ShipmentDetailController.prototype.init = function () {
            var self = this;
            self.shipmentDetailService.init(this.shipmentId)
        };

        ShipmentDetailController.prototype.back = function () {
            window.location.href = "#/shipment/shipment_info";
        };

        return ShipmentDetailController;
    })());
});