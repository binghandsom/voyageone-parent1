/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms',
    'directives/angularBarcode.directive'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {

        function AddToShipmentController(context, notify, confirm, shipmentScanPopupService, $uibModalInstance) {
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentDetails = context.scanPopupInitialInfo;
            this.scannedSkuList = context.scannedSkuList;
            this.waitingSkuList = context.waitingSkuList;
            this.shipmentScanPopupService = shipmentScanPopupService;
            this.$uibModalInstance = $uibModalInstance;
            this.finished = context.waitingSkuList.length == 0;
            this.printed = false;
            this.barcodeOpts = {
                width: 32,
                height: 1200,
                displayValue: false,
                fontSize: 96
            };
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
                self.finished = data.finished;
                if (self.finished) self.focusOn('printButton');
                else self.focusOn('barcodeInputBar');
            });
        };

        AddToShipmentController.prototype.finishScanning = function () {
            var self = this;
            var req = {
                "barcode": "",
                "shipment": self.shipmentDetails.shipment,
                "consolidationOrderId": self.shipmentDetails.consolidationOrderId
            };
            self.confirm('TXT_CONFIRM_ORDER_PACKAGED').then(function () {
                self.shipmentScanPopupService.finishScanning(req).then(function (data) {
                    if (data.success > 0) self.$uibModalInstance.close(data.success);
                })
            });
            self.focusOn('barcodeInputBar');
        };

        AddToShipmentController.prototype.revertScanning = function () {
            var self = this;
            var req = {
                "barcode": "",
                "shipment": self.shipmentDetails.shipment,
                "consolidationOrderId": self.shipmentDetails.consolidationOrderId
            };
            self.confirm('TXT_CONFIRM_REVERT_SCANNED_SKU').then(function () {
                self.shipmentScanPopupService.revertScanning(req).then(function (data) {
                    if (data.success > 0) self.$uibModalInstance.close(data.success);
                });
            });
            self.focusOn('barcodeInputBar');
        };

        AddToShipmentController.prototype.focusOn = function (elementName) {
            angular.element(document.getElementsByName(elementName)).focus();
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
            var self = this;
            var canvas = $('#label').find('canvas').get(0);
            var popupWin = window.open('', '_blank', 'width=600,height=300');
            popupWin.document.open();
            var img = canvas.toDataURL("image/png");
            popupWin.document.write('<div style="float: left;border: solid 1px black;padding: 2px;margin: 2px;"><div>' +
                '<strong style="font-size: 20px;">[Order  No.] ' + self.shipmentDetails.consolidationOrderId+'</strong></div>' +
                '<hr><img width="300px" height="150px" src="'+img+'"/></div>');
            popupWin.document.close();
            popupWin.print();
            popupWin.close();
            self.printed = true;
            self.focusOn('finishButton');
        };

        return AddToShipmentController;
    })());
});