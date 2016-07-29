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
                size: this.pageInfo.size
            };

            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;
            this.searchInfo.shippedDateFrom = null;
            this.searchInfo.shippedDateTo = null;
        }

        ShipmentInfoController.prototype.init = function () {
            var self = this;
            self.shipmentInfoService.init().then(function (data) {
                self.shipmentStatusList = data.shipmentStatusList;
                self.channelConfigs = data.channelConfigs;
                var sessionSearchInfo = JSON.parse(sessionStorage.getItem('shipmentSearchInfo'));
                if (sessionSearchInfo) self.searchInfo = sessionSearchInfo;
                self.search(self.searchInfo.curr);
            });
        };

        ShipmentInfoController.prototype.search = function (curr) {
            var self = this;
            if (self.shippedDateFrom)
                self.searchInfo.shippedDateFrom = self.shippedDateFrom;
            else self.searchInfo.shippedDateFrom = undefined;
            if (self.shippedDateTo) {
                var date = angular.copy(self.shippedDateTo);
                date.setDate(date.getDate() + 1);
                self.searchInfo.shippedDateTo = date;
            } else {
                self.searchInfo.shippedDateTo = undefined;
            }
            self.searchInfo.curr = curr;
            self.searchInfo.size = self.pageInfo.size;
            sessionStorage.setItem('shipmentSearchInfo', JSON.stringify(self.searchInfo));
            self.shipmentInfoService.search(self.searchInfo).then(function (data) {
                self.pageInfo.total = data.shipmentInfo.total;
                self.data = data.shipmentInfo.shipmentList;
            })
        };

        ShipmentInfoController.prototype.popShipment = function (shipment, type) {
            var self = this;
            //1:Open；3：Shipped；4：Arrived；5：Recevied；6：Receive with Error
            var pendingShipmentStatus = "1";
            if (type == "edit") {
                pendingShipmentStatus = shipment.status;
            } else if (type == "end") {
                pendingShipmentStatus = "3";
            }
            var popShipment = angular.copy(shipment);
            popShipment.orderTotal = 0;
            popShipment.skuTotal = 0;
            delete popShipment.$$hashKey;
            var shipmentInfo = {
                shipment: popShipment,
                type: type,
                pendingShipmentStatus: pendingShipmentStatus,
                statusList: this.shipmentStatusList
            };

            this.popups.openShipment(shipmentInfo).then(function () {
                self.search();
            });
        };

        ShipmentInfoController.prototype.getStatusName = function (statusValue) {
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

        return ShipmentInfoController;

    }()));
});