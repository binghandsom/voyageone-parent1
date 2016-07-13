/**
 * Created by sofia on 7/1/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('NewShipmentController', (function () {


        function NewShipmentController(alert, notify, confirm, shipmentService, context) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentService = shipmentService;
            this.shipment = context.shipment;
            if (!this.shipment) this.shipment = {status: "1"};
        }

        return NewShipmentController;
    })());
});
