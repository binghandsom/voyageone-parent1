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

                //设置自由标签时，有初始勾选值
                if (context.orgFlg == 2) {
                    //checkbox勾选状态
                    if (!res.data.orgChkStsMap) {
                        self.orgChkStsMap = {};
                        self._orgChkStsMap = {};
                    } else {
                        self.orgChkStsMap = res.data.orgChkStsMap;
                        self._orgChkStsMap = angular.copy(self.orgChkStsMap);
                    }

                    //checkbox半选状态
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

        popFreeTagCtl.prototype.openTag = function (tag, treeIndex) {
            var self = this, vm = self.vm,
                nextTags = vm.trees[treeIndex + 1];

            if (nextTags)
                vm.trees.splice(treeIndex + 1);

            vm.trees.push({tags: tag.children});

        };

        popFreeTagCtl.prototype.save = function () {
            var self = this,
                map = flatTrees(self.source),
                selectdTagList = [],
                orgDispTagList = [];


            var selFlagArr = _.map(self.taglist.selFlag, function (value, key) {
                return {selectedId: key, selected: value};
            }).filter(function (item) {
                return item.selected;
            });

            /**当是高级检索，查询自由标签时，不检查勾选*/
            if (self.orgFlg == 2) {

                for (var key in self.orgDispMap) {
                    if (self.orgDispMap[key] == true) {
                        orgDispTagList.push(key);
                    }
                }
            }

            selFlagArr.forEach(function (item) {
                var selTagList = map[item.selectedId];
                var self = this;
                self.list = {
                    "id": selTagList.id,
                    "tagPathName": selTagList.tagPathName,
                    "tagPath": selTagList.tagPath
                };
                selectdTagList.push(self.list);
            });

            self.context = {"selectdTagList": selectdTagList, 'orgFlg': self.orgFlg, 'orgDispTagList': orgDispTagList};
            self.$uibModalInstance.close(self.context);
        };


        return popFreeTagCtl;

    })());

});