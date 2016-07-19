/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {
        function AddToShipmentController(context, shipmentScanPopupService) {
            this.shipmentDetails = context;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.shipmentPageOption = {curr: 1, total: 0, size: 10};
            this.barcode = null;
        }

        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            self.barcode = null;

        };
        return AddToShipmentController;
    })());
});