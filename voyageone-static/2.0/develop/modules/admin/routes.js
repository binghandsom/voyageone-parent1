define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/admin/views/home/welcome/datachart.ctl"
        },//用户/权限-用户管理
        "adminUserManage": {
            "hash": "/admin/userManage",
            "templateUrl": "views/adminManagement/user/index.tpl.html",
            "controllerUrl": "modules/admin/views/adminManagement/user/index.ctl",
            "controller": "UserManagementController as ctrl"
        },//用户/权限-角色管理
        "adminRoleManage": {
            "hash": "/admin/roleManage",
            "templateUrl": "views/adminManagement/role/index.tpl.html",
            "controllerUrl": "modules/admin/views/adminManagement/role/index.ctl",
            "controller": "RoleManagementController as ctrl"
        },//用户/权限-组织管理
        "adminOrgManage": {
            "hash": "/admin/orgManage",
            "templateUrl": "views/adminManagement/orgnization/index.tpl.html",
            "controllerUrl": "modules/admin/views/adminManagement/orgnization/index.ctl",
            "controller": "OrgManagementController as ctrl"
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
        "typeInfoManagement": {
            "hash": "/system/typeInfoManagement",
            "templateUrl": "views/systemManagement/typeInfo.tpl.html",
            "controllerUrl": "modules/admin/views/systemManagement/typeInfo.ctl",
            "controller": "TypeInfoManagementController as ctrl"
        },//系统配置-类型属性管理
        "typeAttributeManagement": {
            "hash": "/system/typeAttributeManagement",
            "templateUrl": "views/systemManagement/typeAttribute.tpl.html",
            "controllerUrl": "modules/admin/views/systemManagement/typeAttribute.ctl",
            "controller": "TypeAttributeManagementController as ctrl"
        },//系统配置-属性统一配置管理
        "PropertyUnifiedConfig": {
            "hash": "/system/unifiedConfig",
            "templateUrl": "views/systemManagement/propertyUnifiedCog.tpl.html",
            "controllerUrl": "modules/admin/views/systemManagement/propertyUnifiedCog.ctl",
            "controller": "UnifiedConfigController as ctrl"
        },//系统配置-Code属性管理
        "codeAttributeConfig": {
            "hash": "/system/codeAttr",
            "templateUrl": "views/systemManagement/codeAttr.tpl.html",
            "controllerUrl": "modules/admin/views/systemManagement/codeAttr.ctl",
            "controller": "CodeAttributeController as ctrl"
        }
    };
});