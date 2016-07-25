define([
    'vms'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, confirm, popups, orderInfoService, shipmentScanPopupService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.orderInfoService = orderInfoService;
            this.popups = popups;
            this.shipmentScanPopupService = shipmentScanPopupService;

            this.oneDay = 24 * 60 * 60 * 1000;
            this.twoDay = 2 * this.oneDay;
            this.threeDay = 3 * this.oneDay;
            this.oneMonth = 30 * this.oneDay;

            var now = new Date();
            var today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
            this.orderDateFrom = new Date(today.getTime() - 6 * this.oneMonth);
            this.orderDateTo = today;

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
                status: "1",
                consolidationOrderId: "",
                sku: "",
                orderDateFrom: "",
                orderDateTo: ""
            };

            this.downloadInfo = {
                orderType: 'client_sku'
            };

            this.channelConfigs = {
                vendorOperateType: 'SKU'
            };
            this.searchOrderStatus = [];
            this.data = [];
        }

        OrderInfoController.prototype.init = function () {
            var self = this;
            self.orderInfoService.init().then(function (data) {
                // 获取当前shipment
                self.currentShipment = data.currentShipment;
                // 获取可选的订单状态
                self.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                self.channelConfigs = data.channelConfigs;

                self.search();
            });
        };

        OrderInfoController.prototype.search = function () {
            var self = this;
            if (self.orderDateFrom === undefined || self.orderDateTo === undefined) {
                self.alert("TXT_PLEASE_INPUT_A_VALID_DATE");
                return;
            } else if (self.orderDateFrom)
                self.searchInfo.orderDateFrom = self.orderDateFrom;

            if (self.orderDateTo) {
                var date = angular.copy(self.orderDateTo);
                date.setDate(date.getDate() + 1);
                self.searchInfo.orderDateTo = date;
            }

            self.searchInfo.curr = self.pageInfo.curr;
            self.searchInfo.size = self.pageInfo.size;
            self.orderInfoService.search(self.searchInfo).then(function (data) {
                self.pageInfo.total = data.orderInfo.total;
                self.data = data.orderInfo.orderList.map(function (item) {
                        item.className = 'bg-default';
                        item.subClassName = 'bg-sub-default';
                        var date = new Date();
                        if (item.status == '7') {
                            item.className = 'bg-gainsboro';
                            item.subClassName = 'bg-sub-gainsboro';
                        }
                        else if (item.status == '1') {
                            if (self.channelConfigs.vendorOperateType == 'ORDER') {
                                date = new Date(item.consolidationOrderTime);
                            } else if (self.channelConfigs.vendorOperateType == 'SKU') {
                                date = new Date(item.consolidationOrderTime);
                            } else {
                                self.alert('TXT_MISSING_REQUIRED_CHANNEL_CONFIG');
                            }
                            if ((new Date().getTime() - date.getTime()) >= self.threeDay) {
                                item.className = 'bg-danger';
                                item.subClassName = 'bg-sub-danger';
                            } else if ((new Date().getTime() - date.getTime()) >= self.twoDay) {
                                item.className = 'bg-warning';
                                item.subClassName = 'bg-sub-warning';
                            }
                        }
                        return item;
                    }
                )
            })
        };

        OrderInfoController.prototype.cancelOrder = function (item) {
            var self = this;
            self.confirm('TXT_CONFIRM_TO_CANCEL_ORDER').then(function () {
                self.orderInfoService.cancelOrder(item).then(function () {
                    self.search()
                })
            })
        };

        OrderInfoController.prototype.cancelSku = function (item) {
            var self = this;
            self.confirm('TXT_CONFIRM_TO_CANCEL_SKU').then(function () {
                self.orderInfoService.cancelSku(item).then(function () {
                    self.search();
                })
            })
        };

        OrderInfoController.prototype.downloadPickingList = function () {
            var self = this;
            $.download.post('/vms/order/order_info/downloadPickingList', {"orderType": self.downloadInfo.orderType});
        };

        OrderInfoController.prototype.toggleAll = function () {
            var self = this;
            var collapse = (self.collapse = !self.collapse);
            self.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };

        OrderInfoController.prototype.getStatusName = function (statusValue) {
            var self = this;
            for (var i = 0; i < self.searchOrderStatus.length; i++) {
                if (self.searchOrderStatus[i].value == statusValue) {
                    var currentStatus = self.searchOrderStatus[i];
                    break;
                }
            }
            // Receive with Error 太长了
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        OrderInfoController.prototype.popNewShipment = function (type) {
            var self = this;
            //1:Open；3：Shipped；4：Arrived；5：Recevied；6：Receive with Error
            var pendingShipmentStatus = "1";
            if (type == "edit") {
                pendingShipmentStatus = self.currentShipment.status;
            } else if (type == "end") {
                pendingShipmentStatus = "3";
            }
            var shipmentInfo = {
                shipment: self.currentShipment,
                type: type,
                pendingShipmentStatus: pendingShipmentStatus,
                searchOrderStatus: this.searchOrderStatus
            };

            this.popups.openShipment(shipmentInfo).then(function (shipment) {
                self.currentShipment = shipment;
            });
        };

        OrderInfoController.prototype.popAddToShipment = function (item) {
            var self = this;
            self.scanPopupInitialInfo = {
                "shipment": self.currentShipment,
                "consolidationOrderId": item.consolidationOrderId
            };
            self.shipmentScanPopupService.init(self.scanPopupInitialInfo).then(function (shipmentDetails) {
                shipmentDetails.scanPopupInitialInfo = self.scanPopupInitialInfo;
                self.popups.openAddShipment(shipmentDetails).then(function () {
                    self.init();
                });
            });
        };

        return OrderInfoController;
    }()));
});