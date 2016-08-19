/**
 * Created by sofia on 2016/8/18.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelTypeController', (function () {
        function AddChannelTypeController(context, channelService, typeService, AdminCartService, channelAttributeService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.channelService = channelService;
            this.typeService = typeService;
            this.AdminCartService = AdminCartService;
            this.channelAttributeService = channelAttributeService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance

        }

        AddChannelTypeController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active ? "0" : "1";
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.typeService.getAllType().then(function (res) {
                    self.typeList = res.data;
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '0' ? true : false;
                if (self.append == true) {
                    self.channelAttributeService.addChannelAttribute(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.channelAttributeService.updateChannelAttribute(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelTypeController;
    })())
});