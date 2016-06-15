/**
 * Created by sofia on 6/7/2016.
 */
define([
    'cms'
], function (cms) {
    return cms.controller('popFreeTagCtl', (function () {
        function popFreeTagCtl(channelTagService, confirm) {
            this.channelTagService = channelTagService;
            this.tagTypeSelectValue = "4";
            this.tagTree = null;
            this.id = "";
            this.parentTagId = "";
            this.tagTypeList = [];

            this.tree = [];
            this.key = [];
            this.selected = [];
            this.newIndex = {value: -1};
        }

        popFreeTagCtl.prototype = {
            /**
             *初始化数据,上手调用search拼装tree
             */
            init: function () {
                var self = this;
                //默认选中店铺类分类
                self.channelTagService.init({tagTypeSelectValue: self.tagTypeSelectValue}).then(function (res) {
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

                    if (!selected[index]) {
                        selected[index] = tree[index][0];
                    } else if (_.isString(selected[index])) {
                        selected[index] = tree[index].find(function (item) {
                            return item.tagChildrenName === selected[index];
                        });
                    } else if (tree[index].indexOf(selected[index]) < 0) {
                        var indexSelected = tree[index].find(function (item) {
                            return item.id === selected[index].id;
                        });
                        if (indexSelected)
                            selected[index] = indexSelected;
                        else
                            selected[index] = tree[index][0];
                    }
                }
            },
            /**
             * 新增tag操作
             * @param savedata
             */
            save: function (savedata) {
                var self = this;
                //记录新增记录名称
                self.selected[$scope.newIndex.value] = savedata.vm.tagPathName;

                self.channelTagService.save(savedata.vm).then(function (res) {
                        self.source = self.tagTree = res.data.tagInfo.tagTree;
                        self.search(0);
                        savedata.$close();
                    },
                    function (err) {
                        if (err.message != null) {
                            savedata.vm.errMsg = err.message;
                        }
                        self.search(0);
                    })
            }
        };

        return popFreeTagCtl;
    })())
});