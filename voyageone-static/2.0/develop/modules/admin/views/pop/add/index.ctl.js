/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddController', (function () {
        function AddController(context, channelService, AdminCartService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.popType = '编辑';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加';
                    self.sourceData = {}
                }
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
                if (type == 'secretKey') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.sourceData.screctKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.sourceData.sessionKey = res.data;
                    })
                }
            },
            selected: function (item) {
                var self = this;
                self.selectedCartId = item.cartId;
            },
            move: function (type) {
                var self = this;
                self.cartList = self.cartList ? self.cartList : [];
                self.cartAllList = self.cartAllList ? self.cartAllList : [];
                switch (type) {
                    case 'allInclude':
                        _.extend(self.cartList, self.cartAllList);
                        self.cartAllList = null;
                        break;
                    case 'include':
                        self.data = _.find(self.cartAllList, function (cart) {
                            return cart.cartId == self.selectedCartId;
                        });
                        self.cartList.push(self.data);
                        self.cartAllList.splice(self.cartAllList.indexOf(self.data), 1);
                        break;
                    case 'exclude':
                        self.data = _.find(self.cartList, function (cart) {
                            return cart.cartId == self.selectedCartId;
                        });
                        self.cartAllList.push(self.data);
                        self.cartList.splice(self.cartList.indexOf(self.data), 1);
                        break;
                    case 'allExclude':
                        _.extend(self.cartAllList, self.cartList);
                        self.cartList = null;
                        break;
                }
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
                    _.extend(self.sourceData, {'cartIds': tempCartList.join(',')});
                });
                if (self.append == true) {
                    self.channelService.addChannel(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result,{'res':'success','sourceData':self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.channelService.updateChannel(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result,{'res':'success','sourceData':self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddController;
    })())
});