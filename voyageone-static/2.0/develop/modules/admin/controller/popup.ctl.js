/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'underscore'
], function ( _) {

    angular.module('com.voyageone.popups',[]).constant('popActions', {
        "modifyPass": {
            "templateUrl": "views/pop/modifyPass/modifyPass.tpl.html",
            "controllerUrl": "modules/admin/views/pop/modifyPass/modifyPass.ctl",
            "controller": 'ModifyPassController as ctrl',
            "size": 'md'
        },
        "config": {
            "templateUrl": "views/pop/config/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/config/index.ctl",
            "controller": 'ConfigController as ctrl',
            "size": 'lg'
        },
        "createEdit": {
            "templateUrl": "views/pop/createEdit/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/createEdit/index.ctl",
            "controller": 'CreateEditController as ctrl',
            "size": 'md'
        },

        "addAdminUser": {
            "templateUrl": "views/pop/addAdmin/addUser/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addUser/index.ctl",
            "controller": 'AddUserController as ctrl',
            "size": 'lg'
        },
        "userAuthority": {
            "templateUrl": "views/pop/authorityManagement/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/authorityManagement/index.ctl",
            "controller": 'authorityController as ctrl',
            "size": 'lg'
        },
        "addOrg": {
            "templateUrl": "views/pop/addAdmin/addOrg/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addOrg/index.ctl",
            "controller": 'AddOrgController as ctrl',
            "size": 'md'
        },
        "addRole": {
            "templateUrl": "views/pop/addAdmin/addRole/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addRole/index.ctl",
            "controller": 'AddRoleController as ctrl',
            "size": 'lg'
        },
        "addRoleEdit": {
            "templateUrl": "views/pop/addAdmin/addRole/roleEdit.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addRole/roleEdit.ctl",
            "controller": 'RoleEditController as ctrl',
            "size": 'lg'
        },
        "copyRole": {
            "templateUrl": "views/pop/addAdmin/addRole/copy_role.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addRole/copy_role.ctl",
            "controller": 'RoleCopyController as ctrl',
            "size": 'md'
        },
        "addRes": {
            "templateUrl": "views/pop/addAdmin/addRes/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addAdmin/addRes/index.ctl",
            "controller": 'AddResController as ctrl',
            "size": 'lg'
        },

        "addChannel": {
            "templateUrl": "views/pop/addChannel/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addChannel/index.ctl",
            "controller": 'AddController as ctrl',
            "size": 'lg'
        },
        "addChannelType": {
            "templateUrl": "views/pop/addChannel/addChannelTypeAttr.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addChannel/addChannelTypeAttr.ctl",
            "controller": 'AddChannelTypeController as ctrl',
            "size": 'md'
        },
        "addChannelThird": {
            "templateUrl": "views/pop/addChannel/addChannelThirdPartyCog.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addChannel/addChannelThirdPartyCog.ctl",
            "controller": 'AddChannelThirdController as ctrl',
            "size": 'md'
        },
        "addChannelSms": {
            "templateUrl": "views/pop/addChannel/addChannelSmsConfig.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addChannel/addChannelSmsConfig.ctl",
            "controller": 'AddChannelSmsController as ctrl',
            "size": 'md'
        },
        "addChannelCarrier": {
            "templateUrl": "views/pop/addChannel/addChannelCarrier.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addChannel/addChannelCarrier.ctl",
            "controller": 'AddChannelCarrierController as ctrl',
            "size": 'lg'
        },

        "addCart": {
            "templateUrl": "views/pop/addCart/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addCart/index.ctl",
            "controller": 'CartAddController as ctrl',
            "size": 'md'
        },
        "addCartChannelShop": {
            "templateUrl": "views/pop/addCart/addChannelShop.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addCart/addChannelShop.ctl",
            "controller": 'AddChannelShopController as ctrl',
            "size": 'md'
        },
        "addCartTracking": {
            "templateUrl": "views/pop/addCart/addTrackingInfo.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addCart/addTrackingInfo.ctl",
            "controller": 'CartAddTrackingInfoController as ctrl',
            "size": 'lg'
        },

        "addStore": {
            "templateUrl": "views/pop/addStore/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addStore/index.ctl",
            "controller": 'AddStoreController as ctrl',
            "size": 'lg'
        },

        "addTask": {
            "templateUrl": "views/pop/addTask/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addTask/index.ctl",
            "controller": 'AddTaskController as ctrl',
            "size": 'md'
        },

        "addSystemTypeInfo": {
            "templateUrl": "views/pop/addSystem/addTypeInfo.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addSystem/addTypeInfo.ctl",
            "controller": 'AddTypeInfoController as ctrl',
            "size": 'md'
        },
        "addSystemTypeAttr": {
            "templateUrl": "views/pop/addSystem/addTypeAttr.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addSystem/addTypeAttr.ctl",
            "controller": 'AddTypeAttrController as ctrl',
            "size": 'md'
        },
        "addSystemCode": {
            "templateUrl": "views/pop/addSystem/addCode.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addSystem/addCode.ctl",
            "controller": 'AddCodeController as ctrl',
            "size": 'md'
        },

        "logDetail": {
            "templateUrl": "views/pop/viewLog/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/viewLog/index.ctl",
            "controller": 'ViewLogController as ctrl',
            "size": 'lg'
        }
    }).controller('popupCtrl', function popupCtrl($scope, $uibModal, popActions, $q) {
        function openModal(config, context, contextIsResolve) {

            config.resolve = contextIsResolve ? context : {
                context: function () {
                    return context;
                }
            };

            var defer = $q.defer();
            require([config.controllerUrl], function () {

                defer.resolve($uibModal.open(config).result);
            });
            return defer.promise;
        }
        /**
         * 打开新建权限配置页面
         */
        $scope.openModifyPass= function openModifyPass(context) {
            return openModal(popActions.modifyPass, context);
        };
        /**
         * 打开新建权限配置页面
         */
        $scope.openConfig = function openConfig(context) {
            return openModal(popActions.config, context);
        };
        /**
         * 打开创建编辑页面
         */
        $scope.openCreateEdit = function openCreateEdit(context) {
            return openModal(popActions.createEdit, context);
        };

        /**
         * 打开用户/权限页面--用户管理页面的添加页面
         */
        $scope.openAddUser = function openAddUser(context) {
            return openModal(popActions.addAdminUser, context);
        };
        /**
         * 打开用户/权限页面--用户管理页面的查看权限页面
         */
        $scope.openUserAuthority = function openUserAuthority(context) {
            return openModal(popActions.userAuthority, context);
        };

        /**
         * 打开用户/权限页面--组织管理页面的新增页面
         */
        $scope.openOrg = function openOrg(context) {
            return openModal(popActions.addOrg, context);
        };

        /**
         * 打开用户/权限页面--组织管理页面的新增页面
         */
        $scope.openRole = function openRole(context) {
            return openModal(popActions.addRole, context);
        };
        /**
         * 打开用户/权限页面--角色管理页面的权限编辑页面
         */
        $scope.openRoleEdit = function openRoleEdit(context) {
            return openModal(popActions.addRoleEdit, context);
        };
        /**
         * 打开用户/权限页面--角色管理页面的复制角色页面
         */
        $scope.openRoleCopy = function openRoleCopy(context) {
            return openModal(popActions.copyRole, context);
        };

        /**
         * 打开用户/权限页面--菜单资源管理页面的新增页面
         */
        $scope.openRes = function openRes(context) {
            return openModal(popActions.addRes, context);
        };
        /**
         * 打开渠道配置页面--渠道信息页面的添加页面
         */
        $scope.openAdd = function openAdd(context) {
            return openModal(popActions.addChannel, context);
        };
        /**
         * 打开渠道配置页面--渠道类型属性管理Add页面
         */
        $scope.openAddChannelType = function openAdd(context) {
            return openModal(popActions.addChannelType, context);
        };
        /**
         * 打开渠道配置页面--第三方配置页面的添加页面
         */
        $scope.openChannelThird = function openChannelThird(context) {
            return openModal(popActions.addChannelThird, context);
        };
        /**
         * 打开渠道配置页面--短信配置页面的添加页面
         */
        $scope.openChannelSms = function openChannelSms(context) {
            return openModal(popActions.addChannelSms, context);
        };
        /**
         * 打开渠道配置页面--快递信息页面的Add页面
         */
        $scope.openChannelCarrier = function openChannelCarrier(context) {
            return openModal(popActions.addChannelCarrier, context);
        };


        /**
         * 打开Cart管理页面--Cart信息页面的添加页面
         */
        $scope.openCartAdd = function openCartAdd(context) {
            return openModal(popActions.addCart, context);
        };
        /**
         * 打开Cart管理页面--渠道Cart页面的添加页面
         */
        $scope.openCartChannelShop = function openCartChannelShop(context) {
            return openModal(popActions.addCartChannelShop, context);
        };
        /**
         * 打开Cart管理页面--Cart物流信息页面的添加页面
         */
        $scope.openCartTrackingInfo = function openCartTrackingInfo(context) {
            return openModal(popActions.addCartTracking, context);
        };


        /**
         * 打开Store管理页面的添加页面
         */
        $scope.openStoreAdd = function openStoreAdd(context) {
            return openModal(popActions.addStore, context);
        };


        /**
         * 打开任务管理页面的添加页面
         */
        $scope.openTask = function openTask(context) {
            return openModal(popActions.addTask, context);
        };

        /**
         * 打开系统配置页面--类型信息页面的添加页面
         */
        $scope.openTypeAdd = function openTypeAdd(context) {
            return openModal(popActions.addSystemTypeInfo, context);
        };
        /**
         * 打开系统配置页面--类型属性页面的添加页面
         */
        $scope.openTypeAttr = function openTypeAttr(context) {
            return openModal(popActions.addSystemTypeAttr, context);
        };
        /**
         * 打开系统配置页面--Code属性页面的添加页面
         */
        $scope.openTypeCode = function openTypeCode(context) {
            return openModal(popActions.addSystemCode, context);
        };
        /**
         * 打开日志查询页面--操作日志的日志详情页面
         */
        $scope.openLogDetail = function openLogDetail(context) {
            return openModal(popActions.logDetail, context);
        };

    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
