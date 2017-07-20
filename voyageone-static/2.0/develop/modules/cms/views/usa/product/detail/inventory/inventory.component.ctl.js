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

    class IntentoryController {
        constructor($scope, detailDataService) {
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
        }

        init() {
            let self = this;

            self.showDetail = false;
            self.noStock = false;
            self.noStockSkus = [];
            self.tblData = {};

            /*设置海外库存表头宽度*/
            self.foreign = '1';
            self.detailDataService.getSkuStockInfo(self.productInfo.productId)
                .then(function (resp) {
                    if (!resp) {

                    } else {
                        self.noStock = resp.data.nostock;
                        self.supplier = resp.data.excute.data.header.supplier;
                        self.store = resp.data.excute.data.header.store;
                        self.base = resp.data.excute.data.header.base;
                        self.stocks = resp.data.excute.data.stocks;

                        let noStockSkuData = resp.data.noStockSkus;
                        if (noStockSkuData && noStockSkuData.length > 0) {
                            scope.noStockSkus = angular.copy(noStockSkuData);
                        }
                    }
                });
        }

        count(value) {
            let self = this;

            self.showDetail = value;

            if (value == true) {
                self.foreign = self.supplier.length;
            } else {
                self.foreign = '1';
            }

        }

    }


    cms.directive("inventoryTab", function () {

        return {
            restrict: "E",
            controller: IntentoryController,
            controllerAs: 'ctrl',
            templateUrl: "views/usa/product/detail/inventory/inventory.component.tpl.html",
            scope: {productInfo: "=productInfo"}
        };
    });

});