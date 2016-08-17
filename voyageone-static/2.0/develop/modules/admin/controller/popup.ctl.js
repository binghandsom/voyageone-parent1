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
        "addStore": {
            "templateUrl": "views/pop/addStore/index.tpl.html",
            "controllerUrl": "modules/admin/views/pop/addStore/index.ctl",
            "controller": 'AddStoreController as ctrl',
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
        /**
         * 打开Store管理页面的添加页面
         */
        $scope.openStoreAdd = function openStoreAdd(context) {
            return openModal(popActions.addStore, context);
        };

    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
