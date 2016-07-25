/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms',
    'underscore'
], function (vms) {
    vms.controller('NewShipmentController', (function () {
        function NewShipmentController(alert, notify, confirm, $translate, shipmentPopupService, context, $uibModalInstance) {
            this.$translate = $translate;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentPopupService = shipmentPopupService;
            this.type = context.type;
            this.$uibModalInstance = $uibModalInstance;
            this.expressCompanies = [];
            this.originalShipment = context.shipment;
            this.shipment = angular.copy(context.shipment);
            if (this.shipment) {
                if (this.shipment.shippedDate) {
                    this.shipment.shippedDate = new Date(this.shipment.shippedDate);
                }
            } else {
                this.shipment = {
                    status: context.pendingShipmentStatus
                }
            }
            this.shipment.status = context.pendingShipmentStatus;
        }

        NewShipmentController.prototype.init = function () {
            var self = this;
            self.shipmentPopupService.init().then(function (data) {
                self.expressCompanies = data.expressCompanies;
            })
        };

        NewShipmentController.prototype.create = function () {
            var self = this;
            self.shipmentPopupService.create(self.shipment).then(function (data) {
                self.$uibModalInstance.close(data.currentShipment);
            })
        };

        NewShipmentController.prototype.submit = function () {
            var self = this;
            var req = angular.copy(self.shipment);
            var tempShipment = {};
            // 先判断是否有其他人改了当前的shipment
            self.shipmentPopupService.get().then(function (data) {
                tempShipment = data.currentShipment;
                if (!_.isEqual(self.originalShipment, tempShipment)) {
                    self.alert("TXT_SHIPMENT_HAVE_BEEN_EDITED");
                    self.$uibModalInstance.close(tempShipment);
                    return;
                }

                if (req.shippedDate) {
                    req.shippedDate = req.shippedDate.getTime();
                }
                self.shipmentPopupService.submit(req).then(function (data) {
                    self.shipment = data.currentShipment;
                    if (self.shipment) {
                        self.shipmentExisted = true;
                        if (self.shipment.shippedDate)
                            self.shipment.shippedDate = new Date(self.shipment.shippedDate);
                    }
                    self.notify.success("TXT_SUCCESS");
                    self.$uibModalInstance.close(self.shipment);
                });

            });
        };

        NewShipmentController.prototype.end = function () {
            var self = this;
            var req = angular.copy(self.shipment);
            var tempShipment = {};
            // 先判断是否有其他人改了当前的shipment
            self.shipmentPopupService.get().then(function (data) {
                tempShipment = data.currentShipment;
                if (!_.isEqual(self.originalShipment, tempShipment)) {
                    self.alert("TXT_SHIPMENT_HAVE_BEEN_EDITED");
                    self.$uibModalInstance.close(tempShipment);
                    return;
                }

                if (req.shippedDate) {
                    req.shippedDateTimestamp = req.shippedDate.getTime();
                    req.shippedDate = undefined;
                }

                // 确认当前shipment下的所有订单是否扫描完整
                self.shipmentPopupService.confirm(req).then(function (data) {
                    if (!data.invalidOrderList || data.invalidOrderList.length == 0) {
                        self.shipmentPopupService.end(req).then(function (data) {
                            self.shipment = data.currentShipment;
                            if (self.shipment) {
                                self.shipmentExisted = true;
                                if (self.shipment.shippedDateTimestamp)
                                    self.shipment.shippedDate = new Date(self.shipment.shippedDateTimestamp);
                            }
                            self.notify.success("TXT_SUCCESS");
                            self.$uibModalInstance.close(self.shipment);
                        });
                    } else {
                        // 订单号
                        var orders = "";
                        for (var index = 0; index < data.invalidOrderList.length; index ++) {
                            if (index < data.invalidOrderList.length - 1)
                                orders = orders + data.invalidOrderList[index] + ", ";
                            else orders = orders + data.invalidOrderList[index];
                        }
                        self.confirm(self.$translate.instant('TXT_SCANNED_ORDER_NOT_FINISHED').replace("%s",orders)).then(function () {
                            self.shipmentPopupService.end(req).then(function (data) {
                                self.shipment = data.currentShipment;
                                if (self.shipment) {
                                    self.shipmentExisted = true;
                                    if (self.shipment.shippedDateTimestamp)
                                        self.shipment.shippedDate = new Date(self.shipment.shippedDateTimestamp);
                                }
                                self.notify.success("TXT_SUCCESS");
                                self.$uibModalInstance.close(self.shipment);
                            });
                        })
                    }
                });
            });
        };

        return NewShipmentController;
    })());
});
