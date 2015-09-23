/**
 * @Name:    popBindPriceDifferenceOrder.ctl.js
 * @Date:    2015/5/8
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');

    require ('modules/oms/popup/popConfirmBindOrder.ctl');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popBindOrderController', ['$scope', 'popBindOrderService', 'ngDialog', 'omsPopupPages',
        function ($scope, popBindOrderService, ngDialog, omsPopupPages) {
            var _ = require ('underscore');

            // 初始化被选中.
            $scope.popBindOrderToUseInfo.bindNumberKind = "0";

            /**
             * 点击OK按钮操作，绑定订单，然后更返回主页面
             */
            $scope.doOk = function () {

                if (!$scope.popForm.$valid) {
                    // TODO 显示请输入reason的message.
                }
                // 需要输入的场合，必须输入项校验
                else {

                    // 弹出确认订单绑定确认对话框
                    ngDialog.open ({
                        template: omsPopupPages.popConfirmBindOrder.page,
                        controller: omsPopupPages.popConfirmBindOrder.controller,
                        scope: $scope,
                        popWindowSize: "750px"
                    });
                }
            };

            /**
             * 关闭窗口，并初始化该页面输入内容.
             */
            $scope.doClose = function () {
                $scope.$parent.doPopupClose();
                $scope.closeThisDialog();
            };

        }])
});
