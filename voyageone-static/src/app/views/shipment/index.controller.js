/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentInfoController', (function () {

        function ShipmentInfoController(popups) {
            this.popups = popups;
        }

        ShipmentInfoController.prototype = {
            popNewShipment: function () {
                this.popups.openShipment();
            }
        };
        return ShipmentInfoController;

    }()));
});