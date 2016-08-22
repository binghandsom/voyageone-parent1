/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CartAddTrackingInfoController', (function () {
        function CartAddTrackingInfoController(context, channelService, AdminCartService, cartShopService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.cartShopService = cartShopService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        CartAddTrackingInfoController.prototype = {
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
                self.AdminCartService.getAllCart().then(function (res) {
                    self.cartAllList = res.data;
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
                    self.cartShopService.addCartShop(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.cartShopService.updateCartShop(self.sourceData).then(function (res) {
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
        return CartAddTrackingInfoController;
    })())
});