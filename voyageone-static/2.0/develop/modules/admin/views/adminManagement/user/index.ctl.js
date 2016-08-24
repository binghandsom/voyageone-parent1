/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('UserManagementController', (function () {
        function UserManagementController(popups, alert, confirm, adminUserService, storeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.selectRowsFactory = selectRowsFactory;
            this.storeService = storeService;
            this.storePageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.storeList = [];
            this.storeSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                userAccount: '',
                roleId: '',
                active: '',
                channelId: '',
                orgId: '',
                storeId: '',
                pageInfo: this.storePageOption
            }
        }

        UserManagementController.prototype = {
            init: function () {
                var self = this;
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.adminUserService.init({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'userAccount': self.searchInfo.userAccount,
                        'roleId': self.searchInfo.roleId,
                        'active': self.searchInfo.active,
                        'channelId': self.searchInfo.channelId,
                        'orgId': self.searchInfo.orgId,
                        'storeId': self.searchInfo.storeId
                    })
                    .then(function (res) {
                        self.storeList = res.data.result;
                        self.storePageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.storeList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.storeId,
                                    "code": Info.storeName,
                                    "orderChannelId": Info.orderChannelId
                                });
                            }
                        });
                        self.storeSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: self.storePageOption,
                    userAccount: '',
                    roleId: '',
                    active: '',
                    channelId: '',
                    orgId: '',
                    storeId: ''
                }
            },
            config: function (type) {
                var self = this;
                if (self.storeSelList.selList.length < 1) {
                    self.popups.openConfig({'configType': type});
                    return;
                } else {
                    _.forEach(self.storeList, function (storeInfo) {
                        if (storeInfo.storeId == self.storeSelList.selList[0].id) {
                            _.extend(storeInfo, {'configType': type});
                            self.popups.openConfig(storeInfo);
                        }
                    })
                }
            },
            edit: function () {
                var self = this;
                if (self.storeSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.storeList, function (Info) {
                        if (Info.storeId == self.storeSelList.selList[0].id) {
                            Info['areaId'] = Info['areaId'] + '';
                            var copyData = Info.inventoryHold.split(",");
                            Info.inventoryHold = copyData[0];
                            Info.remainNum = copyData[1];
                            self.popups.openStoreAdd(Info).then(function () {
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
                        _.forEach(self.storeSelList.selList, function (delInfo) {
                            delList.push({'orderChannelId': delInfo.orderChannelId, 'storeId': delInfo.id});
                        });
                        self.storeService.deleteStore(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
            },
            getStoreType: function (type) {
                switch (type) {
                    case '0':
                        return '自营仓库';
                        break;
                    case '1':
                        return '第三方合作仓库';
                        break;
                    case '2':
                        return '菜鸟保税仓';
                        break;
                    case '3':
                        return '聚美保税仓';
                        break;
                }
            },
            getInventoryHold: function (item) {
                if (item.indexOf(',') < 0) {
                    switch (item) {
                        case '0':
                            return '不做保留';
                            break;
                        case '1':
                            return '按加减保留';
                            break;
                        case '2':
                            return '按百分比保留';
                            break;
                        case '3':
                            return '按销售计算（默认百分比）';
                            break;
                    }
                } else {
                    var type = item.split(",")[0];
                    var num = item.split(",")[1];
                    switch (type) {
                        case '0':
                            return '不做保留' + '' + num;
                            break;
                        case '1':
                            return '按加减保留' + '' + num;
                            break;
                        case '2':
                            return '按百分比保留' + '' + num;
                            break;
                        case '3':
                            return '按销售计算（默认百分比）' + '' + num;
                            break;
                    }
                }
            }
        };
        return UserManagementController;
    })())
});
