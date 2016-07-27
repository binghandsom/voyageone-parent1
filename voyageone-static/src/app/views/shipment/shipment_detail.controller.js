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
            this.orderStatusList = [];
            this.expressCompanies = [];
            this.scannedSkuList = [];
            this.barcode = "";

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
                self.orderStatusList = data.orderStatusList;
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
                if (data.success == 1) self.notify.success('TXT_SUCCESS');
                else if (data.success == 0) {
                    self.notify.warning('TXT_ITEM_NOT_FOUND_SKU');
                }
                self.scannedSkuList = data.scannedSkuList;
                self.barcode = "";
            })
        };

        return ShipmentDetailController;
    })());
});