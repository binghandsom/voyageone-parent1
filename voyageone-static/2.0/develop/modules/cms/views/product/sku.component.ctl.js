/**
 * Created by sofia on 2016/10/10.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.directive("skuSchema", function (selectRowsFactory) {
        return {
            restrict: "E",
            templateUrl: "views/product/sku.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope, element) {
                scope.initial = initial;

                /**
                 * Sku属性初始化
                 */
                function initial() {
                    scope.tempStockListSelect = new selectRowsFactory();
                }

            }
        };
    });
});
