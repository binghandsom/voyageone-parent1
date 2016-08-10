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
            "tmOrderChannel": {
                "templateUrl": "views/pop/config/index.tpl.html",
                "controllerUrl": "modules/admin/views/pop/config/index.ctl",
                "controller": 'ConfigController as ctrl',
                "size":'lg'
            }
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
         * 打开新建权限页面
         * @type {openAuthority}
         */
        $scope.openConfig = function openConfig(context) {
            return openModal(popActions.config.tmOrderChannel, context);
        };

    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
