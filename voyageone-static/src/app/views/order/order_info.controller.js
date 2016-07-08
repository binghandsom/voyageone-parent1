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

            var oneDay = 24 * 60 * 60 * 1000;
            var twoDay = 2 * oneDay;
            var threeDay = 3 * oneDay;
            var oneMonth = 30 * oneDay;

            this.orderDateFrom = new Date(new Date().getTime() - 6 * oneMonth);
            this.orderDateTo = new Date();

            this.searchInfo = {
                curr: 1,
                total: 0,
                size: 10,
                status: "1",
                orderId: "",
                sku: "",
                orderDateFrom: "",
                orderDateTo: ""
            };

            this.pageInfo = {
                curr: this.searchInfo.curr,
                total: this.searchInfo.total,
                size: this.searchInfo.size,
                fetch: this.search
            };

            this.channelConfigs = {
                venderOperateType: 'SKU'
            };
            this.searchOrderStatus = [];
            this.data = [];

            this.orderInfoService.init().then((data) => {

                // 获取可选的订单状态
                this.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                this.channelConfigs = data.channelConfigs;
                if (data.orderInfo) {
                    this.searchInfo.total = data.orderInfo.total;
                    // 获取现有的订单信息(默认为Open 订单时间倒序)
                    this.data = data.orderInfo.orderList.map((item) => {
                        item.className = '';
                        if (item.status === "7")
                            item.className = 'bg-gainsboro';
                        else {
                            var date = new Date(item.orderDateTimestamp);
                            if ((new Date().getTime() - date) >= threeDay)
                                item.className = 'bg-danger';
                            else if ((new Date().getTime() - date) >= twoDay)
                                item.className = 'bg-warning';
                        }
                        return item;
                    })
                }
            });
        }

        OrderInfoController.prototype.search = function () {

            this.searchInfo.orderDateFrom = this.orderDateFrom.getTime();
            this.searchInfo.orderDateTo = this.orderDateTo.getTime();

            this.orderInfoService.search(this.searchInfo).then((data) => {
                this.searchInfo.total = data.orderInfo.total;
                this.data = data.orderInfo.orderList.map((item) => {
                    item.className = '';
                    if (item.status === 'cancel')
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