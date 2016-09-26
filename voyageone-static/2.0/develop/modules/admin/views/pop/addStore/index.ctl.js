/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddStoreController', (function () {
        function AddStoreController(context, channelService, storeService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.channelService = channelService;
            this.storeService = storeService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddStoreController.prototype = {
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
                self.sourceData.active = self.sourceData.active ? self.sourceData.active ? "1" : "0" : '';
                if (self.sourceData.isReadOnly == true) {
                    self.channelList = [self.sourceData.sourceData];
                    self.changeStore(self.sourceData.orderChannelId);
                } else {
                    self.channelService.getAllChannel().then(function (res) {
                        self.channelList = res.data;
                    });
                    self.changeStore(self.sourceData.orderChannelId);
                }
            },
            changeStore: function (value) {
                var self = this;
                if (self.sourceData.isReadOnly == true) {
                    self.storeAllList = self.sourceData.storeSourceData;
                } else {
                    self.storeService.getAllStore(value).then(function (res) {
                        self.storeAllList = res.data;
                    });
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                if (self.readOnly == true) {
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                if (self.append == true) {
                    if (self.sourceData.remainNum)
                        self.sourceData.inventoryHold = self.sourceData.inventoryHold + ',' + self.sourceData.remainNum;
                    self.storeService.addStore(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    if (self.sourceData.remainNum)
                        self.sourceData.inventoryHold = self.sourceData.inventoryHold + ',' + self.sourceData.remainNum;
                    self.storeService.updateStore(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddStoreController;
    })())
});