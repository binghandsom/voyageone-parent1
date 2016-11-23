/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
],function(cms) {
    cms.directive("priceSchema", function ($productDetailService, $rootScope, alert, notify, confirm) {
        return {
            restrict: "E",
            templateUrl : "views/product/price.component.tpl.html",
            scope: {
                productInfo: "=productInfo"
            },
            link: function (scope) {
                scope.sales = {};
                scope.vm = {
                    selectSales: "codeSumAll",
                    productPriceList: []
                    ,model:{}
                };
                initialize();
                function initialize() {
                    initData();
                }
               function initData() {
                   console.log(scope.productInfo);
                   $productDetailService.getProductPriceSales(scope.productInfo.productId).then(function (resp) {
                       scope.vm.productPriceList = resp.data.productPriceList;
                       scope.vm.model = resp.data;
                       scope.sales = resp.data.sales;
                       scope.selectSalesOnChange();
                       scope.vm.productPriceList.forEach(function (f) {
                           if(f.checked ==2) {
                               f.isSale = true;
                           }
                       });
                   });
               }
                scope.selectSalesOnChange = function () {
                    var cartSales = scope.sales[scope.vm.selectSales];
                    if (cartSales) {
                        scope.vm.productPriceList.forEach(function (f) {
                            f.saleQty = cartSales["cartId" + f.cartId];
                        });
                    }
                    else {
                        scope.vm.productPriceList.forEach(function (f) {
                            f.saleQty = 0;
                        });
                    }
                }
                scope.isSaleOnChange=function (item) {
                   // console.log(item);
                    var parameter={};
                    parameter.prodId=scope.productInfo.productId;
                    parameter.cartId=item.cartId;
                    parameter.isSale=item.isSale;
                    $productDetailService.setCartSkuIsSale(parameter).then(function (resp) {
                      //  console.log(resp.data);
                    });
                }
                scope.calculateCartMsrpClick=function () {
                    $productDetailService.getCalculateCartMsrp(scope.productInfo.productId).then(function (resp) {
                        // console.log(resp.data);
                        scope.vm.productPriceList.forEach(function (f) {
                            var msrpInfo = _.find(resp.data, function (d) {
                                return d.cartId == f.cartId
                            });
                            if (msrpInfo) {
                                f.priceMsrp = msrpInfo.msrp;
                            }
                        });

                    });
                }
                scope.saveCartSkuPriceClick=function (item) {

                    var parameter = {};
                    parameter.prodId = scope.productInfo.productId;
                    parameter.cartId = item.cartId;

                    if (item.priceMsrp) {
                        parameter.priceMsrp = item.priceMsrp;//中国建议售价
                    }

                    if (item.priceSale) {
                        parameter.priceSale = item.priceSale;//中国最终售价
                    }

                    $productDetailService.saveCartSkuPrice(parameter).then(function (resp) {
                        //console.log(resp.data);
                        initData();
                    });

                }
            }
        };
    });
});