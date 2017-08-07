/**
 * @description 编辑自由标签
 * @author rex.wu
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/noticeTip.directive'
], function (cms) {

    cms.controller('EditTagController',class EditTagController{

       constructor(channelTagService, confirm, context, notify, alert, $uibModalInstance){
           this.channelTagService = channelTagService;
           this.confirm = confirm;
           this.context = context;
           this.notify = notify;
           this.alert = alert;
           this.$uibModalInstance = $uibModalInstance;

           this.tag = context.tag;
           this.tagName = "";
           this.parentTag = {};
           this.init();
       }

        /**
         * 初始化    构建tag树形结构
         */
        init() {
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
        editTag() {
            let self = this;

            if (self.tagName) {
                let parameter = {
                    tagId:self.tag.id,
                    tagName:self.tagName
                };
                self.channelTagService.editTagName(parameter).then(res => {
                    self.$uibModalInstance.close({success:!!res.data});
                });
            }
        };

    });


});
