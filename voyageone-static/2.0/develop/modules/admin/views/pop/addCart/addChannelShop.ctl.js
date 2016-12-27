/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelShopController', (function () {
        function AddChannelShopController(context, alert, channelService, popups, AdminCartService, cartShopService, $uibModalInstance) {
            this.sourceData = context ? angular.copy(context) : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.context = context;
            this.popups = popups;
            this.alert = alert;
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
                self.sourceData.active = self.sourceData.active!=null ? self.sourceData.active ? "1" : "0" : '';
                if (self.sourceData.isReadOnly == true) {
                    self.channelAllList = [self.sourceData.sourceData];
                } else {
                    self.channelService.getAllChannel(null).then(function (res) {
                        self.channelAllList = res.data;
                    });
                }
                if (self.sourceData.isReadOnly == true) {
                    self.AdminCartService.getCartByIds({'cartIds': self.sourceData.sourceData.cartIds}).then(function (res) {
                        self.cartAllList = res.data;
                    });
                } else {
                    self.AdminCartService.getAllCart(null).then(function (res) {
                        self.cartAllList = res.data;
                    });
                }
            },
            changeCartList: function () {
                var self = this;
                self.AdminCartService.getAllCart(self.sourceData.orderChannelId).then(function (res) {
                    self.cartAllList = res.data;
                    if (self.cartAllList.length == 0) {
                        self.alert('请前往【 渠道信息管理 】页，选取 渠道Cart 信息！');
                    }
                });

            },
            cancel: function () {
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                _.extend(self.context, self.sourceData);
                if (self.readOnly == true) {
                    self.data = _.find(self.cartAllList, function (cart) {
                        return cart.cartId == self.sourceData.cartId;
                    });
                    _.extend(self.sourceData, {'cartName': self.data.name});
                    self.$uibModalInstance.close(self.context);
                    return;
                }
                if (self.append == true) {
                    self.cartShopService.addCartShop(self.sourceData).then(function (res) {
                        if(res.data == false){
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.cartShopService.updateCartShop(self.sourceData).then(function (res) {
                        if(res.data == false){
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.context});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddChannelShopController;
    })())
});