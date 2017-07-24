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

        constructor($scope,detailDataService,$usProductDetailService,notify,$rootScope){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.$usProductDetailService = $usProductDetailService;
            this.notify = notify;
            this.$rootScope = $rootScope;
            console.log($rootScope);

            this.imageView = {
                imageNum:0,
                images:[],
                boxImageNum:0,
                boxImages:[],
                imageUrl: $rootScope.imageUrl,
                currImage:{},
                currBoxImage:{}
            };
        }

        init(){
            let self = this;

            self.detailDataService.getProductInfo({prodId:self.$scope.productInfo.productId}).then(res => {

                // console.log(res);

                self.mastData = res.data.mastData;
                self.platform = res.data.platform;
                self.productComm = res.data.productComm;

                // 图片
                let images1 = self.productComm.fields.images1;
                if (images1 && _.size(images1) > 0) {
                    self.imageView.images = images1;
                    self.imageView.imageNum = _.size(images1);
                }
                let images2 = self.productComm.fields.images2;
                if (images2 && _.size(images2) > 0) {
                    self.imageView.boxImages = images2;
                    self.imageView.boxImageNum = _.size(images2);
                }

                console.log(self.imageView);

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

        /**全schema中通过name递归查找field*/
        searchField(fieldId, schema) {
            let self = this;

            let result = null;

            _.find(schema, function (field) {

                if (field.id === fieldId) {
                    result = field;
                    return true;
                }

                if (field.fields && field.fields.length) {
                    result = self.searchField(fieldId, field.fields);
                    if (result)
                        return true;
                }

                return false;
            });

            return result;
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
