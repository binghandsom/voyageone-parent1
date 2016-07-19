/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {
        function AddToShipmentController(context, shipmentScanPopupService) {
            this.shipmentDetails = context.ScanPopupInitialInfo;
            this.shipmentSkuList = context.scannedSkuList;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.shipmentPageOption = {curr: 1, total: 0, size: 10};
        }


        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            self.barcode = null;

        };
        return AddToShipmentController;
    })());
});