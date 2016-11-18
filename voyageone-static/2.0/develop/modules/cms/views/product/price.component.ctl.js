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
                productInfo: "=productInfo",
                //sales:{}
            },
            link: function (scope) {
                scope.vm = {
                    selectSales:"codeSumAll",
                    productPriceList:[]
                };
                initialize();
                function initialize() {
                    $productDetailService.getProductPriceSales(scope.productInfo.productId).then(function (resp) {
                      console.log(resp.data);
                        scope.vm.productPriceList=resp.data.productPriceList;
                        scope.sales=resp.data.productPriceList;
                    });
                }
            }
        };
    });
});