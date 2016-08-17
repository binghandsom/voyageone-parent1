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

        function AttributeDetailController($routeParams, popups, menuService, productDetailService, platformMappingService) {

            var self = this;
            var searchJson = $routeParams.upEntity;

            self.searchInfo = searchJson ? angular.fromJson(searchJson) : {};

            self.popups = popups;
            self.productDetailService = productDetailService;
            self.platformMappingService = platformMappingService;

            menuService.getPlatformType().then(function (resp) {
                self.platformTypes = _.filter(resp, function (cart) {
                    return cart.value != 0 && cart.value != 1
                });

                var searchInfo = self.searchInfo;

                if (!searchInfo.cartId)
                    searchInfo.cartId = self.platformTypes[0].value;

                self.tryGet();
            });
        }

        AttributeDetailController.prototype.tryGet = function () {

            var self = this,
                searchInfo = self.searchInfo;

            if (searchInfo.categoryType != 1 && searchInfo.categoryType != 2)
                return;

            if (searchInfo.categoryType == 2 && !searchInfo.categoryPath)
                return;

            self.categoryTitle = (self.searchInfo.categoryType == 1) ? "全类目" : self.searchInfo.categoryPath;

            self.$get();
        };

        AttributeDetailController.prototype.$get = function () {

            var self = this,
                platformMappingService = self.platformMappingService;

            platformMappingService.get(self.searchInfo).then(function (res) {
                self.fields = res.data.schema;
            });
        };

        AttributeDetailController.prototype.openCategorySelector = function () {

            var self = this,
                productDetailService = self.productDetailService;

            productDetailService.getPlatformCategories(self.searchInfo).then(function (resp) {

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
                    self.searchInfo.categoryPath = context.selected.catPath;
                    self.searchInfo.categoryId = context.selected.catId;
                    self.tryGet();
                });
            });
        };

        return AttributeDetailController;
    })())
});
