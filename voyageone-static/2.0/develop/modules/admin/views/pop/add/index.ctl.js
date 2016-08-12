/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddController', (function () {
        function AddController(context, channelService, AdminCartService) {
            this.sourceData = context ? context : {};
            this.append = context ? false : true;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
        }

        AddController.prototype = {
            init: function () {
                var self = this;
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
            generate: function (type) {
                var self = this;
                if (type == 'screctKey') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.sourceData.screctKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.sourceData.sessionKey = res.data;
                    })
                }
            }
        };
        return AddController;
    })())
});