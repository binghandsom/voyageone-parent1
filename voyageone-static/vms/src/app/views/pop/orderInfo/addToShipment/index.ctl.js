/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms',
    'directives/angularBarcode.directive'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {

        function AddToShipmentController(context, notify, shipmentScanPopupService, $uibModalInstance) {
            this.notify = notify;
            this.shipmentDetails = context.scanPopupInitialInfo;
            this.scannedSkuList = context.scannedSkuList;
            this.waitingSkuList = context.waitingSkuList;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.$uibModalInstance = $uibModalInstance;
            setTimeout("angular.element(document.getElementsByName('barcodeInputBar')).focus()", 1500)
        }

        AddToShipmentController.prototype.scan = function (barcode) {
            if (!barcode) return;
            var self = this;
            var req = {
                "barcode": barcode,
                "shipment": self.shipmentDetails.shipment,
                "consolidationOrderId": self.shipmentDetails.consolidationOrderId
            };
            self.barcode = null;
            self.shipmentScanPopupService.scanBarcode(req).then(function (data) {
                if (data.success == 1) {
                    self.audioPlay(true);
                    self.notify.success('TXT_SUCCESS');
                }
                else if (data.success == 0) {
                    try {
                        self.audioPlay(false);
                    } catch (exception) {
                    }
                    self.notify.warning('TXT_ITEM_NOT_FOUND');
                }
                self.scannedSkuList = data.scannedSkuList;
                self.waitingSkuList = data.waitingSkuList;
                if (data.finished) {
                    self.notify.success('TXT_COMPLETED');
                    self.$uibModalInstance.close(data.finished);
                }
            });
            self.focusOnScanBar();
        };

        AddToShipmentController.prototype.focusOnScanBar = function () {
            angular.element(document.getElementsByName('barcodeInputBar')).focus();
        };

        AddToShipmentController.prototype.audioPlay = function (value) {
            if (value == true) {
                var audioEleSuccess = document.getElementById('successAudio');
                audioEleSuccess.play();
            } else {
                var audioEleWarning = document.getElementById('warningAudio');
                audioEleWarning.play();
            }
        };

        AddToShipmentController.prototype.scanKeyUp = function (event) {
            var self = this;
            if (event.keyCode == 13) {
                self.scan(self.barcode);
            }

        };

        AddToShipmentController.prototype.printLabel = function () {
            var canvas = $('#label').find('canvas').get(0);
            var popupWin = window.open('', '_blank', 'width=300,height=300');
            popupWin.document.open();
            var img = canvas.toDataURL("image/png");
            popupWin.document.write('<img src="'+img+'"/>');
            popupWin.document.close();
            popupWin.print();
            popupWin.close();
        };

        return AddToShipmentController;
    })());
});