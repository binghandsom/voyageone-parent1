/**
 * Created by sofia on 2016/10/10.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.directive("skuSchema", function (productDetailService, $rootScope, systemCategoryService, alert, notify, confirm, selectRowsFactory) {
        return {
            restrict: "E",
            templateUrl: "views/product/sku.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope, element) {
                scope.skuList = [];
                scope.skuSelList = { selList: []};
                initialize();
                // scope.initial = initial;

                /**
                 * Sku属性初始化
                 */
                function initialize() {
                    productDetailService.getCommonProductSkuInfo({
                        prodId: scope.productInfo.productId
                    }).then(function (resp) {
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = resp.data.productComm;

                    });
                }

            }
        };
    });
});
