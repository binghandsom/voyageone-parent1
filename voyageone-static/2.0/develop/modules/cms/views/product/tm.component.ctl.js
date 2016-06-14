/**
 * @author tony-piao
 * 天猫产品概述（schema）
 */
define([
    'cms',
    'underscore',
    'modules/cms/enums/FieldTypes'
],function(cms , _ , FieldTypes) {
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
            link: function (scope,element) {

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
                    // 尝试检查商品的 field 验证
                    var invalidNames = validSchema(scope.vm.productDetails);

                    if (invalidNames.length && scope.vm.productDetails.productStatus.approveStatus == 'Approved') {
                        return alert({id: 'TXT_MSG_INVALID_FEILD', values: {fields: invalidNames.join(', ')}});
                    }

                    // 推算产品状态
                    //// 如果该产品以前不是approve,这次变成approve的
                    //if (self.productDetails.productStatus.statusInfo.isApproved
                    //    && !self.productDetailsCopy.productStatus.statusInfo.isApproved)
                    //    self.productDetails.productStatus.approveStatus = Status.APPROVED;
                    //// 变成ready,或者以前是approve这次数据发生变化的
                    //else if (self.productDetails.productStatus.statusInfo.isWaitingApprove
                    //    || (self.productDetails.productStatus.statusInfo.isApproved
                    //    && self.productDetailsCopy.productStatus.statusInfo.isApproved
                    //    && self.productDetails != self.productDetailsCopy))
                    //    self.productDetails.productStatus.approveStatus = Status.READY;

                    productDetailService.updateProductDetail(scope.vm.productDetails)
                        .then(function (res) {
                            scope.vm.productDetails.modified = res.modified;
                            scope.vm.productDetails.productStatus.approveStatus = res.approveStatus;
                            scope.vm.productDetails.productStatus.isApproved = res.isApproved;
                            //self.productDetailService._setProductStatus(self.productDetails.productStatus);
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

                    // TODO 弹出新的popup画面,展示主类目预览
                    //this.feedMappingService.setMapping({
                    //    from: context.from,
                    //    to: context.selected.catPath
                    //}).then(function (res) {
                    //    // 从后台获取更新后的 mapping
                    //    // 刷新数据
                    //    var feedCategoryBean = this.findCategory(context.from);
                    //    feedCategoryBean.mapping = _.find(res.data, function (m) {
                    //        return m.defaultMapping === 1;
                    //    });
                    //}.bind(this));
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
                    //self.productDetailService._setProductStatus(self.productDetails.productStatus);
                    scope.vm._orgChaName = context.orgChaName;
                    scope.vm._isminimall = context.isminimall;
                    scope.vm._isMain = context.isMain;

                    scope.vm.productDetailsCopy = angular.copy(scope.vm.productDetails);
                    scope.vm.showInfoFlag = scope.vm.productDetails.productDataIsReady
                }

                /**
                 * 验证一组字段, 是否通过了 ng-form 验证
                 * @param {object|Array} fields
                 * @returns Array.<Field>
                 */
                function validFields(fields) {
                    return _.filter(fields, function (field) {
                        if (field.form && field.type === FieldTypes.multiComplex) {
                            return !field.complexValues.some(function (value) {
                                if (!value.fieldMap) return false;
                                return !validFields(value.fieldMap).length;
                            });
                        }
                        // 如果是复杂类型, 并且启用了检查, 那么就递归
                        if (field.form && field.type === FieldTypes.complex)
                            return validFields(field.fields).length;
                        // 简单类型就直接检查
                        return field.$valid === false;
                    });
                }

                /**
                 * 验证商品 Schema, 返回无效的属性名
                 * @param schema
                 * @returns Array.<String>
                 */
                function validSchema(schema) {
                    var skuValues = schema.skuFields.complexValues;
                    var hasSkuValues = skuValues && skuValues.length;
                    var invalid = [];

                    if (hasSkuValues) invalid = skuValues.reduce(function(arr, value) {
                        if (!value.fieldMap) return;
                        return arr.concat(validFields(value.fieldMap));
                    }, invalid);

                    invalid = invalid.concat(validFields(schema.masterFields));

                    return invalid.reduce(function (arr, item) {
                        if (arr.indexOf(item.name) < 0)
                            arr.push(item.name);
                        return arr;
                    }, []);
                }
            }
        };
    });
});