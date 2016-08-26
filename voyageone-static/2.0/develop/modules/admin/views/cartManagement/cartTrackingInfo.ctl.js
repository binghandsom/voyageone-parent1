/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CartTrackingInfoManagementController', (function () {
        function CartTrackingInfoManagementController(popups, alert, confirm, channelService, AdminCartService, cartTrackingService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.cartTrackingService = cartTrackingService;
            this.cartPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.cartList = [];
            this.cartTrackingSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                cartId: '',
                trackingStatus: '',
                location: '',
                active: '',
                pageInfo: this.cartPageOption
            }
        }

        CartTrackingInfoManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.AdminCartService.getAllCart().then(function (res) {
                    self.cartAllList = res.data;
                });
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.cartTrackingService.searchCartTrackingByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'cartId': self.searchInfo.cartId,
                        'orderChannelId': self.searchInfo.orderChannelId,
                        'trackingStatus': self.searchInfo.trackingStatus,
                        'location': self.searchInfo.location,
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
                                    "id": Info.seq,
                                    "orderChannelId": Info.orderChannelId,
                                    "cartId": Info.cartId
                                });
                            }
                        });
                        self.cartTrackingSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.cartPageOption,
                    orderChannelId: '',
                    cartId: '',
                    trackingStatus: '',
                    active: '',
                    location: ''
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openCartTrackingInfo('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.cartTrackingSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.cartList, function (Info) {
                            if (Info.seq == self.cartTrackingSelList.selList[0].id) {
                                self.popups.openCartTrackingInfo(Info).then(function () {
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
                        _.forEach(self.cartTrackingSelList.selList, function (delInfo) {
                            delList.push({'seq': delInfo.id, 'cartId': delInfo.cartId});
                        });
                        self.cartTrackingService.deleteCartTracking(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
            }
        };
        return CartTrackingInfoManagementController;
    })())
});