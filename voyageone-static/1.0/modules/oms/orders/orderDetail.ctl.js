/**
 * @Name:    orderinfoController.js
 * @Date:    2015/3/5
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    var commonUtil = require ('components/util/commonUtil');

    require ('components/services/message.service');
    require ('modules/oms/orders/orders.service');
    require ('modules/oms/common/common.service');

    // 该画面popup页面的ctl调用
    require ('modules/oms/popup/popMessage.ctl');
    require ('modules/oms/popup/popBindOrder.ctl');
    require ('modules/oms/popup/popAddManualOrder.ctl');
    require ('modules/oms/popup/popCancelOrder.ctl');
    require ('modules/oms/popup/popReturnOrder.ctl');
    require ('modules/oms/popup/popRefundOrder.ctl');
    require ('modules/oms/popup/popRefundOrderCN.ctl');
    require ('modules/oms/popup/popAddDiscountOrder.ctl');
    require ('modules/oms/popup/popAddShipmentOrder.ctl');
    require ('modules/oms/popup/popEditSoldToAddress.ctl');
    require ('modules/oms/popup/popEditShipToAddress.ctl');
    require ('modules/oms/popup/popAddNoteOrder.ctl');

    // 该画面需要和其他ctl交互的service引用
    require ('modules/oms/services/orderDetailInfo.service');
    require ('modules/oms/services/addNewOrder.service');
    require ('modules/oms/services/orderSearch.service');

    omsApp.controller ('orderDetailController',
        ['$scope', '$q', '$routeParams', '$route', '$location', 'ngDialog', 'messageService', 'orderDetailService', 'orderDetailInfoService'
            , 'newOrderService', 'messageKeys', 'omsType', 'omsCommonService', 'omsRoute', 'omsPopupPages', 'orderStatus', 'searchOrderService', 'searchService','cartId','platformId',
            function ($scope, $q, $routeParams, $route, $location, ngDialog, messageService, orderDetailService, orderDetailInfoService, newOrderService
                , messageKeys, omsType, omsCommonService, omsRoute, omsPopupPages, orderStatus, searchOrderService, searchService, cartId, platformId) {

                var _ = require ('underscore');

                // 历史订单Tree
                $scope.orderHistoryTreeList = [];
                $scope.currentOrderNumber = "";
//                $scope.orderHistoryTree = {};
                //定义变量for search order list
                $scope.searchOrder = {};
                $scope.searchOrder.orderInfoListForOrderDetail = [];
                $scope.searchOrder.searOrderCurrentPage = 0;
                $scope.searchOrder.searchOrderCurrentPageCount = 0;
                $scope.searchOrder.searchOrderCurrentPageRows = 0;
                // 初始化被选中的订单
                $scope.selectedOrder = {};
                $scope.selectedOrder.orderInfo = {};
                // 初始化传递给popup的变量
                $scope.popupMessage = {};
                $scope.popBindOrderToUseInfo = {};
                $scope.popAddManualOrderToUseInfo = {};
                $scope.popCancelOrderToUseInfo = {};
                $scope.popReturnOrderToUseInfo = {};
                $scope.popDiscountOrderToUseInfo = {};
                $scope.popShippingOrderToUseInfo = {};
                $scope.popSoldToOrderToUseInfo = {};
                $scope.popAddNoteToUseInfo = {};
                $scope.popRefundOrderToUseInfo = {};

                $scope.cartId = cartId;
                $scope.platformId = platformId;
                $scope.orderStatus = orderStatus;
                // 自定义变量
                /* 用来标示填出messagepopup页面 */
                var popupMessageType = {
                    lockOrUnlockOrder: "1",
                    approveOrder: "2",
                    cancelClientOrder: "3"
                };

                /**
                 * 初始化order detail页面.
                 */
                $scope.initialize = function () {

                    if (_.isEmpty ($routeParams.sourceOrderId)) {
                        messageService.alertMessage (messageKeys.ID_200002);
                    } else {
                        orderDetailService.doGetCodeList ()
                            .then (function (data) {
                            $scope.invoiceStatusList = omsCommonService.doGetOneCodeFromList (data, omsType.invoice);
                            $scope.shippingMethodList = omsCommonService.doGetOneCodeFromList (data, omsType.shippingMethod);
                            $scope.invoiceKindList = omsCommonService.doGetOneCodeFromList (data, omsType.invoiceKind);
                        })
                            .then (function () {
                            getOrderDetailInfo ($routeParams.sourceOrderId);
                        })
                    }
                };

                /**
                 * 刷新该页面.
                 */
                $scope.doReLoadOrder = function () {
                    if (!_.isEmpty ($scope.mainOrderInfo.sourceOrderId)) {

                        getOrderDetailInfo ($scope.mainOrderInfo.sourceOrderId);
                    }
                };

                /**
                 * 调用popup画面: 绑定订单.
                 */
                $scope.doPopupBindOrder = function () {
                    $scope.popBindOrderToUseInfo = {};
                    $scope.popBindOrderToUseInfo.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                    ngDialog.open ({
                        template: omsPopupPages.popBindOrder.page,
                        controller: omsPopupPages.popBindOrder.controller,
                        scope: $scope
                    });
                };

                /**
                 * 设定search order list，并重新刷新页面
                 * @param sourceOrderId
                 */
                $scope.setPopupBindOrder = function (sourceOrderId) {
                    fnResetSearchOrderList ($scope.popBindOrderToUseInfo.orderNumber, sourceOrderId);
                    fnGotoOrderDetailPage (sourceOrderId);
                };

                /**
                 * 调用popup画面: 新订单编辑前客户ship to 选择画面.
                 */
                $scope.doPopupAddManualOrder = function () {
                    $scope.popAddManualOrderToUseInfo = {};
                    $scope.popAddManualOrderToUseInfo.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                    $scope.popAddManualOrderToUseInfo.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;
                    $scope.popAddManualOrderToUseInfo.orderChannelId = $scope.selectedOrder.orderInfo.orderChannelId;
                    $scope.popAddManualOrderToUseInfo.cartId = $scope.selectedOrder.orderInfo.cartId;

                    ngDialog.open ({
                        template: omsPopupPages.popAddManualOrder.page,
                        controller: omsPopupPages.popAddManualOrder.controller,
                        scope: $scope,
                        popWindowSize: "850px"
                    });
                };

                /**
                 * 将选择的cutomer信息赋值给service，并天传到ordernew页面
                 * @param orderInfo
                 */
                $scope.setPopupAddManualOrderInfo = function (orderInfo) {
                    newOrderService.setCurrentOrderInfo (orderInfo);
                    fnGotoNewOrderPage ();
                };

                /**
                 * 锁住该订单.
                 */
                $scope.doLockOrder = function () {

                    $scope.popupMessage.lockFlag = !$scope.mainOrderInfo.localShipOnHold;
                    $scope.popupMessage.sourceOrderId = $scope.mainOrderInfo.sourceOrderId;
                    $scope.popupMessage.type = popupMessageType.lockOrUnlockOrder;
                    if ($scope.popupMessage.lockFlag) {
                        $scope.popupMessage.message = messageService.getMessageByKey (messageKeys.ID_000003);
                    } else {
                        $scope.popupMessage.message = messageService.getMessageByKey (messageKeys.ID_000004);
                    }

                    ngDialog.open ({
                        template: omsPopupPages.popMessage.page,
                        controller: omsPopupPages.popMessage.controller,
                        scope: $scope
                    });
                };

                /**
                 * 返回到该页面的前一个页面.
                 */
                $scope.doBack = function () {
                    if (!_.isEmpty (orderDetailInfoService.getOrderBeforePageUrl ())) {

                        $location.path (orderDetailInfoService.getOrderBeforePageUrl ())
                    } else {
                        $location.path (omsRoute.oms_default_index.hash);
                    }
                };

                /**
                 * Approve该订单.
                 */
                $scope.doApprove = function (orderInfo, index) {
                    $scope.popupMessage = {};
                    $scope.popupMessage.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popupMessage.orderNumber = orderInfo.orderNumber;
                    $scope.popupMessage.index = index;
                    $scope.popupMessage.type = popupMessageType.approveOrder;
                    $scope.popupMessage.message = messageService.getMessageByKey (messageKeys.ID_000002);

                    ngDialog.open ({
                        template: omsPopupPages.popMessage.page,
                        controller: omsPopupPages.popMessage.controller,
                        scope: $scope
                    });
                };

                /**
                 * dialog确认操作.
                 */
                $scope.setPopupMessage = function () {
                    switch ($scope.popupMessage.type) {
                        case popupMessageType.lockOrUnlockOrder:
                            setPopupLockOrder ();
                            break;
                        case popupMessageType.approveOrder:
                            setPopupApproveOrder ();
                            break;
                        case popupMessageType.cancelClientOrder:
                            setCancelClientOrder ();
                            break;
                        default :
                            break;
                    }
                };

                /**
                 * 调用popup画面: Cancel
                 * @param orderInfo
                 * @param index
                 */
                $scope.doPopupCancel = function (orderInfo, index) {

                    // 订单只有处于Approved状态的时候才能cancel.
                    //if (_.isEqual (orderInfo.orderStatus, orderStatus.Approved)) {

                    //将选中orderinfo传递给子画面
                    $scope.popCancelOrderToUseInfo = {};
                    $scope.popCancelOrderToUseInfo.index = index;
                    $scope.popCancelOrderToUseInfo.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popCancelOrderToUseInfo.orderNumber = orderInfo.orderNumber;
                    $scope.popCancelOrderToUseInfo.currencyType = orderInfo.currencyType;
                    $scope.popCancelOrderToUseInfo.skuInfoList = [];

                    _.each (orderInfo.orderDetailsList, function (orderDetailInfo) {

                        var orderDetailForPopup = {};
                        angular.copy (orderDetailInfo, orderDetailForPopup);

                        // 如果orderDetail不是approved或者Shipped状态则不能cancel.
                        if ((orderDetailForPopup.adjustment
                                && !_.isEqual (orderDetailForPopup.status, orderStatus.Cancelled))
                            || _.isEqual (orderDetailForPopup.status, orderStatus.Approved)
                            || _.isEqual (orderDetailForPopup.status, orderStatus.InProcessing)) {
                            orderDetailForPopup.isSelected = false;
                            $scope.popCancelOrderToUseInfo.skuInfoList.push (orderDetailForPopup);
                        }
                    });

                    ngDialog.open ({
                        template: omsPopupPages.popCancelOrder.page,
                        controller: omsPopupPages.popCancelOrder.controller,
                        scope: $scope,
                        popWindowSize: "550px"
                    });
                    //} else {
                    //    // TODO 显示不能cancel订单消息.
                    //}
                };

                /**
                 * 调用popup画面: Return
                 * @param orderInfo
                 * @param index
                 */
                $scope.doPopupReturn = function (orderInfo, index) {

                    // 订单只有处于Shipped状态的时候才能退货.
                    //if (_.isEqual (orderInfo.orderStatus, orderStatus.Shipped)) {

                    //将选中orderinfo传递给子画面
                    $scope.popReturnOrderToUseInfo = {};
                    $scope.popReturnOrderToUseInfo.index = index;
                    $scope.popReturnOrderToUseInfo.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popReturnOrderToUseInfo.orderNumber = orderInfo.orderNumber;
                    $scope.popReturnOrderToUseInfo.currencyType = orderInfo.currencyType;
                    $scope.popReturnOrderToUseInfo.skuInfoList = [];

                    // TODO 判断该sku是可以return的产品
                    _.each (orderInfo.orderDetailsList, function (orderDetailInfo) {

                        var orderDetailForPopup = {};
                        angular.copy (orderDetailInfo, orderDetailForPopup);

                        // 订单只有处于Shipped状态的时候才能退货.
                        if ((_.isEqual (orderDetailForPopup.status, orderStatus.Shipped) ||
                            _.isEqual (orderDetailForPopup.status, orderStatus.InProcessing) ||
                            _.isEqual (orderDetailForPopup.status, orderStatus.Approved))
                            && !orderDetailForPopup.adjustment) {
                            orderDetailForPopup.isSelected = false;
                            $scope.popReturnOrderToUseInfo.skuInfoList.push (orderDetailForPopup);
                        }
                    });

                    ngDialog.open ({
                        template: omsPopupPages.popReturnOrder.page,
                        controller: omsPopupPages.popReturnOrder.controller,
                        scope: $scope,
                        popWindowSize: "550px"
                    });
                    //} else {
                    //    // TODO 显示不能cancel订单消息.
                    //}
                };

                /**
                 * 调用popup画面: Discount.
                 * @param orderInfo
                 * @param index
                 */
                $scope.doPopupDiscount = function (orderInfo, index) {

                    //// 订单不处于Cancel状态都是可以加减Discount..
                    //if (!_.isEqual (orderInfo.orderStatus, orderStatus.Cancelled)) {

                    //将选中orderinfo传递给子画面
                    $scope.popDiscountOrderToUseInfo = {};
                    $scope.popDiscountOrderToUseInfo.index = index;
                    $scope.popDiscountOrderToUseInfo.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popDiscountOrderToUseInfo.orderNumber = orderInfo.orderNumber;
                    $scope.popDiscountOrderToUseInfo.currencyType = orderInfo.currencyType;
                    $scope.popDiscountOrderToUseInfo.skuInfoList = [];

                    _.each (orderInfo.orderDetailsList, function (orderDetailInfo) {

                        var orderDetailForPopup = {};
                        angular.copy (orderDetailInfo, orderDetailForPopup);

                        // 订单只有处于Shipped状态的时候才能退货.
                        if (!_.isEqual (orderDetailForPopup.status, orderStatus.Cancelled)
                            && !_.isEqual (orderDetailForPopup.status, orderStatus.Returned)
                            && !orderDetailForPopup.adjustment) {
                            orderDetailForPopup.newDiscount = "";
                            orderDetailForPopup.finalDiscount = orderDetailForPopup.discount;
                            orderDetailForPopup.finalPrice = orderDetailForPopup.price;
                            $scope.popDiscountOrderToUseInfo.skuInfoList.push (orderDetailForPopup);
                        }
                    });

                    ngDialog.open ({
                        template: omsPopupPages.popAddDiscountOrder.page,
                        controller: omsPopupPages.popAddDiscountOrder.controller,
                        scope: $scope,
                        popWindowSize: "750px"
                    });
                    //} else {
                    //    // TODO 显示不能cancel订单消息.
                    //}
                };

                /**
                 * 调用popup画面: Shipment.
                 * @param orderInfo
                 * @param index
                 */
                $scope.doPopupShipment = function (orderInfo, index) {

                    $scope.popShippingOrderToUseInfo = {};
                    angular.copy (orderInfo, $scope.popShippingOrderToUseInfo);
                    $scope.popShippingOrderToUseInfo.index = index;

                    ngDialog.open ({
                        template: omsPopupPages.popAddShipmentOrder.page,
                        controller: omsPopupPages.popAddShipmentOrder.controller,
                        scope: $scope
                    });
                };

                /**
                 * 调用popup画面: Refund.
                 * @param orderInfo 主订单信息
                 * @param isShowHistoryOnly 仅显示履历
                 */
                $scope.doPopupRefund = function (orderInfo,isShowHistoryOnly) {

                    //将mainorderinfo传递给子画面
                    $scope.popRefundOrderToUseInfo = {};
                    //$scope.popRefundOrderToUseInfo.index = index;
                    $scope.popRefundOrderToUseInfo.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popRefundOrderToUseInfo.cartId = orderInfo.cartId;
                    $scope.popRefundOrderToUseInfo.orderNumber = $scope.ordersList[0].orderNumber;
                    $scope.popRefundOrderToUseInfo.customerRefund = orderInfo.customerRefund;
                    $scope.popRefundOrderToUseInfo.omsRefund = orderInfo.expected - orderInfo.finalGrandTotal;
                    $scope.popRefundOrderToUseInfo.isShowHistoryOnly = isShowHistoryOnly;
                    ngDialog.open ({
                        template: omsPopupPages.popRefundOrder.page,
                        controller: omsPopupPages.popRefundOrder.controller,
                        scope: $scope,
                        popWindowSize: "750px"
                    });
                };

                /**
                 * 调用popup画面: Refund.
                 * @param orderInfo 主订单信息
                 * @param isShowHistoryOnly 仅显示履历
                 */
                $scope.doPopupRefundCN = function (orderInfo,isShowHistoryOnly) {

                    //将mainorderinfo传递给子画面
                    $scope.popRefundOrderToUseInfo = {};
                    //$scope.popRefundOrderToUseInfo.index = index;
                    $scope.popRefundOrderToUseInfo.sourceOrderId = orderInfo.sourceOrderId;
                    $scope.popRefundOrderToUseInfo.orderChannelId = orderInfo.orderChannelId;
                    $scope.popRefundOrderToUseInfo.cartId = orderInfo.cartId;
                    $scope.popRefundOrderToUseInfo.orderNumber = $scope.ordersList[0].orderNumber;
                    $scope.popRefundOrderToUseInfo.customerRefund = orderInfo.customerRefund;
                    $scope.popRefundOrderToUseInfo.omsRefund = orderInfo.expected - orderInfo.finalGrandTotal;
                    $scope.popRefundOrderToUseInfo.isShowHistoryOnly = isShowHistoryOnly;
                    ngDialog.open ({
                        template: omsPopupPages.popRefundOrderCN.page,
                        controller: omsPopupPages.popRefundOrderCN.controller,
                        scope: $scope,
                        popWindowSize: "750px"
                    });
                };

                /**
                 * 更新被操作的orderInfo,并刷新
                 * @param data
                 * @param index
                 */
                $scope.setPopupOrderAction = function (data, index) {

                    // 将现有order的shippingList保存并设置回被更新后的order中
                    var orderShippingList = $scope.ordersList[index].orderShippingList;

                    $scope.ordersList[index] = data.orderInfo;
                    $scope.ordersList[index].orderDetailsList = fnClearNotShowOrderDetail (data.orderInfo.orderDetailsList);
                    $scope.ordersList[index].orderShippingList = orderShippingList;

                    $scope.selectedOrder.orderInfo = $scope.ordersList[index];

                    // 该web订单的notes信息
                    fnReloadNotesInfo (data.orderNotesList);

                    // 更新显示order transaction list.
                    $scope.orderTransactionsList = data.orderTransactionsList;

                    // 更新显示mainOrderInfo
                    $scope.mainOrderInfo = data.mainOrderInfo;
                };

                /**
                 * 更新被操作的orderInfo,并刷新（退款操作）
                 * @param data
                 * @param index
                 */
                $scope.setPopupOrderActionForRefund = function (data) {

                    // 该web订单的notes信息
                    fnReloadNotesInfo (data.orderNotesList);

                    // 更新显示mainOrderInfo
                    $scope.mainOrderInfo.expected = data.mainOrderInfo.expected;
                    $scope.mainOrderInfo.customerRefund = data.mainOrderInfo.customerRefund;
                    $scope.mainOrderInfo.balanceDue = data.mainOrderInfo.balanceDue;
                    $scope.mainOrderInfo.payTitleText = data.mainOrderInfo.payTitleText;
                    $scope.mainOrderInfo.isHaveRefundHistory = data.mainOrderInfo.isHaveRefundHistory;
                };

                /**
                 * 更新被操作的orderInfo,并刷新（退款操作 独立域名）
                 * @param data
                 * @param index
                 */
                $scope.setPopupOrderActionForRefundCN = function (data) {

                    // 该web订单的notes信息
                    fnReloadNotesInfo (data.orderNotesList);

                    // 该web订单的payment信息
                    $scope.orderPaymentsList = data.orderPaymentsList;

                    // 更新显示mainOrderInfo
                    $scope.mainOrderInfo.expected = data.mainOrderInfo.expected;
                    $scope.mainOrderInfo.customerRefund = data.mainOrderInfo.customerRefund;
                    $scope.mainOrderInfo.balanceDue = data.mainOrderInfo.balanceDue;
                    $scope.mainOrderInfo.payTitleText = data.mainOrderInfo.payTitleText;
                    $scope.mainOrderInfo.isHaveRefundHistory = data.mainOrderInfo.isHaveRefundHistory;
                    $scope.mainOrderInfo.paymentTotal = data.mainOrderInfo.paymentTotal;
                };

                /**
                 * 调用popup画面: Sold To.
                 */
                $scope.doPopupEditSoldToAddress = function () {
                    // 将orderDetail页面的客户信息copy到子页面.
                    $scope.popSoldToOrderToUseInfo = {};
                    angular.copy ($scope.selectedOrder.orderInfo, $scope.popSoldToOrderToUseInfo);

                    ngDialog.open ({
                        template: omsPopupPages.popEditSoldToAddress.page,
                        controller: omsPopupPages.popEditSoldToAddress.controller,
                        scope: $scope,
                        popWindowSize: "750px"
                    });
                };

                /**
                 * 调用popup画面: Ship To.
                 */
                $scope.doPopupEditShipToAddress = function () {
                    // 将被选中的订单传递给地址编辑画面
                    $scope.popShipToOrderToUseInfo = {};
                    angular.copy ($scope.selectedOrder.orderInfo, $scope.popShipToOrderToUseInfo);
                    ngDialog.open ({
                        template: omsPopupPages.popEditShipToAddress.page,
                        controller: omsPopupPages.popEditShipToAddress.controller,
                        scope: $scope,
                        popWindowSize: "500px"
                    });
                };

                /**
                 * 将新的值赋值给order detail.
                 * @param newOrderInfo
                 * @param data
                 */
                $scope.setPopupEditAddress = function (newOrderInfo, data) {
                    // 将 数据赋值给被选中的订单
                    $scope.selectedOrder.orderInfo = newOrderInfo;
                    // 将数据重新赋值给orderlist中对应的order.
                    for(var i = 0; i < $scope.ordersList.length; i++){
                        if (_.isEqual ($scope.ordersList[i].orderNumber, newOrderInfo.orderNumber)) {
                            $scope.ordersList[i] = newOrderInfo;
                        }
                    }
                    //_.each ($scope.ordersList, function (orderInfo) {
                    //    if (_.isEqual (orderInfo.orderNumber, newOrderInfo.orderNumber)) {
                    //        orderInfo = newOrderInfo;
                    //    }
                    //});

                    // 该web订单的notes信息
                    fnReloadNotesInfo (data.orderNotesList);
                };

                /**
                 * 关闭popup画面并 清空数据.
                 */
                $scope.doPopupClose = function () {
                    $scope.popupMessage = {};
                    $scope.popBindOrderToUseInfo = {};
                    $scope.popAddManualOrderToUseInfo = {};
                    $scope.popCancelOrderToUseInfo = {};
                    $scope.popReturnOrderToUseInfo = {};
                    $scope.popDiscountOrderToUseInfo = {};
                    $scope.popShippingOrderToUseInfo = {};
                    $scope.popSoldToOrderToUseInfo = {};
                    $scope.popShipToOrderToUseInfo = {};
                };

                /**
                 * 当顾客信息发生变化时，才更新客户信息的comments.
                 */
                $scope.doUpdateCustomerComment = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.customerComment, $scope.selectedOrder.orderInfo.origCustomerComment)) {

                        // 设定更新数据
                        var data = {};
                        data.customerComment = $scope.selectedOrder.orderInfo.customerComment;
                        data.origCustomerComment = $scope.selectedOrder.orderInfo.origCustomerComment;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateOrderCustomerComment (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origCustomerComment = $scope.selectedOrder.orderInfo.customerComment;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };

                /**
                 * 当备注信息发生变化时，才更新internalMessage.
                 */
                $scope.doUpdateInternalMessage = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.internalMessage, $scope.selectedOrder.orderInfo.origInternalMessage)) {

                        // 设定更新数据
                        var data = {};
                        data.internalMessage = $scope.selectedOrder.orderInfo.internalMessage;
                        data.origInternalMessage = $scope.selectedOrder.orderInfo.origInternalMessage;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateInternalMessage (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origInternalMessage = $scope.selectedOrder.orderInfo.internalMessage;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };

                /**
                 * 当内部交流信息发生变化时，才更新GiftMessage.
                 */
                $scope.doUpdateGiftMessage = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.giftMessage, $scope.selectedOrder.orderInfo.origGiftMessage)) {

                        // 设定更新数据
                        var data = {};
                        data.giftMessage = $scope.selectedOrder.orderInfo.giftMessage;
                        data.origGiftMessage = $scope.selectedOrder.orderInfo.origGiftMessage;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateGiftMessage (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origGiftMessage = $scope.selectedOrder.orderInfo.giftMessage;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };

                /**
                 * 选择一条note信息用于显示其详细信息
                 * @param noteInfo
                 */
                $scope.selectNoteInfo = function (noteInfo) {
                    $scope.selectedNoteInfo = noteInfo;
                };

                /**
                 * 调用popup画面: Add new note.
                 */
                $scope.doPopupAddNote = function () {

                    $scope.popAddNoteToUseInfo.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;
                    $scope.popAddNoteToUseInfo.sourceOrderId = $scope.mainOrderInfo.sourceOrderId;

                    ngDialog.open ({
                        template: omsPopupPages.popAddNoteOrder.page,
                        controller: omsPopupPages.popAddNoteOrder.controller,
                        scope: $scope
                    });
                };

                $scope.setPopupAddNote = function (orderNotesList) {
                    fnReloadNotesInfo (orderNotesList)
                };

                /**
                 * 当Invoice发生变化时，才更新Invoice备注和Invoice状态
                 */
                $scope.doUpdateInvoiceComments = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.invoiceInfo, $scope.selectedOrder.orderInfo.origInvoiceInfo)
                        || !_.isEqual ($scope.selectedOrder.orderInfo.invoice, $scope.selectedOrder.orderInfo.origInvoice)
                        || !_.isEqual ($scope.selectedOrder.orderInfo.invoiceKind, $scope.selectedOrder.orderInfo.origInvoiceKind)) {

                        // 设定更新数据
                        var data = {};
                        data.invoice = $scope.selectedOrder.orderInfo.invoice;
                        data.origInvoice = $scope.selectedOrder.orderInfo.origInvoice;
                        
                        data.invoiceInfo = $scope.selectedOrder.orderInfo.invoiceInfo;
                        data.origInvoiceInfo = $scope.selectedOrder.orderInfo.origInvoiceInfo;
                        
                        data.invoiceKind = $scope.selectedOrder.orderInfo.invoiceKind;
                        data.origInvoiceKind = $scope.selectedOrder.orderInfo.origInvoiceKind;
                        
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateInvoice (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origInvoiceInfo = $scope.selectedOrder.orderInfo.invoiceInfo;
                            $scope.selectedOrder.orderInfo.origInvoice = $scope.selectedOrder.orderInfo.invoice;
                            $scope.selectedOrder.orderInfo.origInvoiceKind = $scope.selectedOrder.orderInfo.invoiceKind;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };
                
                /**
                 * 当shippingMethod发生变化时，才更新ShippingMethod状态
                 */
                $scope.doUpdateShippingMethod = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.shipping, $scope.selectedOrder.orderInfo.origShipping)) {

                        // 设定更新数据
                        var data = {};
                        data.shipping = $scope.selectedOrder.orderInfo.shipping;
                        data.origShipping = $scope.selectedOrder.orderInfo.origShipping;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateShipping (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origShipping = $scope.selectedOrder.orderInfo.shipping;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };
                
                /**
                 * 当freight_collect发生变化时，才更新freight_collect状态
                 */
                $scope.doUpdateOrderOtherProp = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.freightCollect, $scope.selectedOrder.orderInfo.origFreightCollect)) {

                        // 设定更新数据
                        var data = {};
                        data.freightCollect = $scope.selectedOrder.orderInfo.freightCollect;
                        data.origFreightCollect = $scope.selectedOrder.orderInfo.origFreightCollect;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateOrderOtherProp (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origFreightCollect = $scope.selectedOrder.orderInfo.freightCollect;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };

                /**
                 * 当customer_refused发生变化时，才更新customer_refused状态
                 */
                $scope.doUpdateOrderCustomerRefused = function () {

                    if (!_.isEqual ($scope.selectedOrder.orderInfo.customerRefused, $scope.selectedOrder.orderInfo.origCustomerRefused)) {

                        // 设定更新数据
                        var data = {};
                        data.customerRefused = $scope.selectedOrder.orderInfo.customerRefused;
                        data.origCustomerRefused = $scope.selectedOrder.orderInfo.origCustomerRefused;
                        data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                        data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                        // 更新数据
                        orderDetailService.doUpdateOrderCustomerRefused (data)
                            .then (function (data) {
                            $scope.selectedOrder.orderInfo.origCustomerRefused = $scope.selectedOrder.orderInfo.customerRefused;
                            // 将数据重新赋值给orderlist中对应的order.
                            _.each ($scope.ordersList, function (orderInfo) {
                                if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                    orderInfo = $scope.selectedOrder.orderInfo;
                                }
                            });

                            // 该web订单的notes信息
                            fnReloadNotesInfo (data.orderNotesList);
                        });
                    }
                };

                /**
                 * 取消皇马订单
                 */
                $scope.doCancelClientOrder = function () {
                    $scope.popupMessage.lockFlag = !$scope.mainOrderInfo.localShipOnHold;
                    $scope.popupMessage.sourceOrderId = $scope.mainOrderInfo.sourceOrderId;
                    $scope.popupMessage.type = popupMessageType.cancelClientOrder;

                    $scope.popupMessage.message = messageService.getMessageByKey (messageKeys.ID_000005);


                    ngDialog.open ({
                        template: omsPopupPages.popMessage.page,
                        controller: omsPopupPages.popMessage.controller,
                        scope: $scope
                    });
                };

                ///**
                // * 设定被选中的orderInfo
                // * @param orderInfo
                // */
                //$scope.selectOrderInfo = function (orderInfo) {
                //    $scope.selectedOrder.orderInfo = orderInfo;
                //};

                /**
                 * 更换该用户的子订单
                 * @param branch
                 */
                $scope.changeOrderNumber = function (branch) {
                    if (_.isEmpty (branch.children)) {
                    	$scope.currentOrderNumber = branch.orderNumber;
                        fnGotoOrderDetailPage (branch.sourceOrderId);
                    }
                };

                /**
                 * 更换订单从search order list
                 * @param sourceOrderId
                 */
                $scope.changeOrderByOrderList = function (sourceOrderId) {
                    if (!_.isEmpty (sourceOrderId)) {
                        fnGotoOrderDetailPage (sourceOrderId);
                    }
                };

                // TODO
                /**
                 * 显示一下组订单数据，并 跳转到下一页的第一条数据.
                 * @param searchPage
                 */
                $scope.doSearchOrderList = function (searchPage) {

                    // 判断如果该页面的检索条件存在则查询
                    if (!_.isNull (searchOrderService.getSearchCondition ()) && searchPage > 0) {

                        // 重新展示检索条件
                        var searchData = searchOrderService.getSearchCondition ();
                        searchData.page = searchPage;
                        searchData.rows = $scope.searchOrder.searchOrderCurrentPageRows;

                        searchService.doSearch (searchData)
                            .then (function (data) {

                            $scope.searOrderCurrentPage = searchPage;

                            var orderListInfo = {};
                            // 设置传递给order detail 页面的order list.
                            orderListInfo.orderInfoListForOrderDetail = [];
                            _.each (data.ordersInfoList, function (orderInfo) {
                                orderListInfo.orderInfoListForOrderDetail.push ({"orderNumber": orderInfo.orderNumber, "sourceOrderId": orderInfo.sourceOrderId});
                            });
                            orderListInfo.searOrderCurrentPage = searchPage;
                            orderListInfo.searchOrderCurrentPageCount = Math.ceil (data.total / $scope.searchOrder.searchOrderCurrentPageRows);
                            orderListInfo.searchOrderCurrentPageRows = $scope.searchOrder.searchOrderCurrentPageRows;

                            orderDetailInfoService.setOrderInfoList (orderListInfo);
                            $scope.searchOrder = orderListInfo;
                            $scope.$apply ();

                            // 跳转到下一页的第一条数据
                            //fnGotoOrderDetailPage (orderListInfo.orderInfoListForOrderDetail[0].sourceOrderId);
                        });
                    }
                };

                /**
                 * 根据source order id 取得订单信息.
                 * @param sourceOrderId
                 */
                function getOrderDetailInfo (sourceOrderId) {

                    orderDetailService.doGetOrderDetail (sourceOrderId)
                        .then (function (data) {

                        // 该客户的原始历史订单记录用于画面展示
                        $scope.orderHistoryTreeList = data.orderHistoryTreeList;
//                        $scope.orderHistoryTree = $scope.orderHistoryTreeList;
//                        $scope.orderHistoryForTreeLink = data.orderHistoryForTreeLink;
                        // 该客户的原始历史订单记录
                        $scope.orderHistoryList = data.orderHistoryList;
                        // 默认被选中的order
                        $scope.currentOrderNumber = data.ordersList[0].orderNumber;
                        $scope.currentSourceOrderId = data.mainOrderInfo.sourceOrderId;

                        // TODO 还需要修改画面处理orderNumber

                        // 该web订单的总信息
                        $scope.mainOrderInfo = data.mainOrderInfo;
                        // 该web订单的transaction信息
                        $scope.orderTransactionsList = data.orderTransactionsList;
                        // 该web订单的payment信息
                        $scope.orderPaymentsList = data.orderPaymentsList;

                        // 该web订单的所有订单信息
                        $scope.ordersList = data.ordersList;
                        // 处理显示只显示orderDetail的Sku,Discount,Shipment
                        _.each ($scope.ordersList, function (orderInfo) {
                            orderInfo.orderDetailsList = fnClearNotShowOrderDetail (orderInfo.orderDetailsList);
                        });
                        $scope.selectedOrder.orderInfo = data.ordersList[0];

                        // 该web订单的notes信息
                        fnReloadNotesInfo (data.orderNotesList);
                    })
                        .then (function () {

                        if (!_.isNull (orderDetailInfoService.getOrderInfoList ())) {
                            // 获得serch画面过来的searchOrderList
                            $scope.searchOrder = orderDetailInfoService.getOrderInfoList ();
                        }
                    });
                }

                /**
                 * 删除不需要显示的order detail数据.
                 * @param orderDetailList
                 * @returns {Array}
                 */
                function fnClearNotShowOrderDetail (orderDetailList) {
                    var newOrderDetailsList = [];
                    _.each (orderDetailList, function (orderDetailInfo) {
                        // 删除非显示数据
                        if (orderDetailInfo.showFlag) {
                            newOrderDetailsList.push (orderDetailInfo)
                        }
                    });
                    return newOrderDetailsList;
                }

                /**
                 * 刷新Notes页面
                 * @param orderNotesList
                 */
                function fnReloadNotesInfo (orderNotesList) {

                    _.each (orderNotesList, function (orderNoteInfo) {
                        if (orderNoteInfo.notes.length > 30) {
                            orderNoteInfo.shortNotes = orderNoteInfo.notes.substr (0, 30) + '...';
                        } else {
                            orderNoteInfo.shortNotes = orderNoteInfo.notes;
                        }
                    });

                    // get the notes list.
                    $scope.orderNotesList = orderNotesList;
                    $scope.selectedNoteInfo = orderNotesList[0];
                }

                /**
                 * 跳转到 new order 页面
                 */
                function fnGotoNewOrderPage () {

                    var orderDetailHash = omsRoute.oms_orders_orderdetail.hash.substring (omsRoute.oms_orders_orderdetail.hash.indexOf (':'), omsRoute.oms_orders_orderdetail.hash.length);

                    // 跳转到详细页面
                    newOrderService.setOrderBeforePageUrl (omsRoute.oms_orders_orderdetail.hash.replace (orderDetailHash, $scope.selectedOrder.orderInfo.sourceOrderId));

                    //var newOrderHash = omsRoute.oms_orders_addneworder_with_sourceOrderId.hash.substring (omsRoute.oms_orders_addneworder_with_sourceOrderId.hash.indexOf (':')
                    //    , omsRoute.oms_orders_addneworder_with_sourceOrderId.hash.length);

                    // 跳转到NewOrder页面
                    //$location.path (omsRoute.oms_orders_addneworder_with_sourceOrderId.hash.replace (newOrderHash, $scope.selectedOrder.orderInfo.sourceOrderId))
                    $location.path (omsRoute.oms_orders_addneworder.hash);
                }

                /**
                 * 跳转到 新的order detail页面
                 * @param sourceOrderId
                 */
                function fnGotoOrderDetailPage (sourceOrderId) {

                    var orderDetailHash = omsRoute.oms_orders_orderdetail.hash.substring (omsRoute.oms_orders_orderdetail.hash.indexOf (':'), omsRoute.oms_orders_orderdetail.hash.length);

                    // 跳转到详细页面
                    $location.path (omsRoute.oms_orders_orderdetail.hash.replace (orderDetailHash, sourceOrderId));
                }

                /**
                 * 将新绑定的sourceOrderId设置到OrderList.
                 * @param orderNumber
                 * @param sourceOrderId
                 */
                function fnResetSearchOrderList (orderNumber, sourceOrderId) {

                    _.each ($scope.searchOrderList, function (orderInfo) {
                        if (_.isEqual (orderInfo.orderNumber, orderNumber)) {
                            orderInfo.sourceOrderId = sourceOrderId;
                        }
                    });

                    orderDetailInfoService.setOrderInfoList ($scope.searchOrderList);
                }

                /**
                 * 弹出approve确认页面，点击ok的操作.
                 */
                function setPopupApproveOrder () {

                    var data = {
                        sourceOrderId: $scope.popupMessage.sourceOrderId,
                        orderNumber: $scope.popupMessage.orderNumber
                    };

                    orderDetailService.doApprove (data)
                        .then (function (data) {
                        //$scope.ordersList[$scope.popupMessage.index] = data.orderInfo;
                        //$scope.ordersList[$scope.popupMessage.index].orderDetailsList = fnClearNotShowOrderDetail (data.orderInfo.orderDetailsList);

                        var selectedIndex = -1;

                        _.each ($scope.ordersList, function (orderInfo, index) {
                            if (_.isEqual (orderInfo.orderNumber, data.orderInfo.orderNumber)) {
                                //orderInfo = data.orderInfo;
                                //orderInfo.orderDetailsList = fnClearNotShowOrderDetail (data.orderInfo.orderDetailsList);
                                selectedIndex = index;
                            }
                        });

                        if (selectedIndex != -1) {
                            $scope.ordersList[selectedIndex] = data.orderInfo;
                            $scope.ordersList[selectedIndex].orderDetailsList = fnClearNotShowOrderDetail (data.orderInfo.orderDetailsList);
                        }

                        // 该web订单的notes信息
                        fnReloadNotesInfo (data.orderNotesList);
                    });
                }

                /**
                 * 加锁或者解锁订单操作
                 */
                function setPopupLockOrder () {

                    var lockFlag = $scope.popupMessage.lockFlag;

                    var orderLockedData = {
                        lockFlag: lockFlag,
                        sourceOrderId: $scope.popupMessage.sourceOrderId
                    };

                    // lock order
                    orderDetailService.doLockOrUnlockOrder (orderLockedData)
                        .then (function (data) {

                        //	锁定状态同步
                        $scope.mainOrderInfo.localShipOnHold = lockFlag;

                        // get the notes list.
                        fnReloadNotesInfo (data.orderNotesList);
                    });
                }

                /**
                 * 第三方订单取消
                 */
                function setCancelClientOrder() {
                    // 设定更新数据
                    var data = {};
                    data.cancelRMOrderFlag = $scope.selectedOrder.orderInfo.cancelRMOrderFlag;
                    data.origCancelClientOrderFlag = $scope.selectedOrder.orderInfo.origCancelClientOrderFlag;
                    data.sourceOrderId = $scope.selectedOrder.orderInfo.sourceOrderId;
                    data.orderNumber = $scope.selectedOrder.orderInfo.orderNumber;

                    // 更新数据
                    orderDetailService.doCancelClientOrder (data)
                        .then (function (data) {
                        $scope.selectedOrder.orderInfo.origCancelClientOrderFlag = $scope.selectedOrder.orderInfo.cancelClientOrderFlag;
                        // 将数据重新赋值给orderlist中对应的order.
                        _.each ($scope.ordersList, function (orderInfo) {
                            if (_.isEqual (orderInfo.orderNumber, $scope.selectedOrder.orderInfo.orderNumber)) {
                                orderInfo = $scope.selectedOrder.orderInfo;
                            }
                        });

                        // 该web订单的notes信息
                        fnReloadNotesInfo (data.orderNotesList);
                    });
                }
                
            }])
});
