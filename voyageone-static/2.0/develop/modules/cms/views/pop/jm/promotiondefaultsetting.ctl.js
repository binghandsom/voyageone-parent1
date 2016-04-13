/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    return cms.controller('popJmPromotionDefaultSettingCtl', (function () {

        function JmPromotionDefaultSettingController($translate, $CmsMtJmConfigService, notify, $uibModalInstance, alert) {

            this.translate = $translate;
            this.cmsMtJmConfigService = $CmsMtJmConfigService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
            this.alert = alert;

            this.imageConfig = {key: "JM_IMAGE_SETTING"};
            this.masterData = {};
        }

        JmPromotionDefaultSettingController.prototype = {

            // 初始化检索
            initialize: function () {
                var self = this;
                self.cmsMtJmConfigService.init().then(function (res){
                    self.masterData = res.data;
                    self.cmsMtJmConfigService.getByKey(self.imageConfig).then(function (res) {
                        self.imageConfig = res.data;
                        self.imageConfig.value = JSON.parse(self.imageConfig.value);
                    });
                })
            },

            // 保存
            ok: function () {
                var self = this;
                self.imageConfig.value = JSON.stringify(self.imageConfig.value);
                if (self.imageConfig.creater)
                    self.cmsMtJmConfigService.update(self.imageConfig).then(function () {
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        self.$uibModalInstance.dismiss('');
                    });
                else
                    self.cmsMtJmConfigService.insert(self.imageConfig).then(function () {
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        self.$uibModalInstance.dismiss('');
                    });
            }
        };

        return JmPromotionDefaultSettingController
    })());
});