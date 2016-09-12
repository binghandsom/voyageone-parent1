/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelThirdController', (function () {
        function AddChannelThirdController(context, channelService, thirdPartyConfigService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.channelService = channelService;
            this.thirdPartyConfigService = thirdPartyConfigService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance

        }

        AddChannelThirdController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add' || self.sourceData.kind == 'add') {
                    self.popType = '添加';
                    if (self.sourceData.isReadOnly !== true) {
                        self.sourceData = {};
                    } else {
                        self.sourceData = self.sourceData;
                    }
                }
                self.sourceData.active = self.sourceData.active ? self.sourceData.active ? "0" : "1" : '';
                if (self.sourceData.isReadOnly == true) {
                    self.channelAllList = [self.sourceData.sourceData];
                } else {
                    self.channelService.getAllChannel().then(function (res) {
                        self.channelAllList = res.data;
                    });
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                if (self.readOnly == true) {
                    self.sourceData.active = self.sourceData.active == '0' ? true : false;
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                if (self.append == true) {
                    self.thirdPartyConfigService.addThirdPartyConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.thirdPartyConfigService.updateThirdPartyConfig(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelThirdController;
    })())
});