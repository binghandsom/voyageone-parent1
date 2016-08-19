define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/admin/views/home/welcome/datachart.ctl"
        },//渠道信息管理
        "channelInfoManage": {
            "hash": "/channel/channelInfoManage",
            "templateUrl": "views/channelManagement/channelInfoManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelInfoManage.ctl",
            "controller": "ChannelManagementController as ctrl"
        },//渠道类型属性管理
        "channelTypeAttrManage": {
            "hash": "/channel/channelTypeAttrManage",
            "templateUrl": "views/channelManagement/channelTypeAttrManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelTypeAttrManage.ctl",
            "controller": "ChannelTypeAttrManagementController as ctrl"
        },//第三方配置信息管理
        "channelThirdPartyCog": {
            "hash": "/channel/channelThirdPartyCog",
            "templateUrl": "views/channelManagement/channelThirdPartyCog.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelThirdPartyCog.ctl",
            "controller": "ChannelThirdPartyCogController as ctrl"
        },//短信配置管理
        "channelSmsCog": {
            "hash": "/channel/channelSmsConfig",
            "templateUrl": "views/channelManagement/channelSmsConfig.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelSmsConfig.ctl",
            "controller": "ChannelSmsCogController as ctrl"
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