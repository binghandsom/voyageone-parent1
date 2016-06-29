/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("masterSchema", function (productDetailService,platformMappingService,notify,$q,$rootScope) {
        return {
            restrict: "E",
            templateUrl : "views/product/master.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo",
                cartInfo:"=cartInfo"
            },
            link: function (scope) {
                scope.vm = {
                    mastData:null,
                    productComm:null
                };

                initialize();
                scope.jdCategoryMapping = jdCategoryMapping;
                scope.saveProduct = saveProduct;
                scope.validSchema = validSchema;
                scope.pageAnchor = pageAnchor;
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    var productMonitor = scope.$watch("productInfo.productDetails",function(data){

                        // 没有就继续等待
                        if (!data){
                            return;
                        }
                       scope.vm.productDetails = data.productInfo;
                       scope.vm.inventoryList = data.inventoryList;
                       if ($rootScope.imageUrl == undefined) {
                        $rootScope.imageUrl = '';
                       }
                       scope.vm.currentImage = $rootScope.imageUrl.replace('%s', scope.vm.productDetails.productImages.image1[0].image1);
                       scope.vm.currentImage = data.defaultImage;

                        productMonitor();
                        productMonitor = null;
                    });

                    productDetailService.getCommonProductInfo({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = resp.data.productComm;

                    });
                }

                /**
                   @description 类目popup
                 * @param productInfo
                 * @param popupNewCategory popup实例
                 */
                function jdCategoryMapping(popupNewCategory) {
                    platformMappingService.getPlatformCategories({cartId: scope.cartInfo.value})
                        .then(function (res) {
                            return $q(function(resolve, reject) {
                                    if (!res.data || !res.data.length) {
                                        notify.danger("数据还未准备完毕");
                                        reject("数据还未准备完毕");
                                    } else {
                                        resolve(popupNewCategory({
                                            from:scope.vm.platform == null?"":scope.vm.platform.pCatPath,
                                            categories: res.data,
                                            plateSchema:true
                                        }));
                                    }
                            });
                        }).then(function (context) {
                            if(scope.vm.platform != null){
                                if(context.selected.catPath == scope.vm.platform.pCatPath)
                                    return;
                            }

                            productDetailService.changePlatformCategory({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId,catId:context.selected.catId}).then(function(resp){
                                scope.vm.platform = resp.data.platform;
                                scope.vm.platform.pCatPath = context.selected.catPath;
                                scope.vm.platform.pCatId = context.selected.catId;
                                scope.vm.checkFlag.category = 1;
                                scope.vm.platform.pStatus == 'WaitingPublish';
                                scope.vm.status =  "Pending";
                            });
                        });
                }

                /**
                 * 更新操作
                 */
                function saveProduct(){
                    productDetailService.updateCommonProductInfo({prodId:scope.productInfo.productId,productComm:scope.vm.productComm}).then(function(resp){
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    });
                }

                function validSchema(){
                    return scope.vm.platform == null || scope.vm.platform.schemaFields == null ? false : scope.schemaForm.$valid && scope.skuForm.$valid;
                }

                /**
                 * 右侧导航栏
                 * @param index div的index
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(index,speed){
                    var offsetTop = 0;
                    if(index != 1)
                        offsetTop = ($("#"+scope.cartInfo.name+index).offset().top);
                    $("body").animate({ scrollTop:  offsetTop-70}, speed);
                }

            }
        };
    });
});