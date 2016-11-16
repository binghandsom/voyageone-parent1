/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms'
],function(cms) {
    cms.directive("priceSchema", function () {
        return {
            restrict: "E",
            templateUrl : "views/product/price.component.tpl.html",
            scope: {
                productInfo: "=productInfo",
                cartInfo:"=cartInfo"
            },
            link: function (scope) {

            }
        };
    });
});