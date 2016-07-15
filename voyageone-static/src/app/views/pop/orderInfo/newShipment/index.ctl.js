/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('NewShipmentController', (function () {
        function NewShipmentController(alert, notify, confirm, shipmentPopupService, context) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentPopupService = shipmentPopupService;
            this.shipmentExisted = false;
            this.expressCompanies = [
                {
                    name: "test",
                    value: 1
                }
            ];
            this.shipment = angular.copy(context.shipment);
            if (!this.shipment) {
                this.shipment = {
                    status: "1"
                }
            } else {
                this.shipmentExisted = true;
                if (this.shipment.shippedDateTimestamp)
                    this.shipment.shippedDate = new Date(this.shipment.shippedDateTimestamp);
            }
        }

        NewShipmentController.prototype.init = function () {
            var self = this;

            this.shipmentPopupService.init().then(function (data) {
                self.expressCompanies = data.expressCompanies;
            })
        };

        NewShipmentController.prototype.create = function () {
            var self = this;

            this.shipmentPopupService.create(this.shipment).then(function (data) {
                console.info("finished");
                self.modal.$close();
            })
        };

        NewShipmentController.prototype.submit = function () {
            var self = this;
            var req = angular.copy(this.shipment);
            if (req.shippedDate) {
                req.shippedDateTimestamp = req.shippedDate.getTime();
                req.shippedDate = undefined;
            }
            this.shipmentPopupService.submit(req).then(function (data) {
                self.shipment = data.currentShipment;
                if (self.shipment) {
                    this.shipmentExisted = true;
                    if (self.shipment.shippedDateTimestamp)
                        self.shipment.shippedDate = new Date(self.shipment.shippedDateTimestamp);
                }
                self.modal.close(self.shipment);
            });

        };

        return NewShipmentController;
    })());
});
