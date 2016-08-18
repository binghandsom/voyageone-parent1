define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/admin/views/home/welcome/datachart.ctl"
        },
        "channelInfoManage": {
            "hash": "/channel/channelInfoManage",
            "templateUrl": "views/channelManagement/channelInfoManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelInfoManage.ctl",
            "controller": "ChannelManagementController as ctrl"
        },
        "channelTypeAttrManage": {
            "hash": "/channel/channelTypeAttrManage",
            "templateUrl": "views/channelManagement/channelTypeAttrManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelTypeAttrManage.ctl",
            "controller": "ChannelTypeAttrManagementController as ctrl"
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