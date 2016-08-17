/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context, $uibModalInstance, channelService) {
            this.sourceData = context;
            this.$uibModalInstance = $uibModalInstance;
            this.channelService = channelService;
            this.append = context.type == 'add' ? true : false;
            this.popType = '修改';
            this.configType = this.sourceData.configType;
        }

        CreateEditController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData.type == 'add') self.popType = '添加';
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                _.extend(self.sourceData, {
                    'configType': self.configType,
                    'orderChannelId': self.sourceData.orderChannelId
                });
                var result = {};
                if (self.append == true) {
                    self.channelService.addChannelConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.channelService.updateChannelConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return CreateEditController;
    })())
});