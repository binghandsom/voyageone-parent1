/**
 * @author tony-piao
 * feed产品概述
 */
define([
    'cms'
],function(cms) {
    cms.directive("feedSchema", function ($routeParams, $rootScope, $translate, productDetailService, notify) {
        return {
            restrict: "E",
            templateUrl : "views/product/feed.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo"
            },
            link: function (scope) {

                scope.vm = {
                    productDetails : null
                };

                // 获取初始化数据
                initialize();
                function initialize() {
                    if(scope.productInfo.productDetails){
                            scope.vm.productDetails = scope.productInfo.productDetails.productInfo;
                            scope.vm.productStatusList = scope.productInfo.productDetails.productStatusList;
                            scope.vm.inventoryList = scope.productInfo.productDetails.inventoryList;
                            scope.vm._orgChaName = scope.productInfo.productDetails.orgChaName;
                            scope.vm._isminimall = scope.productInfo.productDetails.isminimall;
                            scope.vm._isMain = scope.productInfo.productDetails.isMain;
                            if ($rootScope.imageUrl == undefined) {
                                $rootScope.imageUrl = '';
                            }
                            scope.vm.currentImage = $rootScope.imageUrl.replace('%s', scope.vm.productDetails.productImages.image1[0].image1);
                            scope.vm.currentImage = scope.productInfo.productDetail.defaultImage;
                            scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                            scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady

                       }else{
/*                            scope.vm.errorMsg = res.message;
                            scope.vm.showInfoFlag = false;*/
                       }
                }

                scope.updateFeed = updateFeed;
                function updateFeed(){
                    productDetailService.updateProductFeed(scope.vm.productDetails).then(function(res){
                        scope.vm.productDetails.modified = res.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    });
                }
            }
        };
    });
});
