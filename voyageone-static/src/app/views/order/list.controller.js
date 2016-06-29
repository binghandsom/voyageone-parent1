define(['vms'], function (vms) {
    vms.controller('OrderListController', (function () {

        function OrderListController(alert, notify) {
            this.data = [
                {
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                ]
                },{
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                    ]
                },{
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                    ]
                },
                {
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                ]
                },
                {
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                ]
                },
                {
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                ]
                }
            ];

            this.alert = alert;
            this.notify = notify;
        }

        OrderListController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
            var self = this;
            this.alert('展开/收缩所有').then(function () {
                self.notify.success('已经 展开/收缩所有');
            });
        };

        return OrderListController;

    }()));
});