/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    vms.controller('ShipmentInfoController', (function () {

        function ShipmentInfoController(popups, alert, notify, confirm, shipmentInfoService) {
            this.popups = popups;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.shipmentInfoService = shipmentInfoService;
            this.shipmentStatusList = [];
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
                shippedDateFrom: "",
                shippedDateTo: ""
            };

            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;

            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            this.shippedDateFrom = new Date(today.getTime() - 6 * this.oneMonth);
            this.shippedDateTo = today;
        }

        ShipmentInfoController.prototype.init = function () {
            var self = this;
            self.shipmentInfoService.init().then(function (data) {
                self.shipmentStatusList = data.shipmentStatusList;
                self.search();
            });
        };

        ShipmentInfoController.prototype.search = function () {
            var self = this;
            if (self.shippedDateFrom === undefined || self.shippedDateTo === undefined) {
                self.alert("TXT_PLEASE_INPUT_A_VALID_DATE");
                return;
            } else if (self.shippedDateFrom)
                self.searchInfo.shippedDateFrom = self.shippedDateFrom;
            else self.searchInfo.shippedDateFrom = undefined;
            if (self.shippedDateTo) {
                var date = angular.copy(self.shippedDateTo);
                date.setDate(date.getDate() + 1);
                self.searchInfo.shippedDateTo = date;
            } else {
                self.searchInfo.shippedDateTo = undefined;
            }
            self.searchInfo.curr = self.pageInfo.curr;
            self.searchInfo.size = self.pageInfo.size;
            self.shipmentInfoService.search(self.searchInfo).then(function (data) {
                self.pageInfo.total = data.shipmentInfo.total;
                self.data = data.shipmentInfo.shipmentList;
            })

        };

        ShipmentInfoController.prototype.popNewShipment = function () {
            this.popups.openShipment();
        };

        ShipmentInfoController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.shipmentStatusList.length; i++) {
                if (self.shipmentStatusList[i].value == statusValue) {
                    var currentStatus = self.shipmentStatusList[i];
                    break;
                }
            }
            // Receive with Error 太长了
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        return ShipmentInfoController;

    }()));
});