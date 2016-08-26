/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CartChannelShopManagementController', (function () {
        function CartChannelShopManagementController(popups, alert, confirm, AdminCartService, channelService, cartShopService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.AdminCartService = AdminCartService;
            this.channelService = channelService;
            this.cartShopService = cartShopService;
            this.cartPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.cartList = [];
            this.cartShopSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                cartId: '',
                shopName: '',
                active: '',
                pageInfo: this.cartPageOption
            }
        }

        CartChannelShopManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.AdminCartService.getAllCart().then(function (res) {
                    self.cartAllList = res.data;
                });
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.cartShopService.searchCartShopByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'orderChannelId': self.searchInfo.orderChannelId,
                        'cartId': self.searchInfo.cartId,
                        'shopName': self.searchInfo.shopName,
                        'active': self.searchInfo.active
                    })
                    .then(function (res) {
                        self.cartList = res.data.result;
                        self.cartPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.cartList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.cartId,
                                    "code": Info.name,
                                    "orderChannelId": Info.orderChannelId
                                });
                            }
                        });
                        self.cartShopSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.cartPageOption,
                    orderChannelId: '',
                    cartId: '',
                    active: '',
                    shopName: ''
                }
            },
            config: function (type) {
                var self = this;
                if (self.cartShopSelList.selList.length < 1) {
                    self.popups.openConfig({'configType': type});
                    return;
                } else {
                    _.forEach(self.cartList, function (Info) {
                        if (Info.cartId == self.cartShopSelList.selList[0].id) {
                            _.extend(Info, {'configType': type});
                            self.popups.openConfig(Info);
                        }
                    })
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openCartChannelShop('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.cartShopSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.cartList, function (Info) {
                            if (Info.cartId == self.cartShopSelList.selList[0].id) {
                                self.popups.openCartChannelShop(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.cartShopSelList.selList, function (delInfo) {
                            delList.push({'cartId': delInfo.id, 'orderChannelId': delInfo.orderChannelId});
                        });
                        self.cartShopService.deleteCartShop(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
            },
            getCartType: function (type) {
                switch (type) {
                    case '1':
                        return '中国店铺';
                        break;
                    case '2':
                        return '国外店铺';
                        break;
                    case '3':
                        return 'MiniMall';
                        break;
                }
            }

        };
        return CartChannelShopManagementController;
    })())
});