/**
 * Created by sofia on 2016/8/18.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelTypeController', (function () {
        function AddChannelTypeController(context, channelService, typeService, AdminCartService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.channelService = channelService;
            this.typeService = typeService;
            this.AdminCartService = AdminCartService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddChannelTypeController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.typeService.getAllType().then(function (res) {
                    self.typeList = res.data;
                });
                self.channelService.getAllCompany().then(function (res) {
                    self.companyAllList = res.data;
                });
                self.AdminCartService.getAllCart().then(function (res) {
                    self.cartAllList = res.data
                });
                if (!self.sourceData.cartIds) return;
                self.AdminCartService.getCartByIds({'cartIds': self.sourceData.cartIds}).then(function (res) {
                    self.cartList = res.data;
                })
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                // 设置cartIds
                var tempCartList = [];
                var result = {};
                _.forEach(self.cartList, function (item) {
                    tempCartList.push(item.cartId);
                    _.extend(self.sourceData, {'cartIds': tempCartList.join(','), 'companyId': self.companyId});
                });
                if (self.append == true) {
                    self.channelService.addChannel(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.channelService.updateChannel(self.sourceData).then(function (res) {
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
        return AddChannelTypeController;
    })())
});