define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/admin/views/home/welcome/datachart.ctl"
        },
        "tmOrderChannel": {
            "hash": "/cart/tmOrderChannel",
            "templateUrl": "views/tmOrderChannel/index.tpl.html",
            "controllerUrl": "modules/admin/views/tmOrderChannel/index.ctl",
            "controller": "TmOrderChannelController as ctrl"
        },
        "cartManagement": {
            "hash": "/cart/cartManagement",
            "templateUrl": "views/cartManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/cartManagement/index.ctl",
            "controller": "CartManagementController as ctrl"
        },
        "storeManagement": {
            "hash": "/store/storeManagement",
            "templateUrl": "views/storeManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/storeManagement/index.ctl",
            "controller": "StoreManagementController as ctrl"
        }
    };
});