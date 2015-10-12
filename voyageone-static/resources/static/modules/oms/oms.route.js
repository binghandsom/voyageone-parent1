/**
 * @Name:    route.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define (function (require) {

    var angularAMD = require ('angularAMD'),
        mainApp = require ('components/app');

    mainApp.constant ('omsRoute', {
        'oms_default_index': {
            'hash': '/oms/default/index',
            'page': 'modules/oms/default/index.tpl.html',
            'controller': 'modules/oms/default/omsIndex.ctl'
        },
        'oms_orders_search': {
            'hash': '/oms/orders/search',
            'page': 'modules/oms/orders/search.tpl.html',
            'controller': 'modules/oms/orders/search.ctl'
        },
        'oms_orders_accounting': {
            'hash': '/oms/orders/accounting',
            'page': 'modules/oms/orders/accounting.tpl.html',
            'controller': 'modules/oms/orders/accounting.ctl'
        },
        'oms_orders_rate': {
            'hash': '/oms/orders/rate',
            'page': 'modules/oms/orders/rate.tpl.html',
            'controller': 'modules/oms/orders/rate.ctl'
        },
        //'oms_orders_orderdetail_search': {
        //    'hash': '/oms/orders/orderdetail/',
        //    'page': 'modules/oms/orders/orderDetail.tpl.html',
        //    'controller': 'modules/oms/orders/orderDetail.ctl'
        //},
        'oms_orders_orderdetail': {
            'hash': '/oms/orders/orderdetail/:sourceOrderId',
            'page': 'modules/oms/orders/orderDetail.tpl.html',
            'controller': 'modules/oms/orders/orderDetail.ctl'
        },
        'oms_orders_addneworder': {
            'hash': '/oms/orders/addneworder',
            'page': 'modules/oms/orders/addNewOrder.tpl.html',
            'controller': 'modules/oms/orders/addNewOrder.ctl'
        },
        //'oms_orders_addneworder_with_sourceOrderId': {
        //    'hash': '/oms/orders/addneworder/:sourceOrderId',
        //    'page': 'modules/oms/orders/addNewOrder.tpl.html',
        //    'controller': 'modules/oms/orders/addNewOrder.ctl'
        //},
        'oms_customer_search': {
            'hash': '/oms/customer/search',
            'page': 'modules/oms/customer/customerSearch.tpl.html',
            'controller': 'modules/oms/customer/customerSearch.ctl'
        },
        'oms_customer_customerDetail': {
            'hash': '/oms/customer/customerdetail',
            'page': 'modules/oms/customer/customerDetail.tpl.html',
            'controller': 'modules/oms/customer/customerDetail.ctl'
        },
        'oms_orders_index': {
            'hash': '/oms/orders/index',
            'page': 'modules/oms/orders/orderIndex.tpl.html',
            'controller': 'modules/oms/orders/orderIndex.ctl'
        },
        'oms_customer_index': {
            'hash': '/oms/customer/index',
            'page': 'modules/oms/customer/index.tpl.html',
            'controller': 'modules/oms/customer/customerIndex.ctl'
        }
    });

    mainApp.config (["$routeProvider", "omsRoute",
        function ($routeProvider, route) {

            var _ = require ("underscore");

            return _.each (route, function (value) {

                var angularAMD = require ("angularAMD");
                var commonUtil = require ('components/util/commonUtil');

                return $routeProvider.when (value.hash, angularAMD.route ({
                    templateUrl: value.page,
                    resolve: {
                        load: ["$q", "$rootScope", function ($q, $rootScope) {
                            return commonUtil.loadController ($q, $rootScope, value.controller);
                        }]
                    }
                }));
            });
        }]);
});