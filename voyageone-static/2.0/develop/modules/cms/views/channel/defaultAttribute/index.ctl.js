/**
 * @description 平台默认属性设置一览
 * @date 2016-8-11
 */
define(function (require) {

    require('modules/cms/controller/popup.ctl');

    var _ = require('underscore');
    var cms = require('cms');

    cms.controller('defaultAttributeController', (function () {

        function DefaultAttributeController($q, popups, alert, confirm, menuService, $productDetailService, platformMappingService) {

            var self = this;

            menuService.getPlatformType().then(function (resp) {
                self.cartList = _.filter(resp, function (cart) {
                    return cart.value != 0 && cart.value != 1
                });
            });

            self.$productDetailService = $productDetailService;

            // self.rePriceService.getPlatformCategories()
            //     .then(function (res) {
            //         return self.q(function (resolve, reject) {
            //             if (!res.data || !res.data.length) {
            //                 self.notify.danger("数据还未准备完毕");
            //                 reject("数据还未准备完毕");
            //             } else {
            //                 resolve(self.popups.popupNewCategory({
            //                     from: "",
            //                     categories: res.data,
            //                     divType: ">"
            //                 }));
            //             }
            //         });
            //     }).then(function (context) {
            //     self.searchInfo.categoryPath = context.selected.catPath;
            //     self.searchInfo.categoryId = context.selected.catId;
            // });
            //
            // $productDetailService.getPlatformCategories({cartId: self.searchInfo.cartId})

            self.q = $q;
            self.popups = popups;
            self.alert = alert;
            self.confirm = confirm;
            self.platformMappingService = platformMappingService;
            self.searchInfo = {
                cartId: null,
                categoryType: 3,
                categoryId: null
            };
            self.paging = {
                curr: 1, total: 0, fetch: function () {
                    self.search();
                }
            };
        }

        DefaultAttributeController.prototype.openCategorySelector = function () {

            var self = this,
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
                    self.searchInfo.categoryPath = context.selected.catPath;
                    self.searchInfo.categoryId = context.selected.catId;
                });
            })
        };

        DefaultAttributeController.prototype.clear = function () {
            var self = this;
            self.searchInfo = {
                cartId: null,
                categoryType: 3,
                categoryId: null
            };
        };

        DefaultAttributeController.prototype.search = function () {
            var self = this,
                searchInfo = self.searchInfo,
                paging = self.paging;

            self.platformMappingService.page({
                pageIndex: paging.curr - 1,
                pageRowCount: paging.size,
                parameters: {
                    "cartId": !searchInfo.cartId ? null : +searchInfo.cartId,
                    "categoryType": +searchInfo.categoryType,
                    "categoryPath": searchInfo.categoryPath
                }
            }).then(function (resp) {
                paging.total = resp.data.total;
                self.dataList = resp.data.list;
            });
        };

        DefaultAttributeController.prototype.deleteItem = function (item) {
            var self = this;
            self.confirm("TXT_MSG_DELETE_ITEM").then(function () {
                self.platformMappingService.delete(item).then(function (resp) {
                    if (resp.data) {
                        self.alert("TXT_MSG_DELETE_SUCCESS").then(function () {
                            self.search();
                        });
                    } else {
                        self.alert("TXT_MSG_DELETE_FAIL");
                    }
                });
            });
        };


        // DefaultAttributeController.prototype = {
        //
        //     clear: function () {
        //         var self = this;
        //         _.each(self.searchInfo, function (attr, key) {
        //             self.searchInfo[key] = null;
        //         });
        //     },
        //     search: function () {
        //         var self = this;
        //         var _upEntity = _.extend(self.paging, {
        //             "cartId": +self.searchInfo.cartId,
        //             "categoryType": +self.searchInfo.categoryType,
        //             "categoryPath": self.searchInfo.categoryPath
        //         });
        //         self.platformMappingService.page(_upEntity).then(function (res) {
        //             self.paging.total = res.data.total;
        //             self.dataList = res.data.list;
        //         });
        //     },
        //     switchType: function () {
        //         this.searchInfo.categoryPath = this.searchInfo.categoryType == '2' ? this.searchInfo.categoryPath : '';
        //     },
        //     deleteItem: function (item) {
        //         var self = this;
        //         self.confirm("TXT_MSG_DELETE_ITEM").then(function () {
        //             self.platformMappingService.deleteItem().then(function (res) {
        //                 if (res.data)
        //                     self.notify.success("TXT_MSG_DELETE_SUCCESS");
        //                 else
        //                     self.notify.danger("TXT_MSG_DELETE_FAIL");
        //             });
        //         });
        //     }
        // };


        return DefaultAttributeController;
    })())
});