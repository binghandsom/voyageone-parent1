/**
 * @Name:    currentCustomerDirective.js
 * @Date:    2015/5/4
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popAddManualOrderController'
        , ['$scope', 'popAddManualOrderService'
            , function ($scope, popAddManualOrderService) {

                var _ = require ('underscore');

                // 初始化被选择的shipto 信息.
                $scope.selectShiptoInfo = {};
                // 用与判断是否有数据被选中
                $scope.isSelectHisotyInfo = true;
                // 判断是否使用该订单的ship to信息
                $scope.chkUseThisShipToAddress = false;
                // 判断是否使用被选择的ship to信息
                $scope.chkUseSelectAddress = false;

                /**
                 * 如果从order detail 画面传递过来的ordernumber 不为空
                 */
                if (!_.isEmpty ($scope.popAddManualOrderToUseInfo.orderNumber)) {

                    var data = {};
                    data.orderNumber = $scope.popAddManualOrderToUseInfo.orderNumber;

                    // get the customer info and order history info by orderNumber.
                    popAddManualOrderService.doGetOrderList (data)
                        .then (function (data) {
                        // 保存该订单的shipto信息
                        $scope.curOrderShipInfo = data.curOrderShipInfo;
                        // 保存该用户的历史订单
                        $scope.orderHistoryList = data.orderHistoryList;
                        // 保存改用的用户信息
                        $scope.curCustomerSoldToInfo = data.customerInfo;
                    });
                }

                /**
                 * 点击画面上的Continue按钮.
                 */
                $scope.doSetCustomer = function () {
                    var orderInfo = {};
                    //将该订单的sourceOrderId 传给下一个画面.
                    orderInfo.sourceOrderId = $scope.popAddManualOrderToUseInfo.sourceOrderId;
                    orderInfo.orderChannelId = $scope.popAddManualOrderToUseInfo.orderChannelId;
                    orderInfo.customerInfo = $scope.curCustomerSoldToInfo;
                    orderInfo.cartId = $scope.popAddManualOrderToUseInfo.cartId;

                    if ($scope.chkUseThisShipToAddress) {
                        orderInfo.shipToInfo = $scope.curOrderShipInfo;
                        // TODO 因为名称不一致，所以需要重新付一次值
                        orderInfo.shipToInfo.lastName = orderInfo.shipToInfo.name;
                    } else if ($scope.chkUseSelectAddress) {
                        orderInfo.shipToInfo = $scope.selectShiptoInfo;
                        // TODO 因为名称不一致，所以需要重新付一次值
                        orderInfo.shipToInfo.lastName = orderInfo.shipToInfo.name;
                    } else {
                        $scope.curOrderShipInfo = {};
                        $scope.selectShiptoInfo = {};
                    }

                    // 调用主页面的操作
                    $scope.$parent.setPopupAddManualOrderInfo (orderInfo);
                    $scope.doClose();
                };

                /**
                 * 选中orderhisotory中的数据.
                 * @param orderHistoryInfo
                 * @param index
                 */
                $scope.selectOrderHistory = function (orderHistoryInfo, index) {
                    $scope.selectShiptoInfo = orderHistoryInfo;
                    $scope.selectShiptoInfo.index = index;
                    $scope.isSelectHisotyInfo = false;
                };

                /**
                 * when the use select address has been checked, then set the use this ship to address to false.
                 */
                $scope.$watch ('chkUseSelectAddress', function () {
                    if ($scope.chkUseSelectAddress) {
                        $scope.chkUseThisShipToAddress = false;
                    }
                });

                /**
                 * when the use this ship to address has been checked, then set the use select address to false.
                 */
                $scope.$watch ('chkUseThisShipToAddress', function () {
                    if ($scope.chkUseThisShipToAddress) {
                        $scope.chkUseSelectAddress = false;
                    }
                });

                /**
                 * 关闭窗口，并初始化该页面输入内容.
                 */
                $scope.doClose = function () {
                    $scope.$parent.doPopupClose();
                    $scope.closeThisDialog();
                };

            }])
});		