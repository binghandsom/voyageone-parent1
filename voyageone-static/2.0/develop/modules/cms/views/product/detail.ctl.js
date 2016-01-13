/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/productDetail.service'
], function (cms) {
    return cms.controller('productDetailController', (function () {

        function productDetailController($routeParams, $translate, productDetailService, notify) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.productDetailService = productDetailService;
            this.notify = notify;

            this.productDetails = null;
            this.productDetailsCopy = null;
            // 显示sku列表中
            this.bulkCartStatus = [];
            this.currentImage = "";
        }

        productDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var data = {productId: this.routeParams.productId};
                this.productDetailService.getProductInfo(data)
                    .then(function (res) {
                        this.productDetails = res.data.productInfo;
                        this.productDetailsCopy = angular.copy(this.productDetails);
                    }.bind(this), function (res) {
                        this.notify.danger(res.message);
                    }.bind(this))
            },

            // 保存product详情和product自定义
            updateProductInfo: function () {
                this.productDetailService.updateProductInfo(this.productDetails)
                    .then(function () {
                        this.notify.success (this.translate.instant('TXT_COM_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 取消product详情的变更
            cancelProductInfo: function () {
                this.productDetails.masterFields = angular.copy(this.productDetailsCopy.masterFields);
            },

            // 取消product自定义的变更
            cancelProductCustomInfo: function () {
                this.productDetails.customAttributes = angular.copy(this.productDetailsCopy.customAttributes);
                // 恢复到页面初始化的状态
                this.productDetails.feedInfoModel.attributeList = angular.copy(this.productDetailsCopy.feedInfoModel.attributeList);
                // 删除本次被添加的feedeys
                this.productDetails.feedKeys = angular.copy(this.productDetailsCopy.feedKeys);
            },

            // 保存sku的变更
            updateSkuInfo: function () {
                this.productDetailService.updateSkuInfo(this.productDetails).then(function () {
                    this.notify.success (this.translate.instant('TXT_COM_UPDATE_SUCCESS'));
                }.bind(this))
            },

            // 取消sku的变更
            cancelSkuInfo: function () {
                this.productDetails.skuFields = angular.copy(this.productDetailsCopy.skuFields);
            },

            // sku列表页面选中
            selSkuCart: function (cartId) {
                _.each(this.productDetails.skuFields, function (sku) {
                    sku.SelSkuCarts[cartId] = this.bulkCartStatus[cartId];
                }.bind(this))
            },

            addField: function (data) {
                var newFieldMap = {};
                angular.forEach(data.fields, function (field) {
                    eval("newFieldMap." + field.id + "=field");
                });

                data.complexValues.push({fieldMap: angular.copy(newFieldMap)});
            },

            // 从第三方属性中添加feed属性到product自定义中,或者从product自定义中删除feed属性
            addFeedAttrToCustom: function () {
                _.each(this.productDetails.feedInfoModel.attributeList, function (feedAttr) {
                    if (feedAttr.selected
                        && !_.contains(this.productDetails.feedKeys, feedAttr.key)) {
                        // 设置被选中英文feed属性
                        this.productDetails.customAttributes.orgAtts.push({key: feedAttr.key, value: feedAttr.value, selected: false});
                        // 设置被选中的中文feed属性
                        this.productDetails.customAttributes.cnAtts.push({key: feedAttr.key, value: null});
                        // 设置已经被选中的feed属性key
                        this.productDetails.feedKeys.push(feedAttr.key);
                    }
                }.bind(this))
            },

            // 恢复第三方属性的默认选中
            cancelAddFeedAttrToCustom: function () {
                // 恢复到页面初始化的状态
                this.productDetails.feedInfoModel.attributeList = angular.copy(this.productDetailsCopy.feedInfoModel.attributeList);
                // 移除本次添加的新feed
                var feed = this.productDetails.customAttributes;
                _.each(_.difference(this.productDetails.feedKeys, this.productDetailsCopy.feedKeys), function (key) {
                    feed.orgAtts.splice(_.findLastIndex(feed.orgAtts, {key: key}), 1);
                    feed.cnAtts.splice(_.findLastIndex(feed.cnAtts, {key: key}), 1);
                });
                this.productDetails.customAttributes = angular.copy(feed);
                // 删除本次被添加的feedeys
                this.productDetails.feedKeys = angular.copy(this.productDetailsCopy.feedKeys);
            },

            // 保存所有的变更
            updateProductDetail: function () {
                this.productDetailService.updateProductDetail(this.productDetails)
                    .then(function (){
                        this.notify.success (this.translate.instant('TXT_COM_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 取消所有的变更
            cancelProductDetail: function () {
                this.productDetails = angular.copy(this.productDetailsCopy);
            }
        };

        return productDetailController
    })());
});