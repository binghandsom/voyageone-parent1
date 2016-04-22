/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'underscore',
    'modules/cms/enums/Status',
    'modules/cms/enums/FieldTypes',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service'
], function (cms, _, Status, FieldTypes) {

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

    return cms.controller('productDetailController', (function () {

        function ProductDetailController($routeParams, $translate, productDetailService, feedMappingService, notify, confirm, alert) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.productDetailService = productDetailService;
            this.feedMappingService = feedMappingService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;

            this.productDetails = null;
            this.productDetailsCopy = null;
            this.currentImage = "";
            this.errorMsg = "";
            this.showInfoFlag = true;

            this.tempImage = {"image1":[],"image2":[],"image3":[],"image4":[]};
        }

        ProductDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var self = this;
                var data = {productId: self.routeParams.productId};
                self.productDetailService.getProductInfo(data)
                    .then(function (res) {
                        self.productDetails = res.data.productInfo;
                        self.inventoryList = res.data.inventoryList;
                        self._orgChaName = res.data.orgChaName;
                        self._isminimall = res.data.isminimall;
                        self._isMain = res.data.isMain;

                        self.productDetailsCopy = angular.copy(self.productDetails);
                        self.showInfoFlag = self.productDetails.productDataIsReady

                    }, function (res) {
                        self.errorMsg = res.message;
                        self.showInfoFlag = false;
                    })
            },

            // 保存所有的变更
            updateProductDetail: function () {
                var self = this;

                // 尝试检查商品的 field 验证
                var invalidNames = validSchema(self.productDetails);

                if (invalidNames.length && self.productDetails.productStatus.statusInfo.isApproved) {
                    return self.alert({id: 'TXT_MSG_INVALID_FEILD', values: {fields: invalidNames.join(', ')}});
                }

                // 推算产品状态
                // 如果该产品以前不是approve,这次变成approve的
                if (self.productDetails.productStatus.statusInfo.isApproved
                    && !self.productDetailsCopy.productStatus.statusInfo.isApproved)
                    self.productDetails.productStatus.approveStatus = Status.APPROVED;
                // 变成ready,或者以前是approve这次数据发生变化的
                else if (self.productDetails.productStatus.statusInfo.isWaitingApprove
                    || (self.productDetails.productStatus.statusInfo.isApproved
                    && self.productDetailsCopy.productStatus.statusInfo.isApproved
                    && self.productDetails != self.productDetailsCopy))
                    self.productDetails.productStatus.approveStatus = Status.READY;

                this.productDetailService.updateProductDetail(this.productDetails)
                    .then(function (res) {
                        self.productDetails.modified = res.data.modified;
                        this.productDetailService._setProductStatus(self.productDetails.productStatus);
                        self.productDetailsCopy = angular.copy(self.productDetails);
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 取消所有的变更
            cancelProductDetail: function () {
                this.productDetails = angular.copy(this.productDetailsCopy);
                this.showInfoFlag = this.productDetails.productDataIsReady;
            },

            openCategoryMapping: function (productInfo, popupNewCategory) {
                this.feedMappingService.getMainCategories()
                    .then(function (res) {

                        popupNewCategory({

                            categories: res.data,
                            from: productInfo.categoryFullPath
                        }).then(this.bindCategory.bind(this));

                    }.bind(this));
            },
            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:string, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {

                var self = this;
                this.confirm(this.translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                    .then(function () {
                        var data = {
                            prodIds: [self.productDetails.productId],
                            catId: context.selected.catId,
                            catPath: context.selected.catPath
                        };
                        self.productDetailService.changeCategory(data).then(function (res) {
                            if (res.data.isChangeCategory) {
                                self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                self.initialize()
                            }
                            else
                            // TODO 需要enka设计一个错误页面 res.data.publishInfo
                                self.notify("有商品处于上新状态,不能切换类目");
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
            },

            openImageSetting: function(productDetails, imageType,openImageSetting) {
                openImageSetting({
                    product:  productDetails,
                    imageType: imageType
                }).then(this.imageCallBack.bind(this));
            },

            imageCallBack: function(context){
                var self = this;
                self.tempImage[context.imageType].push(context.base64);

                self.productDetails = context.productInfo;
                self._orgChaName = context.orgChaName;
                self._isminimall = context.isminimall;
                self._isMain = context.isMain;

                self.productDetailsCopy = angular.copy(self.productDetails);
                self.showInfoFlag = self.productDetails.productDataIsReady
            }
        };

        return ProductDetailController
    })());
});