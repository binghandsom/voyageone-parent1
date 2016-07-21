/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {
        function AddToShipmentController(context, notify, shipmentScanPopupService, $uibModalInstance) {
            this.notify = notify;
            this.shipmentDetails = context.ScanPopupInitialInfo;
            this.scannedSkuList = context.scannedSkuList;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.$uibModalInstance = $uibModalInstance;
            this.warningSound = new Audio('http://developer.mozilla.org/@api/deki/files/2926/=AudioTest_(1).ogg');
            this.warningSound.preload = 'auto';
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
                    self.warningSound.play();
                    self.notify.warning('TXT_ITEM_NOT_FOUND');
                }
                self.scannedSkuList = data.scannedSkuList;
                if (data.finished) {
                    self.notify.success('TXT_COMPLETE');
                    self.$uibModalInstance.close(data.finished);
                }
            })
        };
        return AddToShipmentController;
    })());
});