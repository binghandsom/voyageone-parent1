/**
 * @description 自由标签设置
 * @author Piao
 * @author rex.wu 2017-07-31 美国自由标签
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/noticeTip.directive'
], function (cms) {

    cms.controller('tagListController', (function () {

        function TagListCtl(channelTagService, confirm, popups, notify, alert, $location, usTagService) {
            this.channelTagService = channelTagService;
            this.confirm = confirm;
            this.popups = popups;
            this.notify = notify;
            this.alert = alert;
            this.$location = $location;
            this.usTagService = usTagService;

            this.usaFlag = false; // 是否是美国CMS->标签管理
            let url = $location.$$url;
            if (url.indexOf("usa") != -1) {
                this.usaFlag = true;
            }

            this.selected = [];
            this.searchName = [];
            this.vm = {
                tagTypeSelectValue: "0",
                usTagTypeSelectValue: "6",
                tagTypeList: null,
                trees: null
            }
        }

        /**
         * 初始化    构建tag树形结构
         * @param parentIndex
         */
        TagListCtl.prototype.init = function (parentIndex) {
            var self = this, vm = self.vm;

            if (self.usaFlag) {
                self.usTagService.init({tagTypeSelectValue: vm.usTagTypeSelectValue}).then(res => {
                    //获取选择下拉数据
                    vm.tagTypeList = res.data.tagTypeList;
                    //保存原来的树
                    vm.orgTagTree = res.data.tagTree;

                    self.constructOrg(res.data.tagTree, parentIndex);
                });
            } else {
                self.channelTagService.init({tagTypeSelectValue: vm.tagTypeSelectValue}).then(function (res) {
                    //获取选择下拉数据
                    vm.tagTypeList = res.data.tagTypeList;
                    //保存原来的树
                    vm.orgTagTree = res.data.tagTree;

                    self.constructOrg(res.data.tagTree, parentIndex);
                });
            }
        };

        /**
         * 展开树状结构
         * @param tag
         * @param treeIndex
         */
        TagListCtl.prototype.openTag = function (tag, treeIndex) {
            var self = this, vm = self.vm,
                nextTags = vm.trees[treeIndex + 1];

            //设置各级选中节点
            if (treeIndex == 0)
                self.selected[0] = tag;
            else
                self.selected[treeIndex] = tag;

            if (nextTags)
                vm.trees.splice(treeIndex + 1);

            vm.trees.push({tags: tag.children});

        };

        TagListCtl.prototype.editTag = function (tag, parentIndex,$event) {
            var self = this;
            self.popups.editTag({tag:tag}).then(res => {
                self.notify.success("TagName modified successfully.");
                self.init(parentIndex);
            });

            $event.stopPropagation();
        };

        /**
         * 新增标签popup
         * @param parentIndex 层级序号
         */
        TagListCtl.prototype.popNewTag = function (parentIndex) {
            var self = this, vm = self.vm,
                first, tagInfo;

            first = parentIndex === 0;
            tagInfo = {
                tagTypeSelectValue: vm.tagTypeSelectValue,
                tagTree: vm.orgTagTree
            };

            self.popups.openNewTag({
                first: first,
                tagInfo: tagInfo,
                tagSelectObject: self.selected[parentIndex === 0 ? 0 : parentIndex - 1]
            }).then(function () {
                self.notify.success('Tag added successfully.');
                self.init(parentIndex);
            });
        };

        /**
         * 删除标签
         */
        TagListCtl.prototype.delTag = function (tag, parentIndex, $event) {
            var self = this,
                channelTagService = self.channelTagService;

            self.confirm('TXT_CONFIRM_DELETE_TAG').then(function () {
                channelTagService.del({
                    id: tag.id,
                    parentTagId: tag.parentTagId,
                    tagTree: self.vm.orgTagTree
                }).then(function () {
                    self.init(parentIndex);
                });
            });

            $event.stopPropagation();
        };

        TagListCtl.prototype.filterByName = function (parentIndex, tags) {
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

        /**
         * 用于重新构建tag树
         * @param tagTree
         * @param parentIndex
         */
        TagListCtl.prototype.constructOrg = function (tagTree, parentIndex) {
            var self = this,
                vm = self.vm;

            vm.trees = [];
            if (!parentIndex) {
                vm.trees.push({level: 1, tags: tagTree});
            } else {
                vm.trees.push({level: 1, tags: tagTree});
                for (var i = 0; i < parentIndex; i++) {
                    _.each(vm.trees[i].tags, function (item) {
                        if (item.tagChildrenName == self.selected[i].tagChildrenName)
                            vm.trees.push({tags: item.children});
                    });
                }
            }
        };

        return TagListCtl;

    })());

});
