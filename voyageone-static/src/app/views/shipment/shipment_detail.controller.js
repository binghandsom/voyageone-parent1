/**
 * Created by sofia on 7/5/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentDetailController', (function () {

        function ShipmentDetailController($routeParams, notify, alert, confirm, shipmentDetailService) {
            this.shipmentId = $routeParams['shipmentId'];
            this.notify = notify;
            this.alert = alert;
            this.confirm = confirm;
            this.shipmentDetailService = shipmentDetailService;

            this.shipmentStatusList = [];
            this.expressCompanies = [];

            this.pageInfo = {
                curr: 1,
                total: 0,
                size: 10,
                fetch: this.search.bind(this)
            };
            this.searchInfo = {
                curr: this.pageInfo.curr,
                total: this.pageInfo.total,
                size: this.pageInfo.size,
                shipmentId: this.shipmentId
            };
        }

        ShipmentDetailController.prototype.init = function () {
            var self = this;
            self.shipmentDetailService.init(self.searchInfo).then(function (data) {
                self.shipmentStatusList = data.shipmentStatusList;
                self.expressCompanies = data.expressCompanies;
                self.shipment = data.shipment;
                if (self.shipment && self.shipment.shippedDate) self.shipment.shippedDate = new Date(self.shipment.shippedDate);
                self.scannedSkuList = data.scannedSkuList;
            })
        };

        ShipmentDetailController.prototype.search = function () {

        };

        ShipmentDetailController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.shipmentStatusList.length; i++) {
                if (self.shipmentStatusList[i].value == statusValue) {
                    var currentStatus = self.shipmentStatusList[i];
                    break;
                }
            }
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        return ShipmentDetailController;
    })());
});