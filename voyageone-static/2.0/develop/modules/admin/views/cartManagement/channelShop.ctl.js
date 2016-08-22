/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CartChannelShopManagementController', (function () {
        function CartChannelShopManagementController(popups, alert, confirm, AdminCartService, channelService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.AdminCartService = AdminCartService;
            this.channelService = channelService;
            this.cartPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.cartList = [];
            this.cartSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                cartId: null,
                cartName: '',
                cartType: '',
                pageInfo: this.cartPageOption
            }
        }

        CartChannelShopManagementController.prototype = {
            init: function () {
                var self = this;
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.AdminCartService.searchCartByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'cartId': self.searchInfo.cartId,
                        'cartName': self.searchInfo.cartName,
                        'cartType': self.searchInfo.cartType
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
                                    "code": Info.name
                                });
                            }
                        });
                        self.cartSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.cartPageOption,
                    'orderChannelId': '',
                    'channelName': '',
                    'isUsjoi': ''
                }
            },
            edit: function () {
                var self = this;
                if (self.cartSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.cartList, function (Info) {
                        if (Info.cartId == self.cartSelList.selList[0].id) {
                            self.popups.openCartAdd(Info).then(function () {
                                self.search(1);
                            });
                        }
                    })
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.cartSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.AdminCartService.deleteCart(delList).then(function (res) {
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