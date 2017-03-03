/**
 * @description 添加翻译属性值模态框
 * @author Piao
 */

define([
    'cms'
], function (cms) {

    cms.controller('popAddValueCtl', (function () {

        function AddValueCtl(context, notify, $modalInstance, attrTranslateService) {
            this.context = context;
            this.notify = notify;
            this.$modalInstance = $modalInstance;
            this.attrTranslateService = attrTranslateService;
            this.saveEntiy = {
                type: context.type + '',
                name: context.nameEn,
                valueEn: '',
                valueCn: ''
            }
        }

        AddValueCtl.prototype.save = function () {
            var self = this;

            self.attrTranslateService.create(self.saveEntiy).then(function () {
                self.notify.success('添加成功！');
                self.$modalInstance.close();
            });
        };

        return AddValueCtl;

    })());

});
