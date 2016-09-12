/**
 * @description 平台默认属性设置一览=>新建默认设置
 * @date 2016-8-11
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/product.detail.service',
    'modules/cms/directives/defaultAttr.directive'
], function (cms) {
    cms.controller('attributeDetailController', (function () {

        function AttributeDetailController($scope, $routeParams, alert, notify, popups, confirm, menuService,
                                           $productDetailService, platformMappingService) {
            var self = this;

            self.platformMappingFrontId = $routeParams.upEntity;
            self.popups = popups;
            self.notify = notify;
            self.$productDetailService = $productDetailService;
            self.platformMappingService = platformMappingService;
            self.menuService = menuService;
            self.$scope = $scope;
            self.alert = alert;
            self.confirm = confirm;
        }

        AttributeDetailController.prototype.init = function () {

            var self = this,
                platformMappingFrontId = self.platformMappingFrontId;

            // 没有就直接加载
            if (!platformMappingFrontId) {
                self.searchInfo = {};
                self.loadCartListAndTryGet();
                return;
            }

            // 否则, 需要去 local storage 里加载数据
            // 如果没有, 需要去 session 里加载
            // 如果有, 需要把数据转移到 session 中
            // 如果最终都没有, 就直接尝试加载
            var platformMapping, platformMappingJson = localStorage.getItem(platformMappingFrontId);

            if (platformMappingJson) {
                sessionStorage.setItem(platformMappingFrontId, platformMappingJson);
                localStorage.removeItem(platformMappingFrontId);
                platformMapping = angular.fromJson(platformMappingJson);
            } else {
                platformMappingJson = sessionStorage.getItem(platformMappingFrontId);
                platformMapping = angular.fromJson(platformMappingJson);
            }

            self.searchInfo = platformMapping;

            self.loadCartListAndTryGet();
        };

        AttributeDetailController.prototype.loadCartListAndTryGet = function () {

            var self = this,
                menuService = self.menuService;

            menuService.getPlatformType().then(function (resp) {
                // 过滤并转换加载的数据, 以便不修改原始数据
                self.platformTypes = _.map(resp, function (cart) {
                    return {name: cart.name, value: +cart.value};
                }).filter(function (cart) {
                    return cart.value != 0 && cart.value != 1
                });

                var searchInfo = self.searchInfo;

                if (!searchInfo.cartId)
                    searchInfo.cartId = self.platformTypes[0].value;

                self.tryGet();
            });
        };

        AttributeDetailController.prototype.tryGetCart = function () {
            var self = this,
                searchInfo = self.searchInfo;

            if (!self.fields) {
                self.tryGet();
                return;
            }

            self.confirm('切换平台, 会清空您<strong>未保存</strong>的所有内容。确认要切换么?').then(function () {
                if (searchInfo.categoryType == 1) {
                    self.tryGet();
                    return;
                }
                self.fields = null;
                searchInfo.categoryPath = null;
                searchInfo.categoryId = null;
            }, function () {
                searchInfo.cartId = self.lastCartId;
            });
        };

        AttributeDetailController.prototype.tryGetCategory = function () {
            var self = this,
                searchInfo = self.searchInfo;

            if (!self.fields) {
                self.tryGet();
                return;
            }

            self.confirm('切换类目, 会清空您<strong>未保存</strong>的所有内容。确认要切换么?').then(function () {
                self.tryGet();

                if(searchInfo.categoryType == 1)
                    searchInfo.categoryPath = null;
            }, function () {
                searchInfo.categoryType = self.lastCategoryType;
            });
        };

        AttributeDetailController.prototype.tryGet = function () {

            var self = this,
                searchInfo = self.searchInfo;

            if (searchInfo.categoryType != 1 && searchInfo.categoryType != 2) {
                self.fields = null;
                return;
            }

            if (searchInfo.categoryType == 2 && !searchInfo.categoryPath) {
                self.fields = null;
                return;
            }

            self.categoryTitle = (self.searchInfo.categoryType == 1) ? "全类目" : self.searchInfo.categoryPath;
            self.lastCartId = searchInfo.cartId;
            self.lastCategoryType = searchInfo.categoryType;

            self.$get();
        };

        AttributeDetailController.prototype.$get = function () {

            var self = this,
                platformMappingService = self.platformMappingService;

            platformMappingService.get(self.searchInfo).then(function (resp) {
                var data = resp.data;
                if (!data) {
                    self.modified = null;
                    self.fields = null;
                    return;
                }
                self.modified = data.modified;
                self.fields = data.schema;
            });
        };

        AttributeDetailController.prototype.openCategorySelector = function () {

            var self = this,
                searchInfo = self.searchInfo,
                $productDetailService = self.$productDetailService;

            $productDetailService.getPlatformCategories({cartId: self.searchInfo.cartId}).then(function (resp) {

                var categoryList = resp.data;

                if (!categoryList || !categoryList.length) {
                    self.alert("数据还未准备完毕");
                    return;
                }

                self.popups.popupNewCategory({
                    from: "",
                    categories: categoryList,
                    divType: ">"
                }).then(function (context) {

                    var selected = context.selected;

                    if (searchInfo.categoryPath === selected.catPath)
                        return;

                    if (!self.fields) {
                        searchInfo.categoryPath = selected.catPath;
                        searchInfo.categoryId = selected.catId;
                        self.tryGet();
                        return;
                    }

                    self.confirm('切换类目, 会清空您<strong>未保存</strong>的所有内容。确认要切换么?').then(function () {
                        searchInfo.categoryPath = selected.catPath;
                        searchInfo.categoryId = selected.catId;
                        self.tryGet();
                    });
                });
            });
        };

        AttributeDetailController.prototype.save = function () {

            var self = this,
                fields = self.fields,
                searchInfo = self.searchInfo,
                platformMappingService = self.platformMappingService;

            platformMappingService.save({
                cartId: +searchInfo.cartId,
                categoryType: +searchInfo.categoryType,
                categoryPath: searchInfo.categoryPath,
                schema: fields,
                modified: self.modified
            }).then(function (resp) {
                self.modified = resp.data;
                self.notify.success('TXT_SAVE_SUCCESS');
            });
        };

        AttributeDetailController.prototype.close = function () {
            window.opener.focus();
            window.close();
        };

        return AttributeDetailController;
    })())
});
