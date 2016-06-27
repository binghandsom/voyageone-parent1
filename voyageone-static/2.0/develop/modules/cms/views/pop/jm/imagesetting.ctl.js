/**
 * Created by 123 on 2016/4/8.
 */
define([
    'cms'
], function (cms) {

    return cms.controller('popImageSettingCtl', (function () {

        function JmImageSettingController($translate, $CmsMtMasterInfoService, $modalInstance, notify, confirm, alert, context) {

            this.translate = $translate;
            this.cmsMtMasterInfoService = $CmsMtMasterInfoService;
            this.modalInstance = $modalInstance;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;

            this.imageData = context.imageData;
        }

        JmImageSettingController.prototype = {

            // 检索
            save: function () {
                var self = this;
                if (self.imageData.dataType != null && self.imageData.dataType != "" &&
                    self.imageData.value1 != null && self.imageData.value1 != "") {
                    self.cmsMtMasterInfoService.addImage(this.imageData).then(function () {
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        self.modalInstance.close();
                    })
                }
            }
        };

        return JmImageSettingController
    })());
});