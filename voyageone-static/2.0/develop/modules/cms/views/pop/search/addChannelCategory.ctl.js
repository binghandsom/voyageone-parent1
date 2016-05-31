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

        function PopAddChannelCategoryCtrl(context, $rootScope, $addChannelCategoryService, notify, $uibModalInstance) {
            this.code = context.productIds;
            this.cartList = [];
            this.channelCategoryList = null;
            this.isSelectCid = [];
            this.cartId = $rootScope.platformType.cartId.toString();
            this.addChannelCategoryService = $addChannelCategoryService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.checkedCountValid = false;
            this.cartIdValid = false;
        }

        PopAddChannelCategoryCtrl.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {
                var self = this;
                self.addChannelCategoryService.init({"code": self.code, "cartId": self.cartId}).then(function (res) {
                    self.cartList = res.data.cartList;
                    self.cartIdValid = false;
                    if (self.cartId == "0" || self.cartId == "1") {
                        self.isSelectCid = [];
                        self.channelCategoryList = null;
                    } else {
                        self.isSelectCid = res.data.isSelectCid;
                        self.channelCategoryList = res.data.channelCategoryList;
                    }
                });
            },

            /**
             * 点击保存按钮时
             */
            save: function () {
                var self = this;
                if (self.cartId == "0" || self.cartId == "1") {
                    self.cartIdValid = true;
                } else {
                    var cIds = [], cNames = [], fullCNames = [], fullCIds = [];
                    var map = flatCategories(this.channelCategoryList);
                    _.map(this.isSelectCid, function (value, key) {
                        return {categoryId: key, selected: value};
                    }).filter(function (item) {
                        return item.selected;
                    }).forEach(function (item) {
                        var category = map[item.categoryId];
                        if (category && category.isParent == 0) {
                            if (fullCNames.indexOf(category.catPath) < 0) fullCNames.push(category.catPath);
                            if (fullCIds.indexOf(category.fullCatId) < 0)  fullCIds.push(category.fullCatId);
                        }
                        while (category) {
                            if (cIds.indexOf(category.catId) < 0) cIds.push(category.catId);
                            if (cNames.indexOf(category.catName) < 0) cNames.push(category.catName);
                            category = category.parent;
                        }
                    });
                    if (fullCIds.length > 10) {
                        self.checkedCountValid = true;
                    } else {
                        self.addChannelCategoryService.save({
                            "cIds": cIds,
                            "cNames": cNames,
                            "fullCNames": fullCNames,
                            "fullCatId": fullCIds,
                            "code": self.code,
                            "cartId": self.cartId
                        }).then(function (res) {
                            self.notify.success('TXT_MSG_UPDATE_SUCCESS');
                            self.$uibModalInstance.close();
                        });
                    }
                }
            }
        };
        return PopAddChannelCategoryCtrl;
    })());
});