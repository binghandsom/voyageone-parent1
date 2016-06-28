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
            replace: false,
            transclude: true,
            templateUrl : "views/product/feed.component.tpl.html",
            /**独立的scope对象*/
            scope: true,
            link: function (scope) {

                scope.vm = {
                    productDetails : null
                };

                // 获取初始化数据
                initialize();
                function initialize() {
                    var disposeDataWatcher = scope.$watch("ctrl.product.productDetails", function (data) {

                        // 没有就继续等待
                        if (!data){
                            return;
                        }

                        // 有了就保存, 然后触发下一步
                        scope.vm.productDetails = data.productInfo;
                        scope.vm.productStatusList = data.productStatusList;
                        scope.vm.inventoryList = data.inventoryList;
                        scope.vm._orgChaName = data.orgChaName;
                        scope.vm._isminimall = data.isminimall;
                        scope.vm._isMain = data.isMain;
                        if ($rootScope.imageUrl == undefined) {
                            $rootScope.imageUrl = '';
                        }
                        scope.vm.currentImage = $rootScope.imageUrl.replace('%s', scope.vm.productDetails.productImages.image1[0].image1);

                        scope.vm.currentImage = data.defaultImage;
                        scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                        scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady;

                        disposeDataWatcher();
                        disposeDataWatcher = null;
                    });

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
