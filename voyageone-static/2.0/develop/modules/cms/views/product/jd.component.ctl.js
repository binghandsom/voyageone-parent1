/**
 * @author tony-piao
 * 京东产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("jdSchema", function (productDetailService,feedMappingService,productDetailService,platformMappingService) {
        return {
            restrict: "E",
            replace: false,
            transclude: true,
            templateUrl : "views/product/jd.component.tpl.html",
            scope: {
                productId: "=productId"
            },//独立的scope对象*/
            link: function (scope,element) {
                scope.vm = {
                    productDetails:null,
                    productCode:"",
                    mastData:null,
                    platform:null,
                    skuTemp:{},
                    checkFlag:{translate:0, tax:0, category:0, attribute:0}
                };

                initialize();
                scope.jdCategoryMapping = jdCategoryMapping;
                scope.openSellerCat = openSellerCat;
                scope.saveProduct = saveProduct;
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    productDetailService.getProductPlatform({cartId:"26",prodId:scope.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.platform = resp.data.platform;

                        _.each(scope.vm.mastData.skus,function(mSku){
                                scope.vm.skuTemp[mSku.skuCode] = mSku;
                        });

                        //设置check状态 categoryStatus     hsCodeStatus
                        scope.vm.checkFlag.translate =  scope.vm.mastData.translateStatus;
                        scope.vm.checkFlag.tax = scope.vm.mastData.hsCodeStatus;
                        scope.vm.checkFlag.category = scope.vm.platform.pCatPath == null?0:1;

                    });
                    productDetailService.getProductInfo({productId: scope.productId})
                        .then(function (res) {
                            scope.vm.productDetails = res.data.productInfo;
                            scope.vm.productCode = res.data
                        })
                }

                function jdCategoryMapping(productInfo, popupNewCategory) {
                    platformMappingService.getPlatformCategories({cartId: "26"})
                        .then(function (res) {
                            if (!res.data || !res.data.length) {
                                alert("没数据");
                                return null;
                            }
                            return popupNewCategory({
                                from:"",
                                categories: res.data
                            });
                        }).then(function (context) {
                            productDetailService.changePlatformCategory({cartId:"26",prodId:scope.productId,catId:context.selected.catId}).then(function(resp){
                                scope.vm.platform = resp.data.platform;
                            });
                        });
                }

                function openSellerCat (openAddChannelCategoryEdit) {
                    var selList = [{"id": scope.productId, "code": scope.vm.productDetails.productCode}];
                    openAddChannelCategoryEdit(selList).then(function (context) {

                    })
                }

                function saveProduct(){
                    validSchema()
/*                    scope.vm.platform.cartId = "26";
                    productDetailService.updateProductPlatform({prodId:scope.productId,platform:scope.vm.platform}).then(function(resp){

                     });*/
                }

                function validSchema(){
                    scope.vm.platform.schemaFields.some(function(schema){
                        console.log(schema);
                    });
                }
            }
        };
    });
});