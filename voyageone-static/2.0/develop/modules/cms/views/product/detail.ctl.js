/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service'
], function (cms) {
    return cms.controller('productDetailController', (function () {

        function ProductDetailController($routeParams, $translate, productDetailService, feedMappingService, notify, confirm) {

            this.routeParams = $routeParams;
            this.translate = $translate;
            this.productDetailService = productDetailService;
            this.feedMappingService = feedMappingService;
            this.notify = notify;
            this.confirm = confirm;

            this.productDetails = null;
            this.productDetailsCopy = null;
            this.currentImage = "";
            this.errorMsg = "";
            this.showInfoFlag = true;
        }

        ProductDetailController.prototype = {

            // 获取初始化数据
            initialize: function () {
                var self = this;
                var data = {productId: self.routeParams.productId};
                self.productDetailService.getProductInfo(data)
                    .then(function (res) {
                        self.productDetails = res.data.productInfo;
                        self.productDetailsCopy = angular.copy(this.productDetails);
                        self.showInfoFlag = self.productDetails.productDataIsReady

                    }, function (res) {
                        self.errorMsg = res.message;
                        self.showInfoFlag = false;
                    })
            },

            // 保存所有的变更
            updateProductDetail: function () {

                this.productDetailService.updateProductDetail(this.productDetails)
                    .then(function (res) {
                        this.productDetails.modified = res.data.modified;
                        this.productDetailsCopy = angular.copy(this.productDetails);
                        this.notify.success(this.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    }.bind(this))
            },

            // 取消所有的变更
            cancelProductDetail: function () {
                this.productDetails = angular.copy(this.productDetailsCopy);
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

                var $ = this;
                this.confirm(this.translate.instant('TXT_MSG_CONFIRM_IS_CHANGE_CATEGORY')).result
                    .then(function () {
                        var data = {
                            prodIds: [$.productDetails.productId],
                            catId: context.selected.catId,
                            catPath: context.selected.catPath
                        };
                        $.productDetailService.changeCategory(data).then(function (res) {
                            if (res.data.isChangeCategory) {
                                $.notify.success($.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                                $.initialize()
                            }
                            else
                            // TODO 需要enka设计一个错误页面 res.data.publishInfo
                                $.notify("有商品处于上新状态,不能切换类目");
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
        };

        return ProductDetailController
    })());
});