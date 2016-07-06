define([
    'vms',
    './order_info.mock.service'
], function (vms) {
    vms.controller('OrderInfoController', (function () {

        function OrderInfoController(alert, notify, orderListService, popups) {
            this.alert = alert;
            this.notify = notify;
            this.orderListService = orderListService;
            this.popups = popups;

            var now = new Date();
            var onwDay = 24 * 60 * 60 * 1000;
            var twoDay = 2 * onwDay;
            var threeDay = 3 * onwDay;
            now = new Date(now.valueOf() + now.getTimezoneOffset() * 60000);
            this.orderListService.getAll().then((data) => this.data = data.map((item) => {
                item.className = '';
                if (item.status === 'cancel')
                    item.className = 'bg-gainsboro';
                else {
                    var date = new Date(item.orderDate);
                    if ((now - date) >= threeDay)
                        item.className = 'bg-danger';
                    else if ((now - date) >= twoDay)
                        item.className = 'bg-warning';
                }
                return item;
            }));
        }

        OrderInfoController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
            var self = this;
            this.alert('展开/收缩所有').then(function () {
                self.notify.success('已经 展开/收缩所有');
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