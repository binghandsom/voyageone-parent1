/**
 * 统一管理弹出框的定义信息
 * Created by LinAn.Bin on 15/12/7.
 */

define([
    'cms'
], function (cms) {

    angular.module('com.voyageone.popups', []).constant('popActions', {
        search:{
            batchApprove:{
                "templateUrl": "views/pop/items/search/index.tpl.html",
                "controllerUrl": "modules/cms-us/views/pop/items/search/index.ctl",
                "controller": 'batchApproveController as ctrl',
                "size": 'md'
            }
        }
    }).controller('popupCtrl', function popupCtrl($scope, $uibModal, popActions, $q) {

        function openModal(config, context, contextIsResolve) {

            config.resolve = contextIsResolve ? context : {
                context: function () {
                    return context;
                }
            };

            let defer = $q.defer();
            require([config.controllerUrl], function () {

                defer.resolve($uibModal.open(config).result);
            });

            return defer.promise;
        }

        /**
         *  批量approve模态框
         */
        $scope.openBatchApprove = function(context) {
            return openModal(popActions.search.batchApprove,context);
        };


    }).factory('popups', function ($controller, $rootScope) {

        var popupScope = $rootScope.$new();

        popupScope.$controller = $controller('popupCtrl', {$scope: popupScope});

        return popupScope;
    });
});
