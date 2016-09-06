/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideConfigController', (function () {
        function GuideConfigController(popups, alert, confirm, channelService, newShopService, AdminCartService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.newShopService = newShopService;
            this.AdminCartService = AdminCartService;
            this.resList = [];
            this.carrierSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                carrier: '',
                usekd100Flg: '',
                active: ''
            };
        }

        GuideConfigController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.channelService.getAllCompany().then(function (res) {
                    self.companyAllList = res.data;
                });
            },
            copy: function (channelId) {
                var self = this;
                self.newShopService.getChannelSeries(channelId).then(function (res) {
                    if (self.autoCopy == true) {
                        self.resList = res.data;
                        if (!self.resList.channel.cartIds) {
                            self.cartList = [];
                            callback();
                        } else {
                            self.AdminCartService.getCartByIds({'cartIds': self.resList.channel.cartIds}).then(function (res) {
                                self.cartList = res.data;
                                callback();
                            });
                        }
                        function callback() {
                            self.AdminCartService.getAllCart(null).then(function (res) {
                                self.cartAllList = [];
                                if (self.cartList.length == 0) {
                                    self.cartAllList = res.data;
                                    return;
                                } else {
                                    self.cartAllList = res.data;
                                    _.forEach(self.cartList, function (item) {
                                        self.data = _.find(self.cartAllList, function (cart) {
                                            return cart.cartId == item.cartId;
                                        });
                                        self.cartAllList.splice(self.cartAllList.indexOf(self.data), 1);
                                    });
                                }
                            });
                        }
                    } else {
                        self.resList = {};
                    }
                })
            },
            generate: function (type) {
                var self = this;
                if (type == 'secretKey') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.screctKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.sessionKey = res.data;
                    })
                }
            },
            selected: function (item) {
                var self = this;
                self.selectedCartId = item.cartId;
            },
            search: function (item) {
                var self = this;
                self.allList = [];
                _.filter(self.cartAllList, function (data) {
                    if (data.name.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
                        self.allList.push(data)
                    }
                });
                self.cartAllList = self.allList;
            },
            move: function (type) {
                var self = this;
                self.AdminCartService.getAllCart(null).then(function (res) {
                    self.cartAllListCopy = res.data;
                });
                self.cartList = self.cartList ? self.cartList : [];
                self.cartAllList = self.cartAllList ? self.cartAllList : [];
                switch (type) {
                    case '':
                        self.cartAllList = self.cartAllListCopy;
                        _.forEach(self.cartList, function (item) {
                            var index = -1;
                            _.forEach(self.cartAllList, function (e, i) {
                                if (e.cartId == item.cartId) {
                                    index = i;
                                    return;
                                }
                            });
                            if (index > -1) {
                                self.cartAllList.splice(index, 1);
                            }
                        });
                        break;
                    case 'allInclude':
                        if (self.allList) {
                            self.cartAllList = self.allList;
                            _.extend(self.cartList, self.cartAllList);
                            self.cartAllList = [];
                            break;
                        } else {
                            _.forEach(self.cartAllList, function (item) {
                                self.cartList.push(item);
                            });
                            self.cartAllList = [];
                            break;
                        }
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
                        _.forEach(self.cartList, function (item) {
                            self.cartAllList.push(item);
                        });
                        self.cartList = [];
                        break;
                }
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.channelPageOption,
                    orderChannelId: '',
                    carrier: '',
                    active: '',
                    usekd100Flg: ''
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openChannelCarrier('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.carrierSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.resList, function (Info) {
                            if (Info.mainKey == self.carrierSelList.selList[0].id) {
                                self.popups.openChannelCarrier(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.carrierSelList.selList, function (delInfo) {
                            delList.push({'orderChannelId': delInfo.orderChannelId, 'carrier': delInfo.code});
                        });
                        self.carrierConfigService.deleteCarrierConfig(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return GuideConfigController;
    })());
});