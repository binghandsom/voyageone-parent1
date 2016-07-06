define(['vms'], function (vms) {
    vms.service('orderListService', (function () {

        function OrderListService($q) {
            this.$q = $q;
            this.data = [
                {
                    orderId: 89567824, orderDate: '2016-06-23 15:30:07', totalPrice: 6000, details: [
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                    {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                ]
                }, {
                    orderId: 89567824, orderDate: '2016-06-29 15:30:07', totalPrice: 6000, details: [
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000},
                        {sku: 'LKF3958', desc: 'dqaiojgioqjiojdiowqjiojwioqjfiojqwio', status: 'Open', price: 1000}
                    ]
                }, {
                    orderId: 89567824, orderDate: '2016-06-27 15:30:07', totalPrice: 6000, details: [
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

        OrderListService.prototype.getAll = function () {
            return this.$q((resolve) => resolve(this.data));
        };

        return OrderListService;

    }()));
});