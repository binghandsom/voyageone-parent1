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
                this.shipment = {status: "1"}
            } else this.shipmentExisted = true;

            this.shipmentPopupService.init().then((data) => {
                this.expressCompanies = data.expressCompanies;
            })
        }

        NewShipmentController.prototype.submit = function () {
            // this.shipmentPopupService.submit(this.shipment).then(function (res) {
                console.info("sdfassa");
            // })
        };

        return NewShipmentController;
    })());
});
