/**
 * Created by sofia on 2016/10/10.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.directive("skuSchema", function (productDetailService, $translate, alert, notify, confirm) {
        return {
            restrict: "E",
            templateUrl: "views/product/sku.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope, element) {
                scope.skuList = [];
                initialize();
                scope.moveSku = moveSku;

                /**
                 * Sku属性初始化
                 */
                function initialize() {
                    productDetailService.getCommonProductSkuInfo({
                        prodId: scope.productInfo.productId
                    }).then(function (resp) {
                        scope.skuList = resp.data.skuList;
                    });
                }

                /**
                 * 移动Sku到其他Code
                 * */
                function moveSku() {
                    confirm($translate.instant('TXT_CONFIRM_MOVE_CODE')).then(function () {
                        productDetailService.moveSkuInitCheck({
                            skuList: scope.skuList,
                            sourceCode : scope.productInfo.masterField.code
                        }).then(function (resp) {
                            var moveSkuInfo = {
                                skuList: scope.skuList,
                                sourceCode : scope.productInfo.masterField.code
                            };
                            window.sessionStorage.setItem('moveSkuInfo', JSON.stringify(moveSkuInfo));
                            window.open("#/product/sku_move","_blank");
                        });
                    });
                }
            }
        };
    });
});
