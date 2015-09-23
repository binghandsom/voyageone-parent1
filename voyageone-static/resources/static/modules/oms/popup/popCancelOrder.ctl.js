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

    omsApp.controller ('popCancelOrderController', ['$scope', 'popCancelOrderService',
        function ($scope, popCancelOrderService) {
            var _ = require ('underscore');

            // 初始化cancel type被选中.
            $scope.popCancelOrderToUseInfo.type = "1";

            /**
             * 点击OK按钮操作，取消订单或者SKU，然后更返回主页面
             */
            $scope.doOk = function () {

                if (!$scope.popForm.$valid) {
                    // TODO 显示请输入reason的message.
                }
                // 需要输入的场合，必须输入项校验
                else {

                    var data = {};
                    //订单sourceOrderId
                    data.sourceOrderId = $scope.popCancelOrderToUseInfo.sourceOrderId;
                    // 订单编号
                    data.orderNumber = $scope.popCancelOrderToUseInfo.orderNumber;
                    // Cancel的原因
                    data.reason = $scope.popCancelOrderToUseInfo.reason;

                    // 订单Cancel
                    if (_.isEqual ($scope.popCancelOrderToUseInfo.type, "1")) {

                        popCancelOrderService.doCancelOrder (data)
                            .then (function (data) {

                            $scope.$parent.setPopupOrderAction (data, $scope.popCancelOrderToUseInfo.index);
                            $scope.$parent.doPopupClose();
                            // TODO 调用主页面的方法
                            $scope.closeThisDialog ();
                        });
                    }
                    // 订单明细删除
                    else if (_.isEqual ($scope.popCancelOrderToUseInfo.type, "2")) {

                        var validationResult = false;
                        data.orderDetailsList = [];

                        _.each ($scope.popCancelOrderToUseInfo.skuInfoList, function (skuInfo) {
                            if (skuInfo.isSelected) {
                                validationResult = true;
                                data.orderDetailsList.push ({itemNumber: skuInfo.itemNumber});
                            }
                        });

                        if (!validationResult) {
                            // TODO 显示请选择一个sku的message
                        } else {

                            popCancelOrderService.doCancelOrderDetail (data)
                                .then (function (data) {

                                $scope.$parent.setPopupOrderAction (data, $scope.popCancelOrderToUseInfo.index);
                                $scope.doClose();
                            });
                        }
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
