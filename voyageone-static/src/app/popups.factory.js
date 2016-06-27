/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'underscore',
    'require'
], function (_, require) {

    var popups = {
        test: {
            templateUrl: "views/pop/bulkUpdate/addToPromotion.tpl.html",
            controllerUrl: "modules/cms/views/pop/bulkUpdate/addToPromotion.ctl",
            controller: 'popAddToPromotionCtl',
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
            test: function () {
                open(popups.test);
            }
            // 在这里增加你的打开函数
        };
    });
});
