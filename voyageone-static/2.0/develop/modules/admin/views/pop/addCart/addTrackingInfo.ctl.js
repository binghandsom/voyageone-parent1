/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin'
], function (admin) {
    admin.controller('CartAddTrackingInfoController', (function () {
        function CartAddTrackingInfoController(context, channelService, AdminCartService, cartTrackingService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
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
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
                self.sourceData.trackingSpreadFlg ? self.sourceData.trackingSpreadFlg == '1' ? self.checked = true : self.checked = false : '';
                self.checked == true ? self.sourceData.trackingSpreadFlg = true : self.sourceData.trackingSpreadFlg = false;
                self.sourceData.active = self.sourceData.active ? self.sourceData.active ? "1" : "0" : '';
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
                self.sourceData.trackingSpreadFlg = self.sourceData.trackingSpreadFlg == true ? self.sourceData.trackingSpreadFlg = '1' : '';
                self.sourceData.active = self.sourceData.active == '1' ? true : false;
                self.sourceData.trackingSpreadFlg = self.sourceData.trackingSpreadFlg == true ? '1' : '';
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