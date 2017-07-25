/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    class PriceTabController {

        constructor($scope, detailDataService, notify) {
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
            this.usPriceList = {};
            this.priceList = {};
            this.notify = notify;
        }

        init() {
            this.getData();
        }

        getData() {
            let self = this;
            self.detailDataService.getAllPlatformsPrice(self.productInfo.productId).then(res => {
                self.usPriceList = res.data.allUsPriceList;
                self.priceList = res.data.allPriceList;
            });
        }

        //修改价格
        save(cartId, platform) {
            let self = this;

            self.detailDataService.updateOnePrice({
                cartId: cartId + "",
                prodId: self.productInfo.productId + "",
                clientMsrpPrice: platform.priceMsrpSt + "",
                clientRetailPrice: platform.priceRetailSt + ""
            }).then(res => {
                self.success('Update Success');
                //刷新页面
                self.getData();
            });

        }

    }

    cms.directive("priceTab", function () {
        return {
            restrict: "E",
            controller: PriceTabController,
            controllerAs: 'ctrl',
            templateUrl: "views/usa/product/detail/price/price.directive.tpl.html",
            scope: {
                productInfo: "=productInfo"
            }
        };
    });
});