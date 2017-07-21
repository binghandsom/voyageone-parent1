/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    class PriceTabController{

        constructor($scope, detailDataService){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
        }

        init(){
            let self = this;

            self.detailDataService.getAllPlatformsPrice(self.productInfo.productId).then(res => {
                //console.log(res);
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