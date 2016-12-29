/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelThirdController', (function () {
        function AddChannelThirdController(context, channelService, thirdPartyConfigService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.context = context;
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
                self.sourceData.active = self.sourceData.active!=null ? self.sourceData.active ? "1" : "0" : '';
                if (self.sourceData.isReadOnly == true) {
                    self.channelAllList = [self.sourceData.sourceData];
                } else {
                    self.channelService.getAllChannel().then(function (res) {
                        self.channelAllList = res.data;
                    });
                }
            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                if (self.readOnly == true) {
                    self.sourceData.active = self.sourceData.active == '1' ? true : false;
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.thirdPartyConfigService.addThirdPartyConfig(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.thirdPartyConfigService.updateThirdPartyConfig(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelThirdController;
    })())
});