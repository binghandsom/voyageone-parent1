/**
 * @Name:    popRefundService.js
 * @Date:    2015/5/23
 *
 * @User:    Will
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');
    require ('components/services/message.service');

    omsApp.controller ('popRefundOrderCNController', ['$scope', 'omsType', 'popRefundOrderService','omsCommonService','messageService','messageKeys','cartId',
        function ($scope, omsType, popRefundOrderService, omsCommonService, messageService, messageKeys, cartId) {
            var _ = require ('underscore');

            /**
             * 退款页面初始化
             */
            var data = {};

            //同意退款按钮（阿里以外）
            $scope.isShowLocalRefundAgree = false;

            data.sourceOrderId = $scope.popRefundOrderToUseInfo.sourceOrderId;
            data.cartId = $scope.popRefundOrderToUseInfo.cartId;
            data.isShowHistoryOnly = $scope.popRefundOrderToUseInfo.isShowHistoryOnly;
            //popRefundOrderService.doInitRefund (data)
            //    .then (function (data) {
            //    $scope.popRefundOrderToUseInfo.refundList = data.orderRefundsList;
            //    $scope.popRefundOrderToUseInfo.refundInfo = data.refundInfo;
            //    $scope.popRefundOrderToUseInfo.refundMessagesList = data.refundMessagesList;
            //    $scope.popRefundOrderToUseInfo.refundsStatus = data.refundsStatus;
            //    $scope.currentrefundId = data.refundInfo.refundId;
            //});

            /**
             * 点击取消按钮操作，取消输入
             */
            $scope.doCancel = function () {
                $scope.doClose();
            }

            /**
             * 点击OK按钮操作，同意退款
             */
            $scope.doAgreeRefund = function () {
                var refundInfo= {};
                refundInfo.sourceOrderId = $scope.popRefundOrderToUseInfo.sourceOrderId;
                refundInfo.orderChannelId = $scope.popRefundOrderToUseInfo.orderChannelId;
                refundInfo.cartId = $scope.popRefundOrderToUseInfo.cartId;
                refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                refundInfo.refundFee = $scope.popRefundOrderToUseInfo.omsRefund;

                var data = {};
                data.refundInfo = refundInfo;

                popRefundOrderService.doAgreeRefundCN (data)
                    .then (function (data) {
                    $scope.$parent.setPopupOrderActionForRefundCN (data);
                    $scope.doClose();
                });
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
