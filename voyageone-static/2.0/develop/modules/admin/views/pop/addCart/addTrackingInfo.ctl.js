/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CartAddTrackingInfoController', (function () {
        function CartAddTrackingInfoController(context, channelService, AdminCartService, cartTrackingService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' || context.kind == 'add' ? true : false;
            this.readOnly = context.isReadOnly == true ? true : false;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.cartTrackingService = cartTrackingService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
            this.checked = false;
        }

        CartAddTrackingInfoController.prototype = {
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
                self.sourceData.trackingSpreadFlg ? self.sourceData.trackingSpreadFlg == '1' ? self.checked = true : self.checked = false : '';
                self.checked == true ? self.sourceData.trackingSpreadFlg = true : self.sourceData.trackingSpreadFlg = false;
                self.sourceData.active = self.sourceData.active ? self.sourceData.active ? "1" : "0" : '';
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.AdminCartService.getAllCart(null).then(function (res) {
                    self.cartAllList = res.data;
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                self.sourceData.trackingSpreadFlg = self.sourceData.trackingSpreadFlg == true ? self.sourceData.trackingSpreadFlg = '1' : '';
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                self.sourceData.trackingSpreadFlg = self.sourceData.trackingSpreadFlg == true ? '1' : '';
                if (self.readOnly == true) {
                    self.data = _.find(self.cartAllList, function (cart) {
                        return cart.cartId == self.sourceData.cartId;
                    });
                    _.extend(self.sourceData,{'cartName':self.data.name});
                    self.$uibModalInstance.close(self.sourceData);
                    return;
                }
                if (self.append == true) {
                    self.cartTrackingService.addCartTracking(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.cartTrackingService.updateCartTracking(self.sourceData).then(function (res) {
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