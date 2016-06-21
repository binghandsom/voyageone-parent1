/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'cms',
    'underscore',
    'require'
], function (cms, _, require) {

    var popups = {
        // 在这里增加你的 popup 配置
    };

    cms.factory('popups', function PopupsService ($uibModal, $q) {

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
            // 在这里增加你的打开函数
        };
    });
});
