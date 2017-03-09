/**
 * @description 添加了自由标签的默认选中和新增的逻辑判断
 * @author Piao
 */

define([
    'cms'
], function (cms) {

    cms.controller('popFreeTagCtl', (function () {

        function popFreeTagCtl(context, channelTagService, $uibModalInstance, alert) {
            this.context = context;
            this.channelTagService = channelTagService;
            this.$uibModalInstance = $uibModalInstance;
            this.alert = alert;
            this.searchName = [];
            this.vm = {
                selectedNode: {},
                trees: null
            }
        }

        popFreeTagCtl.prototype.init = function () {
            var self = this, vm = self.vm,
                context = self.context,
                params = {
                    tagTypeSelectValue: context.tagTypeSel,
                    'cartId': context.cartId,
                    'orgFlg': context.orgFlg
                };

            if (context.orgFlg == 2) {
                params.productIds = context.productIds;
                params.isSelAll = context.selAllFlg;
            }

            self.channelTagService.init(params).then(function (res) {
                self.orgTagTree = res.data.tagTree;

                /**设置自由标签时，有初始勾选值*/
                if (context.orgFlg == 2) {
                    /**勾选状态*/
                    if (!res.data.orgChkStsMap) {
                        self.orgChkStsMap = {};
                        self._orgChkStsMap = {};
                    } else {
                        self.orgChkStsMap = res.data.orgChkStsMap;
                        self._orgChkStsMap = angular.copy(self.orgChkStsMap);
                    }

                    /**半选状态*/
                    if (!res.data.orgDispMap) {
                        self.orgDispMap = {};
                        self._orgDispMap = {};
                    } else {
                        self.orgDispMap = res.data.orgDispMap;
                        self._orgDispMap = angular.copy(self.orgDispMap);
                    }
                }

                vm.trees = [];
                vm.trees.push({level: 1, tags: res.data.tagTree});
            });

        };

        /**
         * 展开类目树
         * @param tag
         * @param treeIndex
         */
        popFreeTagCtl.prototype.openTag = function (tag, treeIndex) {
            var self = this, vm = self.vm,
                nextTags = vm.trees[treeIndex + 1];

            if (nextTags)
                vm.trees.splice(treeIndex + 1);

            if(!tag || tag.children.length === 0)
                return;

            vm.trees.push({tags: tag.children});

        };

        popFreeTagCtl.prototype.selOrgDisp = function (id, path, event) {
            var self = this;

            /**设置checkbox的选择状态*/
            self.orgChkStsMap[path] = self.vm.selectedNode[id];

            /**记录checkbox的半选状态*/
            if (self.orgDispMap[path]) {
                // 如果初始是半选状态
                if (self.selOrgDispList.indexOf(path) < 0) {
                    self.selOrgDispList.push(path);
                }
                self.orgDispMap[path] = false;
            }

            /**防止事件冒泡*/
            event.stopPropagation();
        };

        popFreeTagCtl.prototype.save = function () {
            var self = this,
                map = flatTrees(self.orgTagTree),
                selectdTagList = [],
                orgDispTagList = [];

            var selFlagArr = _.map(self.vm.selectedNode, function (value, key) {
                return {selectedId: key, selected: value};
            }).filter(function (item) {
                return item.selected;
            });

            /**当是高级检索，设置自由标签*/
            if (self.context.orgFlg == 2) {

                for (var key in self.orgDispMap) {
                    if (self.orgDispMap[key] == true) {
                        orgDispTagList.push(key);
                    }
                }
            }

            selFlagArr.forEach(function (item) {
                var selTagList = map[item.selectedId];
                selectdTagList.push({
                    "id": selTagList.id,
                    "tagPathName": selTagList.tagPathName,
                    "tagPath": selTagList.tagPath
                });
            });

            self.$uibModalInstance.close({
                "selectdTagList": selectdTagList,
                'orgFlg': self.context.orgFlg,
                'orgDispTagList': orgDispTagList
            });
        };


        return popFreeTagCtl;

    })());

    function flatTrees(categories, parent) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr.id] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatTrees(curr.children, curr));
            return map;
        }, {});
    }

});