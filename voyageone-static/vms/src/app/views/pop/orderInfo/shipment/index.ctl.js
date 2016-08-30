/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms',
    'underscore'
], function (vms, underscore, moment) {
    vms.controller('NewShipmentController', (function () {
        function NewShipmentController(alert, notify, confirm, $translate, shipmentPopupService, context, $uibModalInstance, $filter) {
            this.$translate = $translate;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentPopupService = shipmentPopupService;
            this.type = context.type;
            this.$uibModalInstance = $uibModalInstance;
            this.expressCompanies = [];
            this.originalShipment = context.shipment;
            this.statusList = context.statusList;
            this.shipment = angular.copy(context.shipment);
            this.channelConfig = context.channelConfig;
            this.filter = $filter;
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

            if (self.type == 'end') {
                self.shipmentPopupService.countOrder(self.shipment.id).then(function (data) {
                    if (data.orderCount == 0) {
                        self.alert('TXT_NOTHING_TO_SHIP');
                        self.$uibModalInstance.close(self.originalShipment);
                    }
                });
            } else if (self.type == 'new') {
                self.shipment.shippedDate = new Date();
                self.shipment.expressCompany = self.channelConfig.defaultDeliveryCompany;
                if (self.channelConfig.namingConverter)
                    self.shipment.shipmentName = self.filter('date')(new Date(), self.channelConfig.namingConverter);
            }
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
            self.shipmentPopupService.getInfo(self.originalShipment.id).then(function (data) {
                tempShipment = data.shipment;
                if (!_.isEqual(self.originalShipment, tempShipment)) {
                    self.alert("TXT_SHIPMENT_HAVE_BEEN_EDITED");
                    self.$uibModalInstance.close(tempShipment);
                    return;
                }

                self.shipmentPopupService.submit(req).then(function (data) {
                    self.notify.success("TXT_SUCCESS");
                    self.$uibModalInstance.close(data.currentShipment);
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
                // 确认当前shipment下的所有订单是否扫描完整
                self.shipmentPopupService.confirm(req).then(function (data) {
                    if (!data.invalidOrderList || data.invalidOrderList.length == 0) {
                        self.shipmentPopupService.end(req).then(function (data) {
                            self.shipment = data.currentShipment;
                            if (self.shipment)
                                self.shipmentExisted = true;
                            self.notify.success("TXT_SUCCESS");
                            self.$uibModalInstance.close(self.shipment);
                        });
                    } else {
                        // 订单号
                        var orders = "";
                        for (var index = 0; index < data.invalidOrderList.length; index++) {
                            if (index < data.invalidOrderList.length - 1)
                                orders = orders + data.invalidOrderList[index] + ", ";
                            else orders = orders + data.invalidOrderList[index];
                        }
                        self.confirm(self.$translate.instant('TXT_SCANNED_ORDER_NOT_FINISHED').replace("%s", orders)).then(function () {
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
