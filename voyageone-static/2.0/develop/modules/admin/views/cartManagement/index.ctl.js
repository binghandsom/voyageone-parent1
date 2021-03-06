/**
 * @Date:    2016-08-10 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CartManagementController', (function () {
        function CartManagementController(popups, alert, confirm, AdminCartService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.AdminCartService = AdminCartService;
            this.cartPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.cartList = [];
            this.cartSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                cartId: null,
                cartName: '',
                cartType: '',
                active: '',
                pageInfo: this.cartPageOption
            }
        }

        CartManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
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
                        'cartType': self.searchInfo.cartType,
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
                    orderChannelId: '',
                    channelName: '',
                    active: '',
                    isUsjoi: ''
                }
            },
            edit: function (item) {
                var self = this;
                if (item == 'add') {
                    self.popups.openCartAdd('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    self.popups.openCartAdd(item).then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                }
            },
            delete: function (item) {
                var self = this, delList = [];
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                    if(item=='batchDel'){
                        _.forEach(self.cartSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                    }else{
                        delList.push(item);
                    }
                        self.AdminCartService.deleteCart(delList).then(function (res) {
                            if(res.data==true) self.search(1);
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
        return CartManagementController;
    })())
});
