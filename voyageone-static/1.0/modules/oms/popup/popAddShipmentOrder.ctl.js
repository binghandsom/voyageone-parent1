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

    omsApp.controller ('popAddShipmentOrderController',
        ['$scope', 'popShippingOrderService',
            function ($scope, popShippingOrderService) {
                var _ = require ('underscore');

                // TODO 这个方式不是很好,以后再改一下.
                /**
                 * 当newPriceDiscount发生变化的时候，重新计算该订单的价格.
                 */
                $scope.calculatePrice = function () {

                    var newShippingFare = _.isEmpty ($scope.popShippingOrderToUseInfo.amount) ? 0.00 : $scope.popShippingOrderToUseInfo.amount;
                    $scope.popShippingOrderToUseInfo.newFinalShippingTotal = $scope.popShippingOrderToUseInfo.finalShippingTotal;

//                    if (newShippingFare.length > 0) {

                        if (!newShippingFare.startWith ("-")) {
                            newShippingFare = isNaN (parseFloat (newShippingFare)) ? 0.00 : parseFloat (newShippingFare);
                            $scope.popShippingOrderToUseInfo.newFinalShippingTotal = parseFloat (parseFloat ($scope.popShippingOrderToUseInfo.finalShippingTotal) + newShippingFare).toFixed (2);
                        } else {
                            if (newShippingFare.length >= 2) {
                                newShippingFare = isNaN (parseFloat (newShippingFare)) ? 0.00 : parseFloat (newShippingFare);
                                $scope.popShippingOrderToUseInfo.newFinalShippingTotal = parseFloat (parseFloat ($scope.popShippingOrderToUseInfo.finalShippingTotal) + newShippingFare).toFixed (2);
                            }
                        }
//                    } else {
//                    }
                };

                /**
                 * 操作shipping fare.
                 */
                $scope.doOk = function () {
                    // 需要输入的场合，必须输入项校验
                    if (!$scope.popForm.$valid) {
                        // TODO 弹出错误message
                    } else {

                        var data = {};
                        data.sourceOrderId = $scope.popShippingOrderToUseInfo.sourceOrderId
                        data.orderNumber = $scope.popShippingOrderToUseInfo.orderNumber;
                        data.adjustmentType = "4";
                        data.adjustmentNumber = parseFloat ($scope.popShippingOrderToUseInfo.amount).toFixed (2);
                        data.adjustmentReason = $scope.popShippingOrderToUseInfo.reason;

                        popShippingOrderService.doShippingFareOrder (data)
                            .then (function (data) {

                            $scope.$parent.setPopupOrderAction (data, $scope.popShippingOrderToUseInfo.index);
                            $scope.doClose();
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
