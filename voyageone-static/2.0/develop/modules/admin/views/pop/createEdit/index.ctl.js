/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context, $uibModalInstance, AdminChannelService) {
            this.sourceData = context;
            this.$uibModalInstance = $uibModalInstance;
            this.AdminChannelService = AdminChannelService;
            this.append = context.type == 'add' ? true : false;
            this.popType = '修改';
            this.configType = 'Channel';
        }

        CreateEditController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData.type == 'add') self.popType = '添加';
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            },
            save: function () {
                var self = this;
                _.extend(self.sourceData, {'configType': self.configType, 'channelId': self.sourceData.channelId});
                if (self.append == true) {
                    self.AdminChannelService.addConfig(self.sourceData).then(function (res) {
                        if (res.data.success == false)self.confirm(res.data.message);
                        self.$uibModalInstance.close('success');
                    })
                } else {
                    self.AdminChannelService.updateConfig(self.sourceData).then(function (res) {
                        if (res.data.success == false)self.confirm(res.data.message);
                        self.$uibModalInstance.close('success');
                    })
                }
            }
        };
        return CreateEditController;
    })())
});