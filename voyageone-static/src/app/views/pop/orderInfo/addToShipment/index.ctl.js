/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('AddToShipmentController', (function () {
        function AddToShipmentController(context) {
            this.context = context;
            this.barcode = null;
        }

        AddToShipmentController.prototype.init = function () {
            var self = this;
            self.summary = self.context;
        };

        AddToShipmentController.prototype.scan = function (barcode) {
            var self = this;
            self.barcode = null;
        };
        return AddToShipmentController;
    })());
});