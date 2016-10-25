/**
 * Created by tony-piao on 2016/5/20.
 * 店铺分类controller
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    "use strict";
    return cms.controller('categoryController', (function () {

        function CategoryController(platformMappingService, sellerCatService, alert, confirm, $translate) {

            this.platformMappingService = platformMappingService;
            this.sellerCatService = sellerCatService;
            this.alert = alert;
            this.confirm = confirm;
            this.translate = $translate;
            this.carts = [];
            this.tree = [];
            this.source = [];
            this.key = [];
            this.selected = [];
            this.cartInfo = {cart: null, level: 0, maxTag: 0};
            this.newIndex = {value: -1};
        }

        CategoryController.prototype.init = function () {
            var self = this;
            self.platformMappingService.getCarts().then(function (res) {
                self.carts = res.data;
            });

        };

        /**
         * 当选择店铺时初始化页面，并根据店铺深度(level)设置div宽度
         */
        CategoryController.prototype.loadCategories = function () {
            var self = this,
                node;

            self.sellerCatService.init({"cartId": +self.cartInfo.cart}).then(function (res) {
                self.cartInfo.level = res.data.MAX_SELLER_CAT_DEPTH;
                self.cartInfo.maxTag = res.data.MAX_SELLER_CAT_CNT;
            }).then(function () {
                self.sellerCatService.getCat({"cartId": +self.cartInfo.cart}).then(function (res) {

                    self.source = res.data.catTree;
                    node = $('.category-drag-minWidth');
                    self.search(0);

                    switch (+self.cartInfo.level) {
                        case 1:
                            node.css({"width": "90%"});
                            break;
                        case 2:
                            node.css({"width": "48%"});
                            break;
                        case 3:
                            node.css({"width": "32%"});
                            break;
                        case 4:
                            node.css({"width": "24%"});
                            break;
                    }
                });
            });
        };

        CategoryController.prototype.byTagChildrenName = function (arr, index) {
            var key = this.key[index];

            return key ? _.filter(arr, function (item) {
                return item.catName.indexOf(key[index]) > -1;
            }) : arr;
        };

        CategoryController.prototype.search = function (index) {
            var self = this,
                tree = self.tree,
                source = self.source,
                selected = self.selected,
                prev;

            for (; index < self.cartInfo.level; index++) {
                if (!index) {
                    tree[index] = self.byTagChildrenName(source, index);
                } else {
                    prev = selected[index - 1];
                    if (prev)
                        tree[index] = self.byTagChildrenName(prev.children, index);
                    else {
                        tree[index] = [];
                        continue;
                    }
                }

                if (!selected[index]) {
                    selected[index] = tree[index][0];
                } else if (_.isString(selected[index])) {
                    selected[index] = _.find(tree[index], function (item) {
                        return item.catName === selected[index];
                    });
                } else if (tree[index].indexOf(selected[index]) < 0) {
                    var indexSelected = _.find(tree[index], function (item) {
                        return item.catId === selected[index].catId;
                    });

                    if (indexSelected)
                        selected[index] = indexSelected;
                    else
                        selected[index] = tree[index][0];
                }
            }
        };

        CategoryController.prototype.newCategory = function (root, level, openNewCategory) {
            if (this.cartInfo.cart == null) {
                this.alert(this.translate.instant("TXT_STORE_CATEGORY_SELECT"));
                return;
            }
            openNewCategory({root: root, selectObject: this.selected[level], save: this.save, ctrl: this});
        };

        CategoryController.prototype.save = function (parentCatId, catName) {
            var self = this;

            this.selected[this.newIndex.value] = catName;
            self.sellerCatService.addCat({
                "cartId": +this.cartInfo.cart,
                "catName": catName,
                "parentCatId": parentCatId
            }).then(function (res) {
                self.source = res.data.catTree;
                self.search(0);
            });
        };

        CategoryController.prototype.delete = function (node) {
            var self = this;

            self.confirm("TXT_MSG_DELETE_ITEM").then(function () {
                self.sellerCatService.delCat({
                    "cartId": +self.cartInfo.cart,
                    "catId": node.catId,
                    "parentCatId": node.parentCatId
                }).then(function (res) {
                    self.source = res.data.catTree;
                    self.search(0);
                });
            });
        };

        CategoryController.prototype.updateCat = function (node) {
            var self = this;

            if (node.value == null) {
                node.value = 1;
                node.newCatName = node.catName;
            } else {
                node.value = null;
                if (node.newCatName == "" || node.newCatName == null)
                    return;
                self.sellerCatService.updateCat({
                    "cartId": +this.cartInfo.cart,
                    "catId": node.catId,
                    "catName": node.newCatName
                }).then(function (res) {
                    self.source = res.data.catTree;
                    self.search(0);
                });
            }
        };

        CategoryController.prototype.catSort = function ($item, level) {
            var self = this;

            self.selected[0] = $item;
            self.search(level + 1);
        };

        return CategoryController;

    })());
});