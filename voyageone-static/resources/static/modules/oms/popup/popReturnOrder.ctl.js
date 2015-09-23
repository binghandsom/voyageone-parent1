/**
 * @Name:    popNewSurchargeService.js
 * @Date:    2015/3/31
 *
 * @User:    Jerry
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popReturnOrderController', ['$scope', 'popReturnOrderService',
        function ($scope, popReturnOrderService) {
            var _ = require ('underscore');

            /**
             * 点击OK按钮操作，退货SKU，然后更返回主页面
             */
            $scope.doOk = function () {

                if (!$scope.popForm.$valid) {
                    // TODO 显示请输入reason的message.
                }
                // 需要输入的场合，必须输入项校验
                else {

                    var data = {};
                    var validationResult = false;
                    //订单sourceOrderId
                    data.sourceOrderId = $scope.popReturnOrderToUseInfo.sourceOrderId;
                    data.orderNumber = $scope.popReturnOrderToUseInfo.orderNumber;
                    // Return的原因
                    data.reason = $scope.popReturnOrderToUseInfo.reason;

                    data.orderDetailsList = [];
                    data.returnShipping = false;

                    _.each ($scope.popReturnOrderToUseInfo.skuInfoList, function (skuInfo) {
                        if (skuInfo.isSelected) {
                            validationResult = true;
                            data.orderDetailsList.push ({itemNumber: skuInfo.itemNumber});
                        }
                    });

                    if (!validationResult) {
                        // TODO 显示请选择一个sku的message
                    } else {

                        popReturnOrderService.doReturnOrderDetail (data)
                            .then (function (data) {

                            $scope.$parent.setPopupOrderAction (data, $scope.popReturnOrderToUseInfo.index);
                            $scope.doClose();
                        });
                    }
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
