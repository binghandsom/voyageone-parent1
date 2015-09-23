/**
 * @Name:    addNewOrderController.js
 * @Date:    2015/3/17
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');

    require ('modules/oms/common/common.service');

    require ('modules/oms/services/orderDetailInfo.service');
    require ('modules/oms/orders/orders.service');
    require ('modules/oms/services/addNewOrder.service');

    require ('modules/oms/popup/popSearchSku.ctl');
    require ('modules/oms/popup/popSearchCustomer.ctl');
    omsApp.controller ('addNewOrderController',
        ['$scope'
            , '$location'
            , '$routeParams'
            , 'ngDialog'
            , 'omsCommonService'
            , 'omsPopupPages'
            , 'omsType'
            , 'omsRoute'
            , 'addNewOrderService'
            , 'newOrderService',
            //, 'orderDetailInfoService'
            //, 'messageService'
            //, 'messageKeys',
            function ($scope
                , $location
                , $routeParams
                , ngDialog
                , omsCommonService
                , omsPopupPages
                , omsType
                , omsRoute
                , addNewOrderService
                , newOrderService) {
                //, orderDetailInfoService
                //, messageService
                //, messageKeys) {

                var _ = require ('underscore');
                //var messageUtil = require('components/util/messageUtil');

                // 定义新订单信息
                $scope.newOrderInfo = {};
                $scope.customerInfo = {};
                $scope.shipToInfo = {};
                //初始化orderDetailList
                $scope.orderDetailList = [];
                //判断是否有数据被选中
                $scope.selectOrderDetail = [];
                //给popup画面使用的信息
                $scope.popUpUseInfo = {};
                $scope.popDiscountOrderToUseInfo = {};

                /**
                 * 初始化画面，如果来之orderDetail画面，则显示sourceOrderId，
                 * 如果来自orderDetail或者customerDetail页面，则显示customer sold和ship信息
                 */
                $scope.initialize = function () {

                    addNewOrderService.doGetCodeList ()
                        .then (function (data) {
                        //显示各种master数据
                        $scope.countryList = omsCommonService.doGetOneCodeFromList (data, omsType.country);
                        $scope.invoiceStatusList = omsCommonService.doGetOneCodeFromList (data, omsType.invoice);
                        $scope.orderKindList = omsCommonService.doGetOneCodeFromList (data, omsType.orderKind);
                        // 初始化信息
                        $scope.newOrderInfo.sourceOrderId = "";
                        $scope.newOrderInfo.cartId = "";
                        $scope.isFromOrderDetail = false;

                        // 从seassion中取数据并赋值
                        if (!_.isEmpty (newOrderService.getCurrentOrderInfo ())) {
                            $scope.customerInfo = _.isNull(newOrderService.getCurrentOrderInfo().customerInfo) ? {} : newOrderService.getCurrentOrderInfo().customerInfo;
                            $scope.shipToInfo = newOrderService.getCurrentOrderInfo().shipToInfo;
                            $scope.newOrderInfo.orderChannelId = newOrderService.getCurrentOrderInfo().orderChannelId;
                            if (!_.isEmpty(newOrderService.getCurrentOrderInfo().sourceOrderId)) {
                                $scope.newOrderInfo.sourceOrderId = newOrderService.getCurrentOrderInfo().sourceOrderId;
                                $scope.newOrderInfo.cartId = newOrderService.getCurrentOrderInfo().cartId;
                                $scope.isFromOrderDetail = true;
                            }
                        } else {
                            // 线下订单
                            $scope.newOrderInfo.cartId = '22';
                        }

                        // 判断如果该画面来至orderdetail，则可以选择任何订单，如果来至menu和customer只能 选择orgia订单
                        _.each ($scope.orderKindList, function (orderKind, index) {
                            if ($scope.isFromOrderDetail
                                || (!$scope.isFromOrderDetail && index == 0)) {
                                orderKind.isLeaf = false;
                            } else {
                                orderKind.isLeaf = true;
                            }
                        });
                    })
                        .then (function () {
                        addNewOrderService.doGetStoreList ()
                            .then (function (data) {
                            $scope.propertyList = data.propertyList;
                        });
                    });
                };

                /**
                 * 创建新订单.
                 */
                $scope.doSave = function () {
                    if ($scope.formNewOrder.$valid
                        && !_.isEmpty ($scope.orderDetailList)) {

                        //将画面的数据传给服务器端.
                        var data = {};
                        data.orderInfo = $scope.newOrderInfo;
                        //Discount金额需要*-1
                        data.orderInfo.discount = parseFloat ($scope.newOrderInfo.discount) * -1;
                        data.orderInfo.revisedDiscount = parseFloat ($scope.newOrderInfo.revisedDiscount) * -1;
                        data.orderInfo.discountType = "2";
                        data.orderInfo.discountPercent = "0";
                        data.customerInfo = $scope.customerInfo;
                        data.shipToInfo = $scope.shipToInfo;

                        // 赋值OrderDetail信息
                        data.orderDetailsList = [];
                        _.each ($scope.orderDetailList, function (orderDetail) {
                            data.orderDetailsList.push ({
                                sku: orderDetail.sku,
                                quantityOrdered: orderDetail.quantityOrdered,
                                product: orderDetail.product,
                                pricePerUnit: orderDetail.pricePerUnit,
                                discount: parseFloat(orderDetail.discount) * -1
                            })
                        });

                        // 原始订单的场合
                        if ($scope.newOrderInfo.orderKind == 0) {
                            addNewOrderService.doSaveOrderOriginal (data, $scope)
                                .then (function (data) {
                                fnGotoOrderDetailPage (data.sourceOrderId);
                            });
                        // 子订单的场合
                        } else {
                            addNewOrderService.doSaveOrder (data, $scope)
                                .then (function (data) {
                                fnGotoOrderDetailPage (data.sourceOrderId);
                            });
                        }
                    }
                };

                /**
                 * 取消创建新订单，并返回到前一个画面或者index画面.
                 */
                $scope.doCancel = function () {
                    // 返回到前一个画面
                    if (!_.isEmpty (newOrderService.getOrderBeforePageUrl ())) {
                        $location.path (newOrderService.getOrderBeforePageUrl ());
                    }
                    // 如果从menu画面过来，返回到oms的index页面
                    else {
                        $location.path (omsRoute.oms_default_index.hash);
                    }
                };

                /**
                 * popup客户信息检索画面.
                 */
                $scope.doPopupSearchCustomer = function () {
                    $scope.popUpUseInfo.orderChannelId = $scope.newOrderInfo.orderChannelId;
                    ngDialog.open ({
                        template: omsPopupPages.popSearchCustomer.page,
                        controller: omsPopupPages.popSearchCustomer.controller,
                        scope: $scope,
                        popWindowSize: "850px"
                    });
                };

                /**
                 * 设定Customer信息来之popup画面.
                 * @param customerInfo
                 */
                $scope.setPopupCustomerInfo = function (customerInfo) {
                    $scope.customerInfo = customerInfo;
                };

                /**
                 * Copy客户信息到Ship信息
                 */
                $scope.doCopyFromSoldTo = function () {
                    $scope.shipToInfo = {};
                    //$scope.shipToInfo = $scope.customerInfo;
                    angular.copy ($scope.customerInfo, $scope.shipToInfo);
                    //$scope.shipToInfo.customerId = "";
                };

                /**
                 * popupSKU信息检索画面.
                 */
                $scope.doPopupSearchSku = function () {
                    $scope.popUpUseInfo.orderChannelId = $scope.newOrderInfo.orderChannelId;
                    $scope.popUpUseInfo.cartId = $scope.newOrderInfo.cartId;
                    ngDialog.open ({
                        template: omsPopupPages.popSearchSku.page,
                        controller: omsPopupPages.popSearchSku.controller,
                        scope: $scope,
                        popWindowSize: "850px"
                    });
                };

                /**
                 * 清楚popup画面输入的数据
                 */
                $scope.doPopupClose = function () {
                    $scope.popUpUseInfo = {};
                };

                /**
                 * 更新order detail中的数据.
                 * @param skuInfo
                 */
                $scope.doSetPopupSearchSku = function (skuInfo) {
                    $scope.orderDetailList.push (skuInfo);

                    $scope.calculatePrice ();
                };

                /**
                 * 删除选中的SKU list.
                 */
                $scope.doDeleteOrderDetail = function () {
                    // TODO 该处理可以选中多条
                    if (!_.isEmpty ($scope.selectOrderDetail)) {
                        var newOrderDetailList = []
                        _.each ($scope.orderDetailList, function (orderInfo, index) {
                            // 删除被选中的一行

                            if (!_.contains ($scope.selectOrderDetail, index.toString ())) {
                                newOrderDetailList.push (orderInfo);
                            }
                            //$scope.orderDetailList.splice (selectedIndex, 1);
                        });
                        $scope.orderDetailList = newOrderDetailList;

                        // 被选中的状态清空
                        $scope.selectOrderDetail = [];

                        $scope.calculatePrice ();
                    }
                };

                // TODO 这个方式不是很好,以后再改一下.
                /**
                 * 当discount发生变化发生变化的时候，重新计算该订单的价格.
                 */
                $scope.calculatePrice = function () {

                    var productTotal = 0.00;
                    var productItemDiscount = 0.00;
                    var surcharge = isNaN (parseFloat ($scope.newOrderInfo.surcharge)) ? 0.00 : parseFloat ($scope.newOrderInfo.surcharge);
                    var discount = isNaN (parseFloat ($scope.newOrderInfo.discount)) ? 0.00 : parseFloat ($scope.newOrderInfo.discount);
                    var couponDiscount = isNaN (parseFloat ($scope.newOrderInfo.couponDiscount)) ? 0.00 : parseFloat ($scope.newOrderInfo.couponDiscount);
                    var shippingTotal = isNaN (parseFloat ($scope.newOrderInfo.shippingTotal)) ? 0.00 : parseFloat ($scope.newOrderInfo.shippingTotal);

                    //$scope.newOrderInfo.productTotal = 0.00;
                    _.each ($scope.orderDetailList, function (orderDetailInfo) {
                        productTotal += new Number (orderDetailInfo.pricePerUnit) * new Number (orderDetailInfo.quantityOrdered);
                        productItemDiscount += new Number (orderDetailInfo.discount) * new Number (orderDetailInfo.quantityOrdered);
                    });

                    $scope.newOrderInfo.grandTotal = parseFloat (productTotal + surcharge - productItemDiscount - discount + couponDiscount + shippingTotal).toFixed (2);
                    $scope.newOrderInfo.finalGrandTotal = $scope.newOrderInfo.grandTotal;

                    // 初始化所有format价格
                    $scope.newOrderInfo.productTotal = productTotal.toFixed (2);
                    $scope.newOrderInfo.finalProductTotal = productTotal.toFixed (2);
                    $scope.newOrderInfo.surcharge = surcharge.toFixed (2);
                    $scope.newOrderInfo.revisedSurcharge = surcharge.toFixed (2);
                    $scope.productItemDiscount = productItemDiscount.toFixed (2);
                    $scope.revisedProductItemDiscount = productItemDiscount.toFixed (2);
                    $scope.newOrderInfo.discount = discount.toFixed (2);
                    $scope.newOrderInfo.revisedDiscount = discount.toFixed (2);
                    $scope.newOrderInfo.couponDiscount = couponDiscount.toFixed (2);
                    $scope.newOrderInfo.revisedCouponDiscount = couponDiscount.toFixed (2);
                    $scope.newOrderInfo.shippingTotal = shippingTotal.toFixed (2);
                    $scope.newOrderInfo.finalShippingTotal = shippingTotal.toFixed (2);
                };

                /**
                 * 跳转到 新的order detail页面
                 * @param sourceOrderId
                 */
                function fnGotoOrderDetailPage (sourceOrderId) {

                    var orderDetailHash = omsRoute.oms_orders_orderdetail.hash.substring (omsRoute.oms_orders_orderdetail.hash.indexOf (':'), omsRoute.oms_orders_orderdetail.hash.length);

                    // 跳转到详细页面
                    $location.path (omsRoute.oms_orders_orderdetail.hash.replace (orderDetailHash, sourceOrderId));
                }

            }])
});
