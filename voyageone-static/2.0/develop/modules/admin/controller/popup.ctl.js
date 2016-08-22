/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'admin',
    'underscore',
    'modules/admin/enums/MappingTypes'
], function (admin, _, MappingTypes) {

    admin.constant('popActions', {
        "config": {
            "templateUrl": "views/pop/config/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/config/index.ctl",
            "controller": 'ConfigController as ctrl',
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
        "createEdit": {
            "templateUrl": "views/pop/createEdit/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/createEdit/index.ctl",
            "controller": 'CreateEditController as ctrl',
            "size": 'md'
        },
        "addCart": {
            "templateUrl": "views/pop/addCart/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addCart/index.ctl",
            "controller": 'CartAddController as ctrl',
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
        "addType": {
            "templateUrl": "views/pop/addType/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addType/index.ctl",
            "controller": 'AddTypeController as ctrl',
            "size": 'md'
        },
        "addTask": {
            "templateUrl": "views/pop/addTask/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addTask/index.ctl",
            "controller": 'AddTaskController as ctrl',
            "size": 'md'
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
        $scope.openConfig = function openConfig(context) {
            return openModal(popActions.config, context);
        };
        /**
         * 打开创建编辑页面
         */
        $scope.openAdd = function openAdd(context) {
            return openModal(popActions.addChannel, context);
        };
        /**
         * 打开渠道类型属性管理Add页面
         */
        $scope.openAddChannelType = function openAdd(context) {
            return openModal(popActions.addChannelType, context);
        };
        /**
         * 打开创建编辑页面
         */
        $scope.openCreateEdit = function openCreateEdit(context) {
            return openModal(popActions.createEdit, context);
        };
        /**
         * 打开Cart管理页面的添加页面
         */
        $scope.openCartAdd = function openCartAdd(context) {
            return openModal(popActions.addCart, context);
        };
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
         * 打开Type管理页面的添加页面
         */
        $scope.openTypeAdd = function openTypeAdd(context) {
            return openModal(popActions.addType, context);
        };
        /**
         * 打开Type管理页面的添加页面
         */
        $scope.openChannelThird = function openChannelThird(context) {
            return openModal(popActions.addChannelThird, context);
        };
        /**
         * 打开Type管理页面的添加页面
         */
        $scope.openChannelSms = function openChannelSms(context) {
            return openModal(popActions.addChannelSms, context);
        };
        $scope.openChannelCarrier = function openChannelCarrier(context) {
            return openModal(popActions.addChannelCarrier, context);
        };
        $scope.openTask = function openTask(context) {
            return openModal(popActions.addTask, context);
        };

    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
