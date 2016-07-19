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

        AddToShipmentController.prototype.init = function () {
            var self = this;
            self.summary = self.shipmentDetails;
            self.ScanPopupInitialInfo = {
                "shipmentBean": self.summary.shipment,
                "orderId": self.summary.orderId,
                "curr": self.shipmentPageOption.curr,
                "size": self.shipmentPageOption.size
            };
            self.shipmentScanPopupService.init(self.ScanPopupInitialInfo).then(function (res) {
                console.log(res);
            })
        };

        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            self.barcode = null;

        };
        return AddToShipmentController;
    })());
});