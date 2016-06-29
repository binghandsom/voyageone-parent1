define(['vms'], function (vms) {
    vms.controller('OrderListController', (function () {

        function OrderListController() {
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
        }

        OrderListController.prototype.toggleAll = function () {
            var collapse = (this.collapse = !this.collapse);
            this.data.forEach(function (item) {
                item.collapse = collapse;
            });
        };

        return OrderListController;

    }()));
});