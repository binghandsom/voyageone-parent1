/**
 * Created by sofia on 5/19/2016.
 */
define([
    'cms'
], function (cms) {

    function flatCategories(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.catId] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatCategories(curr.children, curr));
            return map;
        }, {});
    }

    cms.controller('popAddChannelCategoryCtrl', (function () {

        function PopAddChannelCategoryCtrl(context, $rootScope, $addChannelCategoryService) {
            this.code = context.productIds;
            this.cartList = [];
            this.channelCategoryList = null;
            this.isSelectCid = [];
            this.cartId = $rootScope.platformType.cartId.toString();
            this.addChannelCategoryService = $addChannelCategoryService;
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                self.addChannelCategoryService.init({"code": self.code, "cartId": self.cartId}).then(function (res) {
                    self.cartList = res.data.cartList;
                    self.isSelectCid = res.data.isSelectCid;
                    self.channelCategoryList = res.data.channelCategoryList;
                });
            },

            /**
             * 点击保存按钮时
             */
            save: function () {

                var cIds = [], cNames = [], fullCNames = [], fullCIds = [];

                var map = flatCategories(this.channelCategoryList);

                _.map(this.isSelectCid, function (value, key) {
                    return {categoryId:key, selected:value};
                }).filter(function(item) {
                    return item.selected;
                }).forEach(function(item) {
                    var category = map[item.categoryId];

                    while(category) {
                        if (cIds.indexOf(category.catId) < 0) cIds.push(category.catId);
                        if (cNames.indexOf(category.catName) < 0) cNames.push(category.catName);

                        if (category.parent) {
                            if (fullCNames.indexOf(category.catPath) < 0) fullCNames.push(category.catPath);
                            if (fullCIds.indexOf(category.fullCatCId) < 0)  fullCIds.push(category.fullCatCId);
                        }

                        category = category.parent;
                    }
                });

                console.log(cIds);
                console.log(cNames);
                console.log(fullCNames);
                console.log(fullCIds);
            }
        };

        return PopAddChannelCategoryCtrl;
    })());
});