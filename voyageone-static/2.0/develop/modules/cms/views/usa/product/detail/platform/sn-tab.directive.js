/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/service/product.detail.service',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive',
    'modules/cms/directives/contextMenu.directive',
    '../detail.data.service'
], function (cms, carts) {

    const mConfig = {
        bigImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?wid=2200&hei=2200',
        newImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?fmt=jpg&scl=1&qlt=100'
    };


    class SnTabController{

        constructor($scope,detailDataService){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
        }

        init(){
            let self = this;

            self.detailDataService.getProductInfo({prodId:self.$scope.productInfo.productId}).then(res => {

                console.log(res)
            });
        }

    }

    cms.directive('snTab', function () {
        return {
            restrict: 'E',
            controller: SnTabController,
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/usa/product/detail/platform/sn-tab.tpl.html'
        }
    })
});
