/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CreateEditController', (function () {
        function CreateEditController(context, $uibModalInstance, channelService, storeService, taskService, cartShopService, portConfigService) {
            this.sourceData = context;
            this.$uibModalInstance = $uibModalInstance;
            this.channelService = channelService;
            this.storeService = storeService;
            this.taskService = taskService;
            this.cartShopService = cartShopService;
            this.portConfigService = portConfigService;
            this.append = context.type == 'add' ? true : false;
            this.popType = '修改';
            this.configType = this.sourceData.configType;
        }

        CreateEditController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData.type == 'add') self.popType = '添加';
                switch (self.configType) {
                    case 'Channel':
                        return self.type = "渠道";
                        break;
                    case 'Store':
                        return self.type = "仓库";
                        break;
                    case 'Task':
                        return self.type = "任务";
                        break;
                    case 'Shop':
                        return self.type = "Cart";
                        break;
                    case 'Port':
                        return self.type = "港口";
                        break;
                }
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
                switch (self.configType) {
                    case 'Channel':
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
                        break;
                    case 'Store':
                        if (self.append == true) {
                            self.storeService.addStoreConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        } else {
                            self.storeService.updateStoreConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        }
                        break;
                    case 'Task':
                        self.taskService.addTaskConfig(self.sourceData).then(function (res) {
                            _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                            self.$uibModalInstance.close(result);
                        });
                        break;
                    case 'Shop':
                        if (self.append == true) {
                            self.cartShopService.addCartShopConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        } else {
                            self.cartShopService.updateCartShopConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        }
                        break;
                    case 'Port':
                        if (self.append == true) {
                            self.portConfigService.addPortConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        } else {
                            self.portConfigService.updatePortConfig(self.sourceData).then(function (res) {
                                _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                                self.$uibModalInstance.close(result);
                            })
                        }
                        break;
                }
            }
        };
        return CreateEditController;
    })())
});