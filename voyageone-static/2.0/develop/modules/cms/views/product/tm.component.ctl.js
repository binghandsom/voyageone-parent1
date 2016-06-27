/**
 * @author tony-piao
 * 天猫产品概述（schema）
 */
define([
    'cms'
],function(cms) {
    cms.directive("tmSchema", function ($routeParams, $rootScope, $translate, productDetailService, feedMappingService, notify, confirm, alert) {
        return {
            restrict: "E",
            replace: false,
            transclude: true,
            templateUrl : "views/product/tm.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productId: "=productId"
            },
            link: function (scope) {

                scope.vm = {
                    productDetails : null,
                    productStatusList : null,
                    productDetailsCopy : null,
                    currentImage : "",
                    errorMsg : "",
                    showInfoFlag : true,

                    tempImage : {"image1":[],"image2":[],"image3":[],"image4":[]}
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

                // 保存所有的变更
                scope.updateProductDetail = updateProductDetail;
                function updateProductDetail () {

                    if (!validSchema()&& scope.vm.productDetails.productStatus.approveStatus == 'Approved') {
                        return alert("保存失败，请查看schema的属性是否填写正确！");
                    }

                    /**推算产品状态
                       如果该产品以前不是approve,这次变成approve的
                       变成ready,或者以前是approve这次数据发生变化的*/


                    productDetailService.updateProductDetail(scope.vm.productDetails)
                        .then(function (res) {
                            scope.vm.productDetails.modified = res.modified;
                            scope.vm.productDetails.productStatus.approveStatus = res.approveStatus;
                            scope.vm.productDetails.productStatus.isApproved = res.isApproved;
                            scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                            notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        });
                }

                scope.cancelProductDetail = cancelProductDetail;
                // 取消所有的变更
                function cancelProductDetail () {
                    scope.vm.productDetails = angular.copy(scope.vm.productDetailsCopy);
                    scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady;
                }
                scope.openCategoryMapping = openCategoryMapping;

                function openCategoryMapping (productInfo, popupNewCategory) {
                    feedMappingService.getMainCategories()
                        .then(function (res) {
                            popupNewCategory({
                                categories: res.data,
                                from: productInfo.categoryFullPath
                            }).then(this.bindCategory);

                        }.bind(this));
                }
                /**
                 * 在类目 Popup 确定关闭后, 为相关类目进行绑定
                 * @param {{from:string, selected:object}} context Popup 返回的结果信息
                 */
                scope.bindCategory = bindCategory;
                function bindCategory (context) {

                    confirm($translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                        .then(function () {
                            var data = {
                                prodIds: [scope.vm.productDetails.productId],
                                catId: context.selected.catId,
                                catPath: context.selected.catPath
                            };
                            productDetailService.changeCategory(data).then(function (res) {
                                if (res.data.isChangeCategory) {
                                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                    initialize();
                                }
                                else
                                // TODO 需要enka设计一个错误页面 res.data.publishInfo
                                    notify("有商品处于上新状态,不能切换类目");
                            })
                        });
                }

                scope.openProImageSetting = openProImageSetting;
                function openProImageSetting(productDetails, imageType,openImageSetting) {
                    openImageSetting({
                        product:  productDetails,
                        imageType: imageType
                    }).then(this.imageCallBack.bind(this));
                }

                scope.imageCallBack = imageCallBack;
                function imageCallBack(context){
                    scope.vm.tempImage[context.imageType].push(context.base64);

                    scope.vm.productDetails = context.productInfo;
                    scope.vm._orgChaName = context.orgChaName;
                    scope.vm._isminimall = context.isminimall;
                    scope.vm._isMain = context.isMain;

                    scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                    scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady
                }

                scope.pageAnchor = pageAnchor;
                function pageAnchor(index,speed){
                    var offsetTop = 0;
                    if(index != 1)
                        offsetTop = ($("#tm"+index).offset().top);
                    $("body").animate({ scrollTop:  offsetTop-70}, speed);
                }

                function validSchema(){
                    return scope.vm.productDetails == null ? false : scope.schemaForm.$valid && scope.skuForm.$valid;
                }
            }
        };
    });
});