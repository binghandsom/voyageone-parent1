/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentDetailController', (function () {
        function warningAudio(value) {
            var audioEle = document.getElementById('warningAudio');
            if (value == true) audioEle.play();
        }

        function successAudio(value) {
            var audioEle = document.getElementById('successAudio');
            if (value == true) audioEle.play();
        }

        function ShipmentDetailController($routeParams, notify, alert, confirm, shipmentDetailService) {
            this.shipmentId = $routeParams['shipmentId'];
            this.notify = notify;
            this.alert = alert;
            this.confirm = confirm;
            this.shipmentDetailService = shipmentDetailService;

            this.originalShipment = {};
            this.shipmentStatusList = [];
            this.orderStatusList = [];
            this.expressCompanies = [];
            this.scannedSkuList = [];
            this.channelConfigs = {};
            this.barcode = "";
        }

        ShipmentDetailController.prototype.init = function () {
            var self = this;
            self.shipmentDetailService.init(self.shipmentId).then(function (data) {
                self.channelConfigs = data.channelConfigs;
                self.shipmentStatusList = data.shipmentStatusList;
                self.orderStatusList = data.orderStatusList;
                self.expressCompanies = data.expressCompanies;
                self.shipment = data.shipment;
                self.originalShipment = angular.copy(data.shipment);
                if (self.shipment && self.shipment.shippedDate) self.shipment.shippedDate = new Date(self.shipment.shippedDate);
                self.scannedSkuList = data.scannedSkuList;
            })
        };

        ShipmentDetailController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.orderStatusList.length; i++) {
                if (self.orderStatusList[i].value == statusValue) {
                    var currentStatus = self.orderStatusList[i];
                    break;
                }
            }
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        ShipmentDetailController.prototype.scan = function () {
            var self = this;
            var req = {
                shipment: self.shipment,
                barcode: self.barcode
            };
            self.shipmentDetailService.scan(req).then(function (data) {
                if (data.success == 1) {
                    try {
                        successAudio(true);
                    } catch (exception) {
                    }
                    self.notify.success('TXT_SUCCESS');
                }
                else if (data.success == 0) {
                    try {
                        warningAudio(true);
                    } catch (exception) {
                    }
                    self.notify.warning('TXT_ITEM_NOT_FOUND_SKU');
                }
                self.scannedSkuList = data.scannedSkuList;
                self.barcode = "";
            })
        };

        ShipmentDetailController.prototype.ship = function () {
            var self = this;
            self.confirm('TXT_CONFIRM_SHIPPED').then(function () {
                var req = angular.copy(self.shipment);
                req.status = 3;//shipped 应使用
                var tempShipment = {};
                // 先判断是否有其他人改了当前的shipment
                self.shipmentDetailService.getInfo(self.shipment.id).then(function (data) {
                    tempShipment = data.shipment;
                    if (!_.isEqual(self.originalShipment, tempShipment)) {
                        self.alert("TXT_SHIPMENT_HAVE_BEEN_EDITED");
                        return;
                    }

                    self.shipmentDetailService.ship(req).then(function (data) {
                        if (data.result.succeedSkuCount > 0) {
                            self.notify.success("TXT_SUCCESS");
                            window.location.href = "#/shipment/shipment_info";
                        }
                    });
                });
            });
        };

        return ShipmentDetailController;
    })());
});