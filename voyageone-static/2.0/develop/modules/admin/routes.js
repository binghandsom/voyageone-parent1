define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/admin/views/home/welcome/datachart.ctl"
        },//渠道-渠道信息管理
        "channelInfoManage": {
            "hash": "/channel/channelInfoManage",
            "templateUrl": "views/channelManagement/channelInfoManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelInfoManage.ctl",
            "controller": "ChannelManagementController as ctrl"
        },//渠道-渠道类型属性管理
        "channelTypeAttrManage": {
            "hash": "/channel/channelTypeAttrManage",
            "templateUrl": "views/channelManagement/channelTypeAttrManage.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelTypeAttrManage.ctl",
            "controller": "ChannelTypeAttrManagementController as ctrl"
        },//渠道-第三方配置信息管理
        "channelThirdPartyCog": {
            "hash": "/channel/channelThirdPartyCog",
            "templateUrl": "views/channelManagement/channelThirdPartyCog.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelThirdPartyCog.ctl",
            "controller": "ChannelThirdPartyCogController as ctrl"
        },//渠道-短信配置管理
        "channelSmsCog": {
            "hash": "/channel/channelSmsConfig",
            "templateUrl": "views/channelManagement/channelSmsConfig.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelSmsConfig.ctl",
            "controller": "ChannelSmsCogController as ctrl"
        },//渠道-快递配置管理
        "channelCarrierCog": {
            "hash": "/channel/channelCarrierConfig",
            "templateUrl": "views/channelManagement/channelCarrierConfig.tpl.html",
            "controllerUrl": "modules/admin/views/channelManagement/channelCarrierConfig.ctl",
            "controller": "ChannelCarrierConfigController as ctrl"
        },//Cart管理
        "cartManagement": {
            "hash": "/cart/cartManagement",
            "templateUrl": "views/cartManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/cartManagement/index.ctl",
            "controller": "CartManagementController as ctrl"
        },//Cart-渠道Cart管理
        "channelShopManagement": {
            "hash": "/cart/channelShopManagement",
            "templateUrl": "views/cartManagement/channelShop.tpl.html",
            "controllerUrl": "modules/admin/views/cartManagement/channelShop.ctl",
            "controller": "CartChannelShopManagementController as ctrl"
        },//Cart-Cart物流管理
        "cartTrackingInfoManagement": {
            "hash": "/cart/trackingInfoManagement",
            "templateUrl": "views/cartManagement/cartTrackingInfo.tpl.html",
            "controllerUrl": "modules/admin/views/cartManagement/cartTrackingInfo.ctl",
            "controller": "CartTrackingInfoManagementController as ctrl"
        },//仓库-仓库管理
        "storeManagement": {
            "hash": "/store/storeManagement",
            "templateUrl": "views/storeManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/storeManagement/index.ctl",
            "controller": "StoreManagementController as ctrl"
        },//任务-任务管理
        "taskManagement": {
            "hash": "/task/taskManagement",
            "templateUrl": "views/taskManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/taskManagement/index.ctl",
            "controller": "TaskManagementController as ctrl"
        },//系统配置管理
        "systemManagement": {
            "hash": "/system/typeInfoManagement",
            "templateUrl": "views/systemManagement/typeInfo.tpl.html",
            "controllerUrl": "modules/admin/views/systemManagement/typeInfo.ctl",
            "controller": "TypeInfoManagementController as ctrl"
        }
    };
});