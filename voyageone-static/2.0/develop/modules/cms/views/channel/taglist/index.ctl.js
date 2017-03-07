/**
 * @description 自由标签设置
 * @author Piao
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    cms.controller('tagListController', (function () {

        function TagListCtl(channelTagService, confirm) {
            this.channelTagService = channelTagService;
            this.confirm = confirm;
            this.vm = {
                tagTypeSelectValue: "1",
                tagTypeList: null,
                trees: null
            }
        }

        TagListCtl.prototype.init = function () {
            var self = this, vm = self.vm;

            self.channelTagService.init({tagTypeSelectValue: vm.tagTypeSelectValue}).then(function (res) {
                //获取选择下拉数据
                vm.tagTypeList = res.data.tagTypeList;

                vm.trees = [];
                vm.trees.push({level: 1, tags: res.data.tagTree});
            });
        };

        TagListCtl.prototype.openTag = function (tag,treeIndex) {
            var self = this, vm = self.vm,
                nextTags = vm.trees[treeIndex + 1];

            if (nextTags)
                vm.trees.splice(treeIndex + 1);

            if(!tag || tag.children.length === 0)
                return;

            vm.trees.push({tags: tag.children});
        };

        return TagListCtl;

    })());

});
