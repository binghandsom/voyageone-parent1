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

    cms.directive("inventorySchema", function () {

        var _inventConfig = {
            orgTh: {
                generalLength: '3',
                CNPrivateLength: '1',
                CNThirdPartyLength: '1',
                USPrivateLength: '1',
                USThirdPartyLength: '1'
            },
            expandTh: {
                generalLength: '4',
                CNPrivateLength: '6',
                CNThirdPartyLength: '4',
                USPrivateLength: '2',
                USThirdPartyLength: '8'
            }
        };

        return {
            restrict: "E",
            templateUrl: "views/product/inventory.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope, element) {

                initial();
                scope.count = count;
                scope.thEntity = _inventConfig.orgTh;

                function initial() {
                    scope.showDetail = false;
                    scope.generalLength = '3';
                }

                function count(value) {
                    if (value == true)
                        scope.thEntity = _inventConfig.expandTh;
                    else
                        scope.thEntity = _inventConfig.orgTh;
                }
            }
        };
    });
});