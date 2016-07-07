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

            this.searchInfo = {
                status: "",
                orderId: "",
                sku: "",
                orderDateTimestampFrom: new Date().getTime(),
                orderDateTimestampTo: new Date().getTime()
            };

            this.channelConfigs = {
                venderOperateType: 'SKU'
            };
            this.searchOrderStatus = [];

            var now = new Date().getTime();
            var onwDay = 24 * 60 * 60 * 1000;
            var twoDay = 2 * onwDay;
            var threeDay = 3 * onwDay;

            this.orderInfoService.init().then((data) => {

                // 获取可选的订单状态
                this.searchOrderStatus = data.searchOrderStatus;

                // 记录用户的操作方式(sku/order)
                this.channelConfigs = data.channelConfigs;
                if (data.orderInfoList) {
                    // 获取现有的订单信息(默认为Open 订单时间倒序)
                    this.data = data.orderInfoList.map((item) => {
                        item.className = '';
                        if (item.status === 'cancel')
                            item.className = 'bg-gainsboro';
                        else {
                            var date = new Date(item.orderDateTimestamp);
                            if ((now - date) >= threeDay)
                                item.className = 'bg-danger';
                            else if ((now - date) >= twoDay)
                                item.className = 'bg-warning';
                        }
                        return item;
                    })
                }
            });
        }

        OrderInfoController.prototype.search = function () {
            this.orderInfoService.search(searchInfo).then((data) => {
                this.data = data.orderInfoList.map((item) => {
                    item.className = '';
                    if (item.status === 'cancel')
                        item.className = 'bg-gainsboro';
                    else {
                        var date = new Date(item.orderDateTimestamp);
                        if ((now - date) >= threeDay)
                            item.className = 'bg-danger';
                        else if ((now - date) >= twoDay)
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