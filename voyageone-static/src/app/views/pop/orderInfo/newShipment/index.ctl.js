/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms',
    'underscore'
], function (vms) {
    vms.controller('NewShipmentController', (function () {
        function NewShipmentController(alert, notify, confirm, shipmentPopupService, context, $uibModalInstance) {
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
                if (this.shipment.shippedDateTimestamp) {
                    this.shipment.shippedDate = new Date(this.shipment.shippedDateTimestamp);
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
                    req.shippedDateTimestamp = req.shippedDate.getTime();
                    req.shippedDate = undefined;
                }
                self.shipmentPopupService.submit(req).then(function (data) {
                    self.shipment = data.currentShipment;
                    if (self.shipment) {
                        self.shipmentExisted = true;
                        if (self.shipment.shippedDateTimestamp)
                            self.shipment.shippedDate = new Date(self.shipment.shippedDateTimestamp);
                    }
                    self.notify.success("TXT_SUCCESS");
                    self.$uibModalInstance.close(self.shipment);
                });

            });
        };

        return NewShipmentController;
    })());
});
