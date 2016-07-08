/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'underscore',
    'require'
], function (_, require) {

    var popups = {
        orderInfo:{
            newShipment: {
                templateUrl: "views/pop/orderInfo/newShipment/index.tpl.html",
                controllerUrl: "views/pop/orderInfo/newShipment/index.ctl",
                controller: 'NewShipmentController as ctrl',
                backdrop: 'static',
                size: 'md'
            },
            addShipment: {
                templateUrl: "views/pop/orderInfo/addShipment/index.tpl.html",
                controllerUrl: "views/pop/orderInfo/addShipment/index.ctl",
                controller: 'AddShipmentController as ctrl',
                backdrop: 'static',
                size: 'lg'
            }
        }
        // 在这里增加你的 popup 配置
    };

    return angular.module('vms.popups', []).factory('popups', function PopupsService($uibModal, $q) {

        function open(config, context) {

            // if (context)
            config.resolve = {
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

        return {
            openNewShipment: function () {
                open(popups.orderInfo.newShipment);
            },
            openAddShipment: function () {
                open(popups.orderInfo.addShipment);
            }
            // 在这里增加你的打开函数
        };
    });
});
