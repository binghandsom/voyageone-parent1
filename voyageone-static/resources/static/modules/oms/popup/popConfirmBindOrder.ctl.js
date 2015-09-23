/**
 * @Name:    popConfirmBindOrder.ctl.js
 * @Date:    2015/5/15
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popConfirmBindOrderController', ['$scope', 'popBindOrderService',
        function ($scope, popBindOrderService) {
            var _ = require ('underscore');

            $scope.confirmOrderInfo = {};

            // 初始化被选中.
            //$scope.popBindOrderToUseInfo.bindNumberKind = "0";
            popBindOrderService.getConfrimBindPriceDiffOrder ($scope.popBindOrderToUseInfo)
                .then (function (data) {
                    $scope.confirmOrderInfo.orderInfo = data.orderInfo;
                    $scope.confirmOrderInfo.bindOrderInfo = data.bindOrderInfo;
                });

            /**
             * 点击OK按钮操作，绑定订单，然后更返回主页面
             */
            $scope.doOk = function () {

                //if (!$scope.popForm.$valid) {
                //    // TODO 显示请输入reason的message.
                //}
                //// 需要输入的场合，必须输入项校验
                //else {

                popBindOrderService.doBindPriceDiffOrder ($scope.popBindOrderToUseInfo)
                    .then (function (data) {
                    $scope.$parent.$parent.setPopupBindOrder (data.sourceOrderId);
                    $scope.$parent.doClose ();
                    $scope.doClose ();
                });
                //}
            };

            /**
             * 关闭窗口，并初始化该页面输入内容.
             */
            $scope.doClose = function () {
                $scope.closeThisDialog ();
            };

        }])
});
