/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideConfigController', (function () {
        function GuideConfigController(popups, alert, confirm, channelService, newShopService, AdminCartService, $location) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.channelService = channelService;
            this.newShopService = newShopService;
            this.AdminCartService = AdminCartService;
            this.resList = [];
            this.$location = $location;
            this.carrierSelList = {selList: []};
            this.tempChannelSelect = null;
            this.display = false;
            this.infoList = {
                companyId: '',
                orderChannelId: '',
                active: null,
                sendName: '',
                sendTel: '',
                sendZip: '',
                sendAddress: '',
                isUsjoi: null,
                name: '',
                fullName: '',
                imgUrl: '',
                cartIds: '',
                sessionKey: '',
                screctKey: '',
                channelConfig: []
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
                var url = self.$location.url();
                if (url.indexOf('previous=true') > -1) {
                    var context = window.sessionStorage.getItem('valueBean');
                    if (context) {
                        self.resListCopy = JSON.parse(context);
                        self.resList = self.resListCopy.channel;
                    }
                    self.display = true;
                }
            },
            copy: function (channelId) {
                var self = this;
                self.resList ? {} : {};
                self.newShopService.getChannelSeries(channelId).then(function (res) {
                    self.resListCopy = res.data;
                    if (self.autoCopy == true) {
                        self.resList = res.data.channel;
                        self.resList.sessionKey = '';
                        self.resList.screctKey = '';
                        if (!self.resList.cartIds) {
                            self.cartList = [];
                            callback();
                        } else {
                            self.AdminCartService.getCartByIds({'cartIds': self.resList.cartIds}).then(function (res) {
                                self.cartList = res.data;
                                callback();
                            });
                        }
                        function callback() {
                            self.AdminCartService.getAllCart(null).then(function (res) {
                                self.cartAllList = [];
                                if (self.cartList.length == 0) {
                                    self.cartAllList = res.data;
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

                        self.alert('渠道信息复制成功！').then(function () {
                            self.display = true;
                        });
                    } else {
                        self.resList = self.infoList;
                        self.cartList = [];
                        self.resList.sessionKey = '';
                        self.resList.screctKey = '';
                        self.AdminCartService.getAllCart(null).then(function (res) {
                            self.cartAllList = [];
                            if (self.cartList.length == 0) {
                                self.cartAllList = res.data;
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
                        self.alert('渠道信息复制成功！').then(function () {
                            self.display = true;
                        });
                    }
                })
            },
            generate: function (type) {
                var self = this;
                if (type == 'secretKey') {
                    self.channelService.generateSecretKey().then(function (res) {
                        self.resList.screctKey = res.data;
                    })
                } else {
                    self.channelService.generateSessionKey().then(function (res) {
                        self.resList.sessionKey = res.data;
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
                            _.forEach(self.cartAllList, function (item) {
                                self.cartList.push(item);
                            });
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
            config: function (type) {
                var self = this;
                var channelInfo = {
                    'orderChannelId': self.resList.orderChannelId,
                    'configType': type,
                    'isReadOnly': true,
                    'sourceData': self.resList
                };
                if (self.autoCopy != true) {
                    _.extend(self.resListCopy.channel, self.resList);
                }
                synchronizeChannelSeries(self.resListCopy);
                self.popups.openConfig(channelInfo);
            },
            next: function () {
                var self = this;
                // 设置cartIds
                var tempCartList = [];
                _.forEach(self.cartList, function (item) {
                    tempCartList.push(item.cartId);
                    _.extend(self.resListCopy.channel, {'cartIds': tempCartList.join(',')});
                });

                if (self.autoCopy != true) {
                    _.extend(self.resListCopy.channel, self.resList);
                }

                synchronizeChannelSeries(self.resListCopy);
                window.sessionStorage.setItem('valueBean', JSON.stringify(self.resListCopy));
                window.location.href = "#/newShop/guide/channelConfig";
            }
        };

        function _forEach(data, callback, subItemName) {
            _.forEach(data, function (item) {
                callback(item, subItemName);
            });
        }

        function synchronizeChannelSeries(data) {
            var channel = data.channel;
            var callback = function (item, subItemName) {
                item.orderChannelId = channel.orderChannelId;
                item.channelId = channel.orderChannelId;
                item.channelName = channel.name;
                if (subItemName) {
                    _forEach(item[subItemName], callback);
                }
            };
            _forEach(channel.channelConfig, callback);
            _forEach(data.sms, callback);
            _forEach(data.thirdParty, callback);
            _forEach(data.carrier, callback);
            _forEach(data.channelAttr, callback);
            _forEach(data.store, callback);
            _forEach(data.cartShop, callback);
            _forEach(data.cartShop, callback, 'cartShopConfig');
            _forEach(data.cartTracking, callback);
        }

        return GuideConfigController;
    })());
});