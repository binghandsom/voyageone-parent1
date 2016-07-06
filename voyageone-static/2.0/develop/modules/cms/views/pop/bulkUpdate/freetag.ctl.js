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
            this.count = 0;
            this.selectdTagList = [];
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
                    if(index==1)selected[1] = undefined;
                    if(index==2&&selected[2]!==undefined)selected[2] = undefined;
                }
                if (selected[0] == undefined || !selected[0].children.length) selected[1] = undefined;
                if (selected[1] == undefined || !selected[1].children.length) selected[2] = undefined;
                for (var i = 2; i >= 0; i--) {
                    var selectedVal = self.selected[i];
                    if (selectedVal !== undefined) {
                        self.tagPathName = selectedVal.tagPathName;
                        self.list = {
                            "id": selectedVal.id,
                            "tagPathName": selectedVal.tagPathName,
                            "tagPath": selectedVal.tagPath
                        };
                        break;
                    }
                }
            },

            /**
             * 点击确认添加的搜索条件
             */
            confirm: function () {
                var self = this;
                if (!self.selectdTagList || self.selectdTagList.length == 0) self.selectdTagList.push(self.list);

                // 校验选择的是否有重复值
                var hasData = false;
                for (var i = 0; i < self.selectdTagList.length; i++) {
                    if (self.selectdTagList[i].id == self.list.id) {
                        hasData = true;
                        break;
                    }
                }
                if (!hasData) {
                    self.selectdTagList.push(self.list);
                }
            },
            clear: function () {
                var self = this;
                self.count = self.count - 1;
                self.selectdTagList.pop();
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