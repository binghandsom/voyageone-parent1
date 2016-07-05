/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'underscore',
    'require'
], function (_, require) {

    var popups = {
        addShipment: {
            templateUrl: "views/pop/addShipment/index.tpl.html",
            controllerUrl: "views/pop/addShipment/index.ctl",
            controller: 'AddShipmentController as ctrl',
            backdrop: 'static',
            size: 'md'
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
            openAddShipment: function () {
                open(popups.addShipment);
            }
            // 在这里增加你的打开函数
        };
    });
});
