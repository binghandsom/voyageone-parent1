/**
 * @Name:    popMessage.ctl.js
 * @Date:    2015/5/10
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    //require ('modules/oms/popup/popup.service');

    omsApp.controller ('popMessageController', ['$scope', /* 'popBindOrderService',*/
        function ($scope/*, popBindOrderService*/) {
            var _ = require ('underscore');

            /**
             * 点击OK按钮操作，绑定订单，然后更返回主页面
             */
            $scope.doOk = function () {
                $scope.$parent.setPopupMessage ();
                $scope.doClose ();
            };

            /**
             * 关闭这个 dialog
             */
            $scope.doClose = function () {
                // 初始化popup页面数据.
                $scope.$parent.doPopupClose ();
                $scope.closeThisDialog ();
            }

        }])
});
