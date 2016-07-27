/**
 * Created by sofia on 6/7/2016.
 */
define([
    'cms'
], function (cms) {

    function flatTrees(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.id] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatTrees(curr.children, curr));
            return map;
        }, {});
    }

    cms.controller('popFreeTagCtl', (function () {
        function popFreeTagCtl(context, channelTagService, $uibModalInstance) {
            this.channelTagService = channelTagService;
            this.$uibModalInstance = $uibModalInstance;
            this.tagTypeSelectValue = context.tagTypeSel;
            this.cartId = context.cartId;
            this.orgFlg = context.orgFlg; // orgFlg==1:表示从高级检索的检索条件而来；其它场合不设值
            this.tagTree = null;
            this.id = "";
            this.parentTagId = "";
            this.tagTypeList = [];

            this.tree = [];
            this.key = [];
            this.selected = [];
            this.taglist = {selList: []};
        }

        popFreeTagCtl.prototype = {
            /**
             *初始化数据,上手调用search拼装tree
             */
            init: function () {
                var self = this;
                self.channelTagService.init({
                    tagTypeSelectValue: self.tagTypeSelectValue,
                    'cartId': self.cartId,
                    'orgFlg': self.orgFlg
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
                        }
                    }
                    break;
                }
                if (index == 1) tree[2] = [];
            },

            /**
             * 点击保存
             */
            save: function () {
                var self = this;
                var map = flatTrees(self.source);
                var selectdTagList = [];
                _.map(self.taglist.selFlag, function (value, key) {
                    return {selectedIds: key, selected: value};
                }).filter(function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    var selTagList = map[item.selectedIds];
                    var self = this;
                    self.list = {
                        "id": selTagList.id,
                        "tagPathName": selTagList.tagPathName,
                        "tagPath": selTagList.tagPath
                    };
                    selectdTagList.push(self.list);
                });
                self.context = {"selectdTagList": selectdTagList};
                self.$uibModalInstance.close(self.context);
            }
        };

        return popFreeTagCtl;
    })())
});