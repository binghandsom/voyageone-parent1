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

    omsApp.controller ('popAddDiscountOrderController',
        ['$scope', 'popDiscountOrderService',
            function ($scope, popDiscountOrderService) {
                var _ = require ('underscore');

                // 初始化被选择的是order 还是 sku.
                $scope.popDiscountOrderToUseInfo.type = "1";

                // TODO 这个方式不是很好,以后再改一下.
                /**
                 * 当newPriceDiscount发生变化的时候，重新计算该订单的价格.
                 * @param index
                 */
                $scope.calculatePrice = function (index) {

                    var orderInfo = $scope.popDiscountOrderToUseInfo.skuInfoList[index];
                    var discount = _.isNull (orderInfo.discount) ? 0.00 : parseFloat (orderInfo.discount);

                    //if (orderInfo.newPriceDiscount.length > 0) {

                    if (!orderInfo.newDiscount.startWith ("-")) {
                        orderInfo.newDiscount = isNaN (parseFloat (orderInfo.newDiscount)) ? 0.00 : parseFloat (orderInfo.newDiscount);
                        orderInfo.finalDiscount = discount - orderInfo.newDiscount;
                        orderInfo.finalPrice = parseFloat (orderInfo.price) - orderInfo.newDiscount;
                        $scope.popDiscountOrderToUseInfo.skuInfoList[index] = orderInfo;
                    } else {
                        if (orderInfo.newDiscount.length >= 2) {
                            orderInfo.newDiscount = isNaN (parseFloat (orderInfo.newDiscount)) ? 0.00 : parseFloat (orderInfo.newDiscount);
                            orderInfo.finalDiscount = discount - orderInfo.newDiscount;
                            orderInfo.finalPrice = parseFloat (orderInfo.price) - orderInfo.newDiscount;
                            $scope.popDiscountOrderToUseInfo.skuInfoList[index] = orderInfo;
                        }
                    }
                    //}
                };

                /**
                 * 执行discount操作
                 */
                $scope.doOk = function () {

                    if (!$scope.popForm.$valid) {
                        // TODO 显示请输入reason的message.
                    } else {

                        var data = {};
                        data.sourceOrderId = $scope.popDiscountOrderToUseInfo.sourceOrderId;
                        data.orderNumber = $scope.popDiscountOrderToUseInfo.orderNumber;

                        if (_.isEqual ($scope.popDiscountOrderToUseInfo.type, "1")) {

                            if (!_.isEqual ($scope.popDiscountOrderToUseInfo.discount, "0")) {

                                data.adjustmentType = "2";
                                data.adjustmentNumber = parseFloat ($scope.popDiscountOrderToUseInfo.discount) * -1;
                                data.adjustmentReason = $scope.popDiscountOrderToUseInfo.reason;
                                data.adjustmentDiscountType = "2";

                                popDiscountOrderService.doDiscountOrder (data)
                                    .then (function (data) {

                                    $scope.$parent.setPopupOrderAction (data, $scope.popDiscountOrderToUseInfo.index);
                                    $scope.doClose ();
                                });
                            } else {
                                // TODO 显示入力的值不能为0的信息.
                            }
                        } else if (_.isEqual ($scope.popDiscountOrderToUseInfo.type, "2")) {

                            data.reason = $scope.popDiscountOrderToUseInfo.reason;

                            data.orderDetailsList = [];
                            //dicount不等于空的数据抽出
                            _.each ($scope.popDiscountOrderToUseInfo.skuInfoList, function (skuInfo) {

                                if (!_.isEmpty (skuInfo.newDiscount.toString ())
                                    && !_.isEqual (skuInfo.newDiscount.toString (), "0")) {
                                    data.orderDetailsList.push (
                                        {
                                            itemNumber: skuInfo.itemNumber,
                                            sku: skuInfo.sku,
                                            pricePerUnit: parseFloat (skuInfo.newDiscount) * -1
                                        })
                                }
                            });

                            if (data.orderDetailsList.length > 0) {

                                popDiscountOrderService.doDiscountOrderDetail (data)
                                    .then (function (data) {

                                    $scope.$parent.setPopupOrderAction (data, $scope.popDiscountOrderToUseInfo.index);
                                    $scope.doClose ();
                                })
                            } else {
                                // TODO 至少得给一个 orderDetail赋值
                            }
                        }
                    }
                };

                /**
                 * 关闭窗口，并初始化该页面输入内容.
                 */
                $scope.doClose = function () {
                    $scope.$parent.doPopupClose ();
                    $scope.closeThisDialog ();
                };
            }])
});
