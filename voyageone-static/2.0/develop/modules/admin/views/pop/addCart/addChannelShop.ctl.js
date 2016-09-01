/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddChannelShopController', (function () {
        function AddChannelShopController(context, channelService, popups, AdminCartService, cartShopService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
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
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.active = self.sourceData.active ?  self.sourceData.active ? "1" : "0":'';
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
            },
            changeCartList:function(){
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