/**
 * @description 美国各平台详情页
 */
define([
    'cms'
], function (cms) {

    class usTabController {

        constructor($scope,detailDataService) {
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
            this.cartInfo = $scope.cartInfo;
            this.platform = {};
        }

        init() {
            let self = this;

            self.detailDataService.getProductPlatform({
                cartId: Number(self.cartInfo.value),
                prodId: self.productInfo.productId
            }).then(res => {
                self.platform.platformFields = res.data.platformFields;
                self.platform.fields = res.data.fields;
            })
        }

    }

    cms.directive('usTab', function () {
        return {
            restrict: 'E',
            controller: usTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/us-tab.directive.html'
        }
    })

});