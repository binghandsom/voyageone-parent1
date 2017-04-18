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
            this.orgDispMap = {};           // 半选状态
            this.orgChkStsMap = {};         // 全选状态
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
            params.searchInfo = context.searchInfo;

            self.channelTagService.init(params).then(function (res) {
                self.orgTagTree = res.data.tagTree;

                /**设置自由标签时，有初始勾选值*/
                if (context.orgFlg == 2) {
                    /**勾选状态*/
                    self.orgChkStsMap = res.data.orgChkStsMap;
                    /**半选状态*/
                    self.orgDispMap = res.data.orgDispMap;

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

            if (!tag || tag.children.length === 0)
                return;

            vm.trees.push({tags: tag.children});

        };

        popFreeTagCtl.prototype.selOrgDisp = function (id, path, event) {
            var self = this;

            /**设置checkbox的选择状态*/
            self.orgChkStsMap[path] = self.vm.selectedNode[id];

            /**记录checkbox的半选状态*/
            if (self.orgDispMap[path]) {
                self.orgDispMap[path] = false;
            }

            /**防止事件冒泡*/
            event.stopPropagation();
        };

        popFreeTagCtl.prototype.filterByName = function (parentIndex, tags) {
            var self = this,
                searchName = self.searchName[parentIndex];

            if (!searchName) {
                self.openTag(tags[0], parentIndex);
                return;
            }

            var result = _.filter(tags, function (item) {
                return item.tagChildrenName.indexOf(searchName) >= 0;
            })[0];

            if (result)
                self.openTag(result, parentIndex);
        };

        popFreeTagCtl.prototype.save = function () {
            var self = this,
                mapById = flatTrees(self.orgTagTree, null, 'id'),
                mapByTagPath = flatTrees(self.orgTagTree, null, 'tagPath'),
                selectdTagList = [],
                orgDispTagList = [];

            /**当是高级检索，设置自由标签*/
            if (self.context.orgFlg == 2) {
                for (var key in self.orgDispMap) {
                    if (self.orgDispMap[key] == true) {
                        orgDispTagList.push(key);
                    }
                }
            }

            /**遍历选中的*/
            _.map(self.vm.selectedNode, function (value, key) {
                if (value) {
                    var selTagList = mapById[key];
                    selectdTagList.push({
                        "id": selTagList.id,
                        "tagPathName": selTagList.tagPathName,
                        "tagPath": selTagList.tagPath
                    });
                }
            });

            /**合并默认选中的节点 , 排除重复项*/
            _.map(self.orgChkStsMap, function (value, key) {
                if (value) {
                    var _obj = mapByTagPath[key],
                        flag = _.some(selectdTagList, function (item) {
                            return item.id == _obj.id;
                        });

                    if (!flag) {
                        selectdTagList.push({
                            "id": _obj.id,
                            "tagPathName": _obj.tagPathName,
                            "tagPath": _obj.tagPath
                        });
                    }
                }
            });

            self.$uibModalInstance.close({
                "selectdTagList": selectdTagList,
                'orgFlg': self.context.orgFlg,
                'orgDispTagList': orgDispTagList
            });

        };

        return popFreeTagCtl;

    })());

    function flatTrees(categories, parent, attrName) {
        return categories.reduce(function (map, curr) {
            curr.parent = parent;
            map[curr[attrName]] = curr;
            if (curr.children && curr.children.length)
                map = angular.extend(map, flatTrees(curr.children, curr, attrName));
            return map;
        }, {});
    }

});