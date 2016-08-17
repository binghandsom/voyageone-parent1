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

        function AttributeDetailController($translate, $routeParams,$q,popups, menuService,productDetailService,platformMappingService) {

            var self = this;

            self.$translate = $translate;
            self.q = $q;
            self.popups = popups;
            self.upEntity = angular.fromJson($routeParams.upEntity);
            self.menuService = menuService;
            self.productDetailService = productDetailService;
            self.platformMappingService = platformMappingService;
            self.searchInfo = {
                cartId: self.upEntity.cartId,
                categoryType: self.upEntity.categoryType,
                categoryId: self.upEntity.categoryId,
                categoryPath: self.upEntity.categoryPath
            };
        }

        AttributeDetailController.prototype = {
            init: function () {
                var self = this;
                self.menuService.getPlatformType().then(function (resp) {
                    self.platformTypes = _.filter(resp, function (cart) {
                        return cart.value != 0 && cart.value != 1
                    });
                });

                //self.searchInfo
                self.platformMappingService.get({
                    cartId: 23,
                    categoryType: 2,
                    categoryPath:"手表>智能腕表"
                }).then(function(res){
                    self.fields = res.data.schema;
                });
            },
            jdCategoryMapping: function () {
                var self = this;
                self.productDetailService.getPlatformCategories({cartId: self.searchInfo.cartId})
                    .then(function (res) {
                        return self.q(function (resolve, reject) {
                            if (!res.data || !res.data.length) {
                                self.notify.danger("数据还未准备完毕");
                                reject("数据还未准备完毕");
                            } else {
                                resolve(self.popups.popupNewCategory({
                                    from: "",
                                    categories: res.data,
                                    divType: ">"
                                }));
                            }
                        });
                    }).then(function (context) {
                        self.searchInfo.categoryPath = context.selected.catPath;
                        self.searchInfo.categoryId = context.selected.catId;
                });
            }

        };

        return AttributeDetailController;
    })())
});
