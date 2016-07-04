/**
 * @author tony-piao
 * 京东 & 聚美 & 天猫国际 产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("jgjSchema", function (productDetailService,feedMappingService,platformMappingService,$translate,notify,confirm,$q) {
        return {
            restrict: "E",
            templateUrl : "views/product/jgj.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo",
                cartInfo:"=cartInfo"
            },
            link: function (scope) {
                scope.vm = {
                    mastData:null,
                    platform:null,
                    productUrl:""
                };

                initialize();
                scope.saveProduct = saveProduct;
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    productDetailService.getProductPlatform({cartId:scope.cartInfo.value,prodId:scope.productInfo.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.platform = resp.data.platform;

                        if(scope.vm.platform != null){
                            scope.vm.platform.pStatus = scope.vm.platform.pStatus == null ? "WaitingPublish" : scope.vm.platform.pStatus;
                            scope.vm.platform.pStatus = scope.vm.platform.pPublishError != null ? "Failed":scope.vm.platform.pStatus;
                        }

                    });

                    switch(+scope.cartInfo.value){
                        case 26:
                            scope.vm.productUrl = "http://ware.shop.jd.com/onSaleWare/onSaleWare_viewProduct.action?wareId=";
                            break;
                        case 27:
                            scope.vm.productUrl = "http://item.jumeiglobal.com/";
                            break;
                    }
                }

                /**
                 * 更新操作
                 */
                function saveProduct(){
                    if(scope.vm.platform.pBrandName == null){
                        notify.danger("请先确认是否在后台申请过相应品牌");
                        return;
                    }

                    scope.vm.platform.status = "Approved";
                    scope.vm.platform.cartId = +scope.cartInfo.value;

                    productDetailService.updateProductPlatform({prodId:scope.productInfo.productId,platform:scope.vm.platform}).then(function(resp){
                        scope.vm.platform.modified = resp.data.modified;
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    });
                }

            }
        };
    });
});