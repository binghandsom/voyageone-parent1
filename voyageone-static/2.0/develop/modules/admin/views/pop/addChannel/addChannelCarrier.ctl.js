/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelCarrierController', (function () {
        function AddChannelCarrierController(context, channelService, popups, carrierConfigService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.context = context;
            this.popups = popups;
            this.channelService = channelService;
            this.carrierConfigService = carrierConfigService;
            this.popType = '编辑';
            this.$uibModalInstance = $uibModalInstance
        }

        AddChannelCarrierController.prototype = {
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
                self.sourceData.active = self.sourceData.active != null ? self.sourceData.active ? "0" : "1" : '0';
                if (self.sourceData.isReadOnly == true) {
                    self.channelAllList = [self.sourceData.sourceData];
                } else {
                    self.channelService.getAllChannel().then(function (res) {
                        self.channelAllList = res.data;
                    });
                }
                self.carrierConfigService.getAllCarrier().then(function (res) {
                    self.carrierList = res.data;
                });

            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
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
                _.extend(self.context, self.sourceData);
                if (self.append == true) {
                    self.carrierConfigService.addCarrierConfig(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.carrierConfigService.updateCarrierConfig(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelCarrierController;
    })())
});