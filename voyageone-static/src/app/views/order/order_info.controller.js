define([
    'vms'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, confirm, popups, orderInfoService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.orderInfoService = orderInfoService;
            this.popups = popups;

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
                orderId: "",
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

            this.orderInfoService.init().then(function (data) {
                var self = this;
                // 获取当前shipment
                self.currentShipment = data.currentShipment;

                // 获取可选的订单状态
                self.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                self.channelConfigs = data.channelConfigs;

                self.search();
            });
        }

        OrderInfoController.prototype.search = function () {
            var self = this;
            if (self.orderDateFrom === undefined || self.orderDateTo === undefined) {
                self.alert("'TXT_PLEASE_INPUT_A_VALID_DATE'");
                return;
            } else if (self.orderDateFrom)
                self.searchInfo.orderDateFrom = self.orderDateFrom.getTime();
            else self.searchInfo.orderDateFrom = undefined;
            if (self.orderDateTo) {
                var date = angular.copy(self.orderDateTo);
                date.setDate(date.getDate() + 1);
                self.searchInfo.orderDateTo = date.getTime();
            } else {
                self.searchInfo.orderDateTo = undefined;
            }
            self.searchInfo.curr = self.pageInfo.curr;
            self.searchInfo.size = self.pageInfo.size;
            self.orderInfoService.search(self.searchInfo).then(function (data) {
                self.pageInfo.total = data.orderInfo.total;
                self.data = data.orderInfo.orderList.map(function (item) {
                        item.className = '';
                        var date = new Date();
                        if (item.status == '7') item.className = 'bg-gainsboro';
                        else if (item.status == '1') {
                            if (self.channelConfigs.vendorOperateType == 'ORDER') {
                                date = new Date(item.orderDateTime);
                            } else if (self.channelConfigs.vendorOperateType == 'SKU') {
                                date = new Date(item.orderDateTimestamp);
                            } else {
                                self.alert('TXT_MISSING_REQUIRED_CHANNEL_CONFIG');
                            }
                            if ((new Date().getTime() - date) >= self.threeDay) item.className = 'bg-danger';
                            else if ((new Date().getTime() - date) >= self.twoDay) item.className = 'bg-warning';
                        }
                        return item;
                    }
                )
            })
        };

        OrderInfoController.prototype.cancelOrder = function (item) {
            var self = this;
            this.confirm('TXT_CONFIRM_TO_CANCEL_ORDER').then(function () {
                self.orderInfoService.cancelOrder(item).then(function () {
                    self.search()
                })
            })
        };

        OrderInfoController.prototype.cancelSku = function (item) {
            var self = this;
            this.confirm('TXT_CONFIRM_TO_CANCEL_SKU').then(function () {
                self.orderInfoService.cancelSku(item).then(function () {
                    self.search();
                })
            })
        };

        OrderInfoController.prototype.downloadPickingList = function () {

            // this.orderInfoService.downloadPickingList().then(function (res) {
            //     console.info(res);
            // });
            $.download.post('/vms/order/order_info/downloadPickingList', {"orderType": this.downloadInfo.orderType});
        };

        OrderInfoController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };

        OrderInfoController.prototype.getStatusName = function (statusValue) {
            var currentStatus = this.searchOrderStatus.find(function (status) {
                return status.value == statusValue;
            });
            if (!currentStatus) return statusValue;
            return currentStatus.name;
        };

        OrderInfoController.prototype.popNewShipment = function () {
            var shipmentInfo = {
                shipment: this.currentShipment,
                searchOrderStatus: this.searchOrderStatus
            };
            this.popups.openShipment(shipmentInfo);
        };
        OrderInfoController.prototype.popAddToShipment = function () {
            this.popups.openAddShipment();
        };
        return OrderInfoController;

    }()));
});