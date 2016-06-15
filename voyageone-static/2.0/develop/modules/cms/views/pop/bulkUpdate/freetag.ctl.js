/**
 * Created by sofia on 6/7/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('popFreeTagCtl', (function () {
        function popFreeTagCtl(channelTagService, confirm) {
            this.channelTagService = channelTagService;
            this.tagTypeSelectValue = "1";
            this.tagTree = null;
            this.id = "";
            this.parentTagId = "";
            this.tagTypeList = [];

            this.tree = [];
            this.key = [];
            this.selected = [];
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
                    self.vm.tagTypeList = res.data.tagTypeList;
                    self.search(0);
                });
            }
    }

        return popFreeTagCtl;
    })())
});