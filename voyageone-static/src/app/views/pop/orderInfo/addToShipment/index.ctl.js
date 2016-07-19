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
        }

        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            var scanPopupCheckBarcodeInfo = {
                "barcode": barcode,
                "shipment": self.shipmentDetails.shipment,
                "orderId": self.shipmentDetails.orderId
            };
            self.barcode = null;
            self.shipmentScanPopupService.checkBarcode(scanPopupCheckBarcodeInfo).then(function (res) {
                console.log(res)
            })
        };
        return AddToShipmentController;
    })());
});