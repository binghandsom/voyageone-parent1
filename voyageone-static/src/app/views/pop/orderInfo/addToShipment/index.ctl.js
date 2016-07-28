/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {
        function AddToShipmentController(context, notify, shipmentScanPopupService, $uibModalInstance) {
            this.notify = notify;
            this.shipmentDetails = context.scanPopupInitialInfo;
            this.scannedSkuList = context.scannedSkuList;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            var req = {
                "barcode": barcode,
                "shipment": self.shipmentDetails.shipment,
                "consolidationOrderId": self.shipmentDetails.consolidationOrderId
            };
            self.barcode = null;
            self.shipmentScanPopupService.scanBarcode(req).then(function (data) {
                if (data.success == 1) self.notify.success('TXT_SUCCESS');
                else if (data.success == 0) {

                    self.notify.warning('TXT_ITEM_NOT_FOUND');
                }
                self.scannedSkuList = data.scannedSkuList;
                if (data.finished) {
                    self.notify.success('TXT_COMPLETED');
                    self.$uibModalInstance.close(data.finished);
                }
            })
        };
        return AddToShipmentController;
    })());
});