/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("masterSchema", function (productDetailService,notify,$rootScope,alert,systemCategoryService) {
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
                    productComm:null,
                    categoryMark:null,
                    tempImage : {"image1":[],"image2":[],"image3":[],"image4":[]}
                };

                initialize();
                scope.masterCategoryMapping = masterCategoryMapping;
                scope.openProImageSetting = openProImageSetting;
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
                function masterCategoryMapping(popupNewCategory) {
                    systemCategoryService.getNewsCategoryList().then(function(res){
                        popupNewCategory({
                            categories: res.data
                        }).then(function(context){
                            //判断类目是否改变
                            scope.vm.categoryMark = scope.vm.productComm.catPath == context.selected.catPath;

                            scope.vm.productComm.catId = context.selected.catId;
                            scope.vm.productComm.catPath = context.selected.catPath;

                        });
                    });
                }

                /**
                 * 添加图片
                 */
                function openProImageSetting(imageType,openImageSetting) {
                    openImageSetting({
                        product:  scope.vm.productDetails,
                        imageType: imageType
                    }).then(function(context){
                        scope.vm.tempImage[context.imageType].push(context.base64);
                        scope.vm.productDetails = context.productInfo;
                    });
                }

                /**
                 * 更新操作
                 */
                function saveProduct(){
                    if (!validSchema()) {
                        return alert("保存失败，请查看产品的属性是否填写正确！");
                    }

                    productDetailService.updateCommonProductInfo({prodId:scope.productInfo.productId,productComm:scope.vm.productComm}).then(function(resp){
                        scope.vm.productComm.modified = resp.data.modified;
                        scope.productInfo.translateStatus = resp.data.translateStatus == null ? 0 : +resp.data.translateStatus;
                        scope.productInfo.hsCodeStatus =  resp.data.hsCodeStatus == null ? 0: +resp.data.hsCodeStatus ;

                        //通知子页面
                        scope.productInfo.checkFlag = new Date().getTime();
                        if(!scope.vm.categoryMark)
                            scope.productInfo.masterCategory = new Date().getTime();
                        notify.success("更新成功!");
                    });
                }

                function validSchema(){
                    return scope.vm.productComm == null || scope.vm.productComm.schemaFields == null ? false : scope.schemaForm.$valid;
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