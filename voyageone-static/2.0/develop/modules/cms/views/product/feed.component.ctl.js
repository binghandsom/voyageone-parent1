/**
 * @author tony-piao
 * feed产品概述
 */
define([
    'cms'
],function(cms) {
    cms.directive("feedSchema", function ($routeParams, $rootScope, $translate, productDetailService, feedMappingService, notify, confirm, alert) {
        return {
            restrict: "E",
            replace: false,
            transclude: true,
            templateUrl : "views/product/feed.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productId: "=productId"
            },
            link: function (scope) {

                scope.vm = {
                    productDetails : null
                };

                // 获取初始化数据
                initialize();
                function initialize() {
                    var data = {productId: scope.productId};
                    productDetailService.getProductInfo(data)
                        .then(function (res) {
                            scope.vm.productDetails = res.data.productInfo;
                            scope.vm.productStatusList = res.data.productStatusList;
                            scope.vm.inventoryList = res.data.inventoryList;
                            scope.vm._orgChaName = res.data.orgChaName;
                            scope.vm._isminimall = res.data.isminimall;
                            scope.vm._isMain = res.data.isMain;
                            if ($rootScope.imageUrl == undefined) {
                                $rootScope.imageUrl = '';
                            }
                            scope.vm.currentImage = $rootScope.imageUrl.replace('%s', scope.vm.productDetails.productImages.image1[0].image1);

                            scope.vm.currentImage = res.data.defaultImage;
                            scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                            scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady

                        }, function (res) {
                            scope.vm.errorMsg = res.message;
                            scope.vm.showInfoFlag = false;
                        })
                }

            }
        };
    });
});
