/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddController', (function () {
        function AddController(context, channelService, AdminCartService) {
            this.type = context;
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
                })
            },
            generate: function (type) {
                var self = this;
                if (type == 'secrect') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.secretKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.sessionKey = res.data;
                    })
                }
            }
        };
        return AddController;
    })())
});