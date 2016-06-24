/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("masterSchema", function (productDetailService,feedMappingService,platformMappingService,$translate,notify,confirm,alert,$q) {
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
                    productDetails:null,
                    productCode:"",
                    mastData:null,
                    platform:null,
                    productComm:null,
                    status:"Pending",
                    skuTemp:{},
                    checkFlag:{translate:0, tax:0, category:0, attribute:0},
                    resultFlag:0,
                    sellerCats:[],
                    productUrl:""
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
                    productDetailService.getCommonProductInfo({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = resp.data.productComm;
/*                        if(scope.vm.platform != null){
                            scope.vm.status = scope.vm.platform.status == null ? scope.vm.status : scope.vm.platform.status;
                            scope.vm.checkFlag.category = scope.vm.platform.pCatPath == null ? 0 : 1;
                            scope.vm.platform.pStatus = scope.vm.platform.pStatus == null ? "WaitingPublish" : scope.vm.platform.pStatus;
                            scope.vm.sellerCats = scope.vm.platform.sellerCats == null?[]:scope.vm.platform.sellerCats;
                            scope.vm.platform.pStatus = scope.vm.platform.pPublishError != null ? "Failed":scope.vm.platform.pStatus;
                        }

                        _.each(scope.vm.mastData.skus,function(mSku){
                                scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

                        scope.vm.checkFlag.translate = scope.vm.mastData.translateStatus == null ? 0 : scope.vm.mastData.translateStatus;
                        scope.vm.checkFlag.tax = scope.vm.mastData.hsCodeStatus == null ? 0 : scope.vm.mastData.hsCodeStatus;*/

                    });
/*                    productDetailService.getProductInfo({productId: scope.productInfo.productId}).then(function (res) {
                        scope.vm.productDetails = res.data.productInfo;
                        scope.vm.productCode = res.data
                    });*/
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