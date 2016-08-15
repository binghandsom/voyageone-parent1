/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
],function(cms) {
    cms.directive("masterSchema", function (productDetailService,notify,$rootScope,alert,systemCategoryService, $compile) {
        return {
            restrict: "E",
            templateUrl : "views/product/master.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo",
                cartInfo:"=cartInfo"
            },
            link: function (scope,element) {
                scope.vm = {
                    mastData:null,
                    productComm:null,
                    categoryMark:null,
                    tempImage : {"images1":[],"images2":[],"images3":[],"images4":[],"images5":[],"images6":[],"images7":[],"images8":[],"images9":[]},
                    hsCodeOrigin: null
                };

                initialize();
                scope.masterCategoryMapping = masterCategoryMapping;
                scope.openProImageSetting = openProImageSetting;
                scope.saveProduct = saveProduct;
                scope.pageAnchor = pageAnchor;

                /**
                 * 获取京东页面初始化数据
                 */
                function initialize(){
                    productDetailService.getCommonProductInfo({cartId:"0",prodId:scope.productInfo.productId}).then(function(resp){
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = resp.data.productComm;

                        var _fields = scope.vm.productComm.fields;

                        scope.productInfo.masterField = _fields;

                        /**通知子页面税号状态和翻译状态*/
                        scope.productInfo.checkFlag = new Date().getTime();
                        scope.productInfo.translateStatus = _fields.translateStatus == null ? 0 : +_fields.translateStatus;
                        scope.productInfo.hsCodeStatus =  _fields.hsCodeStatus == null ? 0: +_fields.hsCodeStatus;


                        /**图片显示*/
                        if ($rootScope.imageUrl == undefined) {
                            $rootScope.imageUrl = '';
                        }

                        scope.vm.currentImage = $rootScope.imageUrl.replace('%s', _fields.images1[0].image1);

                        scope.productInfo.feedInfo = scope.vm.mastData.feedInfo;
                        scope.productInfo.lockStatus = scope.vm.mastData.lock == "1" ? true : false;

                        //暂存税号个人
                        scope.vm.hsCodeOrigin = _.find(scope.vm.productComm.schemaFields,function(field){
                            return field.id === "hsCodePrivate";
                        });

                        console.log("hsCodeOrigin",scope.vm.hsCodeOrigin);

                        /**主商品提示*/
                        if(!scope.vm.mastData.isMain){
                            alert("本商品不是平台主商品，如果您需要在天猫或者京东上新，您所修改的信息不会同步到平台上，图片除外。");
                        }
                    });
                }

                /**
                   @description 类目popup
                 * @param productInfo
                 * @param popupNewCategory popup实例
                 */
                function masterCategoryMapping(popupNewCategory) {
                    systemCategoryService.getNewsCategoryList().then(function(res){
                        if (!res.data || !res.data.length) {
                            notify.danger("数据还未准备完毕");
                            return;
                        }

                        popupNewCategory({
                            categories: res.data,
                            from:scope.vm.productComm == null?"":scope.vm.productComm.catPath
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
                        productId:  scope.productInfo.productId,
                        imageType: imageType
                    }).then(function(context){

                        if(context == null)
                            return;

                        if(context.length == 0)
                            return;

                        scope.vm.productComm.modified = context[context.length -1].modified;

                        var imgType = null;
                        angular.forEach(context,function(item){
                            imgType = item.imageType;
                            scope.vm.tempImage[item.imageType].push($rootScope.imageUrl.replace('%s', item.imageName));
                        });

                        _.map(scope.vm.productComm.schemaFields, function(item){
                            if(item.id == imgType){
                                item.complexValues.splice(0,item.complexValues.length);
                                angular.forEach(context[context.length -1].imageSchema[0].complexValues,function(image){
                                    item.complexValues.push(image);
                                });

                            }
                        });

                    });
                }

                /**
                 * 更新操作
                 */
                function saveProduct(){
                    if (!validSchema()) {
                        alert("保存失败，请查看产品的属性是否填写正确！");
                        focusError();
                        return;
                    }

                    productDetailService.updateCommonProductInfo({prodId:scope.productInfo.productId,productComm:scope.vm.productComm}).then(function(resp){
                        scope.vm.productComm.modified = resp.data.modified;
                        scope.productInfo.translateStatus = resp.data.translateStatus == null ? 0 : +resp.data.translateStatus;
                        scope.productInfo.hsCodeStatus =  resp.data.hsCodeStatus == null ? 0: +resp.data.hsCodeStatus ;

                        //通知子页面
                        scope.productInfo.checkFlag = new Date().getTime();
                        if(!scope.vm.categoryMark)
                            scope.productInfo.masterCategory = new Date().getTime();
                        notify.success("更 新 成 功 ");
                    },function(){
                        alert("更新失败","错误提示");
                    });
                }

                function validSchema(){
                    return scope.vm.productComm == null || scope.vm.productComm.schemaFields == null ? false : scope.schemaForm.$valid;
                }

                /**
                 * 右侧导航栏
                 * @param area 区域
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(area,speed){
                    var offsetTop = 0;
                    if(area != 'master')
                        offsetTop = element.find("#"+area).offset().top;
                    $("body").animate({ scrollTop:  offsetTop-100}, speed);
                }

                function focusError(){
                    var firstError = element.find("schema .ng-invalid:first");
                    firstError.focus();
                    firstError.addClass("focus-error");
                }

            }
        };
    });
});