/**
 * @author sofia
 * @description sku库存
 * @version 2.9.0
 * @datetime 2016/10/24.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.directive("inventorySchema", function (productDetailService) {

        return {
            restrict: "E",
            templateUrl: "views/product/inventory.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope) {

                initialize();
                scope.count = count;

                function initialize() {
                    scope.showDetail = false;
                    scope.noStock = false;
                    scope.noStockSkus = [];
                    scope.tblData = {};
                    /*设置海外库存表头宽度*/
                    scope.foreign = '1';
                    productDetailService.getSkuStockInfo(scope.productInfo.productId)
                        .then(function (resp) {
                            if (!resp ) {

                            } else {
                                scope.noStock = resp.data.nostock;
                                scope.supplier = resp.data.excute.data.header.supplier;
                                scope.store = resp.data.excute.data.header.store;
                                scope.base = resp.data.excute.data.header.base;
                                scope.stocks = resp.data.excute.data.stocks;
                                var noStockSkuData = resp.data.noStockSkus;
                                if (noStockSkuData && noStockSkuData.length > 0) {
                                    scope.noStockSkus = angular.copy(noStockSkuData);
                                }
                            }
                        });
                }
                function count(value) {
                    scope.showDetail= value;
                    if (value == true) {

                        scope.foreign = scope.supplier.length;
                    } else {
                        scope.foreign = '1';
                    }
                }
            }
        };
    });
});