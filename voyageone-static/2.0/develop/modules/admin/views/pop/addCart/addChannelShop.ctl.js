/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelShopController', (function () {
        function AddChannelShopController(context, channelService, popups, AdminCartService, cartShopService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.popups = popups;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.cartShopService = cartShopService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance

        }

        AddChannelShopController.prototype = {
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
                    self.channelAllList = [self.sourceData.sourceData];
                } else {
                    self.channelService.getAllChannel(null).then(function (res) {
                        self.channelAllList = res.data;
                    });
                }
                if (self.sourceData.isReadOnly == true) {
                    self.AdminCartService.getAllCart(self.sourceData.orderChannelId).then(function (res) {
                        self.cartAllList = res.data;
                    });
                }
            },
            changeCartList: function () {
                var self = this;
                self.AdminCartService.getAllCart(self.sourceData.orderChannelId).then(function (res) {
                    self.cartAllList = res.data;
                });

            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                if (self.readOnly == true) {
                    self.data = _.find(self.cartAllList, function (cart) {
                        return cart.cartId == self.sourceData.cartId;
                    });
                    _.extend(self.sourceData, {'cartName': self.data.name});
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                if (self.append == true) {
                    self.cartShopService.addCartShop(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.cartShopService.updateCartShop(self.sourceData).then(function (res) {
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelShopController;
    })())
});