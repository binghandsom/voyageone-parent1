/**
 * @Name:    popSearchSkus.ctl.js
 * @Date:    2015/5/5
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');

    omsApp.controller ('popSearchSkuController'
        , ['$scope', 'popSearchSkuService', 'omsAction'
            , function ($scope, popSearchSkuService, omsAction) {
                var _ = require ('underscore');

                // 初始化检索条件
                $scope.searchInfo = {};
                // 检索返回结果
                $scope.skuInfoList = [];
                //默认检索的第一条数据被选中
                $scope.selectSkuInfo = {};
                $scope.selectSkuInfo.discount = 0;
                $scope.selectSkuInfo.discountPercent = 0.00;
                $scope.selectSkuInfo.quantityOrdered = 1;
                //判断是否有sku被选中
                $scope.isSelectSkuInfo = false;

                /**
                 * 检索sku的list.
                 */
                $scope.doSearch = function () {

                    var data = $scope.searchInfo;
                    data.channelId = $scope.popUpUseInfo.orderChannelId;
                    data.cartId = $scope.popUpUseInfo.cartId;

                    popSearchSkuService.doGetSkuList (data)
                        .then (function (response) {
                        $scope.skuInfoList = response.skuInfoList;

                        // 如果检索结果不存在
                        if (_.isEmpty ($scope.skuInfoList)) {
                            $scope.selectSkuInfo = {};
                            $scope.selectSkuInfo.discount = 0;
                            $scope.selectSkuInfo.discountPercent = 0.00;
                            $scope.selectSkuInfo.quantityOrdered = 1;
                            $scope.isSelectSkuInfo = false;
                        }
                    });
                };

                /**
                 * 选中单挑sku的操作.
                 * @param skuInfo
                 */
                $scope.doSelectSku = function (skuInfo) {

                    // 如果有sku被选中，将被选中的数据赋值给
                    if (!_.isNull ($scope.selectSkuInfo)) {
                        $scope.selectSkuInfo = skuInfo;
                        $scope.selectSkuInfo.discount = 0;
                        $scope.selectSkuInfo.discountPercent = 0.00;
                        $scope.selectSkuInfo.price = $scope.selectSkuInfo.pricePerUnit;
                        $scope.selectSkuInfo.quantityOrdered = 1;
                        $scope.isSelectSkuInfo = true;
                    }
                };

                /**
                 * 当priceDiscount发生变化时，自动计算priceDiscountPercent.
                 */
                $scope.calculatePrice = function () {

                    var discount = $scope.selectSkuInfo.discount;
                    // TODO 金额转换方法做的不好,以后再修改.
//                    if (discount.length > 0) {

                    if (!discount.startWith ("-")) {
                        discount = isNaN (parseFloat (discount)) ? 0.00 : parseFloat (discount);
                    } else {
                        //if (discount.length >= 2) {
                        //    discount = isNaN (parseFloat (discount)) ? 0.00 : parseFloat (discount);
                        //}
                        if (discount.length >= 2) {
                            discount = isNaN (parseFloat (discount)) ? 0.00 : parseFloat (discount);
                        } else {
                            discount = 0;
                        }
                    }
//                    }
//                    var numPricePercent = new Number (discount / $scope.selectSkuInfo.pricePerUnit * 100);
                    $scope.selectSkuInfo.discount = discount.toFixed (2);
                    //$scope.selectSkuInfo.discountPercent = numPricePercent.toFixed (2);
                    $scope.selectSkuInfo.price = parseFloat (new Number ($scope.selectSkuInfo.pricePerUnit) - discount).toFixed (2);
                };

                /**
                 * 当priceDiscountPercent发生变化时，自动计算priceDiscount.
                 */
//                $scope.calculatePercent = function () {
//
//                    var discountPercent = $scope.selectSkuInfo.discountPercent;
//                    // TODO 金额转换方法做的不好,以后再修改.
////                    if (discountPercent.length > 0) {
//
//                    if (!discountPercent.startWith ("-")) {
//                        discountPercent = isNaN (parseFloat (discountPercent)) ? 0.00 : parseFloat (discountPercent);
//                    } else {
//                        discountPercent = 0.00
//                    }
////                    }
//                    var numPriceDiscount = new Number ($scope.selectSkuInfo.pricePerUnit * discountPercent / 100);
//                    $scope.selectSkuInfo.discountPercent = discountPercent.toFixed (2);
//                    $scope.selectSkuInfo.discount = numPriceDiscount.toFixed (2);
//                    $scope.selectSkuInfo.price = parseFloat (new Number ($scope.selectSkuInfo.pricePerUnit) - numPriceDiscount).toFixed (2);
//                };

                /**
                 * 将选择的sku数据添加到neworder页面
                 */
                $scope.doAddSkuToNewOrder = function () {
                    if ($scope.isSelectSkuInfo && $scope.selectSkuInfo.quantityOrdered > 0) {
                        $scope.$parent.doSetPopupSearchSku ($scope.selectSkuInfo);
                        $scope.doClose();
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

