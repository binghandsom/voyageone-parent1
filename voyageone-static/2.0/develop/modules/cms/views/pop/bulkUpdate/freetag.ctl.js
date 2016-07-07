/**
 * Created by sofia on 6/7/2016.
 */
define([
    'cms'
], function (cms) {
    return cms.controller('popFreeTagCtl', (function () {
        function popFreeTagCtl(context, channelTagService, $uibModalInstance) {
            this.channelTagService = channelTagService;
            this.$uibModalInstance = $uibModalInstance;
            this.tagTypeSelectValue = context.tagTypeSel;
            this.cartId = context.cartId;
            this.tagTree = null;
            this.id = "";
            this.parentTagId = "";
            this.tagTypeList = [];

            this.tree = [];
            this.key = [];
            this.selected = [];
            this.selectdTagList = [];
            this.taglist = [];
        }

        popFreeTagCtl.prototype = {
            /**
             *初始化数据,上手调用search拼装tree
             */
            init: function () {
                var self = this;
                self.channelTagService.init({
                    tagTypeSelectValue: self.tagTypeSelectValue,
                    'cartId': self.cartId
                }).then(function (res) {
                    self.source = self.tagTree = res.data.tagTree;
                    self.tagTypeList = res.data.tagTypeList[3];
                    self.search(0);
                });
            },

            byTagChildrenName: function (arr, index) {
                var self = this;
                var key = self.key[index];
                return key ? arr.filter(function (item) {
                    return item.tagChildrenName.indexOf(self.key[index]) > -1;
                }) : arr;
            },

            /**
             * 当用户点击搜索时触发
             * @param index：记录层级
             */
            search: function (index) {
                var self = this;
                var tree = self.tree;
                var source = self.source;
                var selected = self.selected;
                var prev;
                for (; index < 3; index++) {
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
                    // if (index == 1 && selected[0].children.length == 0) selected[1] = undefined;
                    // if (index == 2 && selected[1].children.length == 0) selected[2] = undefined;
                }
            },

            collect: function (item) {
                var self = this;
                self.list = {
                    "id": item.id,
                    "tagPathName": item.tagPathName,
                    "tagPath": item.tagPath
                };
                if (!self.selectdTagList || self.selectdTagList.length == 0) self.selectdTagList.push(self.list);

                // 校验选择的是否有重复值
                var hasData = false;
                for (var j = 0; j < self.selectdTagList.length; j++) {
                    if (self.selectdTagList[j].id == self.list.id) {
                        hasData = true;
                        break;
                    }
                }
                if (!hasData) {
                    self.selectdTagList.push(self.list);
                }
            },

            /**
             * 点击保存
             */
            save: function () {
                var self = this;
                self.context = {"selectdTagList": self.selectdTagList};
                self.$uibModalInstance.close(self.context);
            }
        };

        return popFreeTagCtl;
    })())
});