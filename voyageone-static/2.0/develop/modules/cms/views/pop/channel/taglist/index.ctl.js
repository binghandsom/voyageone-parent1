/**
 * @description 编辑自由标签
 * @author rex.wu
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/noticeTip.directive'
], function (cms) {

    cms.controller('EditTagController', (function () {

        function EditTagController(channelTagService, confirm, context, notify, alert, $modalInstance) {
            this.channelTagService = channelTagService;
            this.confirm = confirm;
            this.context = context;
            this.notify = notify;
            this.alert = alert;
            this.$modalInstance = $modalInstance;

            this.tag = context.tag;
            this.tagName = "";
            this.parentTag = {};
            this.init();
        }

        /**
         * 初始化    构建tag树形结构
         * @param parentIndex
         */
        EditTagController.prototype.init = function () {
            var self = this;
            if (self.tag && self.tag.parentTagId) {
                self.channelTagService.getTag({tagId:self.tag.parentTagId}).then(res => {
                    if (res.data) {
                        self.parentTag = res.data;
                    }
                });
            }
        };

        /**
         * 修改tagPathName
         */
        EditTagController.prototype.editTag = function () {
            var self = this;
            if (self.tagName) {
                let parameter = {
                    tagId:self.tag.id,
                    tagName:self.tagName
                };
                self.channelTagService.editTagName(parameter).then(res => {
                    self.$modalInstance.close({success:!!res.data});
                });
            }
        };

        return EditTagController;

    })());

});
