define(function () {
    return {
        otherwise: '/order/list',
        routes: [{
            "hash": "/order/list",
            "templateUrl": "./views/order/list.html",
            "controllerUrl": "./views/order/list.controller",
            "controller": "OrderListController as ctrl"
        }]
    };
});