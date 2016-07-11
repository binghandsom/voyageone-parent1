define([
    'vms',
    './order_info.mock.service'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, orderInfoService, popups) {
            this.alert = alert;
            this.notify = notify;
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

            this.channelConfigs = {
                vendorOperateType: 'SKU'
            };
            this.searchOrderStatus = [];
            this.data = [];

            this.orderInfoService.init().then((data) => {
                var self = this;

                // 获取可选的订单状态
                this.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                this.channelConfigs = data.channelConfigs;

                this.search();
            });
        }

        OrderInfoController.prototype.search = function () {

            if (this.orderDateFrom)
                this.searchInfo.orderDateFrom = this.orderDateFrom.getTime();
            else this.searchInfo.orderDateFrom = undefined;
            if (this.orderDateTo) {
                var date = angular.copy(this.orderDateTo);
                date.setDate(date.getDate() + 1);
                this.searchInfo.orderDateTo = date.getTime();
            } else {
                this.searchInfo.orderDateTo = undefined;
            }

            this.orderInfoService.search(this.searchInfo).then((data) => {
                this.pageInfo.total = data.orderInfo.total;
                this.data = data.orderInfo.orderList.map((item) => {
                    item.className = '';
                    if (item.status == '7')
                        item.className = 'bg-gainsboro';
                    else {
                        var date = new Date(item.orderDateTimestamp);
                        if ((new Date().getTime() - date) >= this.threeDay)
                            item.className = 'bg-danger';
                        else if ((new Date().getTime() - date) >= this.twoDay)
                            item.className = 'bg-warning';
                    }
                    return item;
                })
            })
        };

        OrderInfoController.prototype.cancelOrder = function (item) {
            this.orderInfoService.cancelOrder(item).then(
                this.search()
            )
        };

        OrderInfoController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };
        OrderInfoController.prototype.popNewShipment = function () {
            this.popups.openNewShipment();
        };
        OrderInfoController.prototype.popAddShipment = function () {
            this.popups.openAddShipment();
        };
        return OrderInfoController;

    }()));
});