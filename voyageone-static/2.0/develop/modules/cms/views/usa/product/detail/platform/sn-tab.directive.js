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

    class SnTabController{

        constructor($scope,detailDataService,$usProductDetailService,notify){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;
        }

        init(){
            let self = this;

            self.detailDataService.getProductInfo({prodId:self.$scope.productInfo.productId}).then(res => {

                console.log(res);

                self.mastData = res.data.mastData;
                self.platform = res.data.platform;
                self.productComm = res.data.productComm;

            });
        }

        // Save
        save() {
            let self = this;
            let parameter = {
                prodId:self.$scope.productInfo.productId,
                data:{
                    mastData:self.mastData,
                    platform:self.platform,
                    productComm:self.productComm
                }
            };
            this.$usProductDetailService.updateCommonProductInfo(parameter).then(res => {
                if (res.data) {
                    self.notify.success("Save success.");
                }
            });
        }

        /**
         * Pop category
         * @param option
         * @param attr
         */
        popUsCategory(option, attr) {
            let self = this;

            self.popups.openUsCategory(option).then(context => {
                if(option.muiti){
                    let categories = _.pluck(context, "catPath");
                    _.extend(self.feed.attribute, {categories:categories});
                    _.extend(self.feed, {categoriesTree:context});
                }else{
                    _.extend(self.feed, {category: context.catPath, categoryCatId:context.catId});
                    if (!!context.mapping) {
                        let seoInfo = {};
                        if (!!context.mapping.seoTitle) {
                            _.extend(seoInfo, {seoTitle: context.mapping.seoTitle});
                        }
                        if (!!context.mapping.seoKeywords) {
                            _.extend(seoInfo, {seoKeywords: context.mapping.seoKeywords});
                        }
                        if (!!context.mapping.seoDescription) {
                            _.extend(seoInfo, {seoDescription: context.mapping.seoDescription});
                        }
                        // amazon、googleCategory、googleDepartment、priceGrabber
                        let category = {
                            amazonBrowseTree:context.mapping.amazon,
                            googleCategory:context.mapping.googleCategory,
                            googleDepartment:context.mapping.googleDepartment,
                            priceGrabberCategory:context.mapping.priceGrabber};
                        _.extend(self.feed, category);
                        _.extend(self.feed, seoInfo);
                    }
                }

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
            templateUrl: 'views/usa/product/detail/platform/sn-tab.directive.html'
        }
    })

});
