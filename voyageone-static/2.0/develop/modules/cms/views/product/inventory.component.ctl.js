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
    cms.directive("inventorySchema", function (selectRowsFactory) {
        return {
            restrict: "E",
            templateUrl: "views/product/inventory.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope, element) {

                scope.initial = initial;
                scope.count = count;
                scope.generalLength = '3';
                scope.CNPrivateLength = '1';
                scope.CNThirdPartyLength = '1';
                scope.USPrivateLength = '1';
                scope.USThirdPartyLength = '1';

                /**
                 * Sku属性初始化
                 */
                function initial() {
                    scope.showDetail = false;
                    scope.generalLength = '3';
                    scope.tempStockListSelect = new selectRowsFactory();
                }

                function count(value) {
                    if (value == true) {
                        scope.generalLength = '4';
                        scope.CNPrivateLength = '6';
                        scope.CNThirdPartyLength = '4';
                        scope.USPrivateLength = '2';
                        scope.USThirdPartyLength = '8';
                    }else {
                        scope.generalLength = '3';
                        scope.CNPrivateLength = '1';
                        scope.CNThirdPartyLength = '1';
                        scope.USPrivateLength = '1';
                        scope.USThirdPartyLength = '1';
                    }
                }
            }
        };
    });
});