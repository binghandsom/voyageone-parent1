/**
 * @Date:    2016-08-10 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('StoreManagementController', (function () {
        function StoreManagementController(popups, alert, confirm, channelService, storeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.channelService = channelService;
            this.selectRowsFactory = selectRowsFactory;
            this.storeService = storeService;
            this.storePageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.storeList = [];
            this.storeSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                storeName: '',
                isSale: '',
                active: '',
                storeType: '',
                pageInfo: this.storePageOption
            }
        }

        StoreManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelList = res.data;
                });
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.storeService.searchStoreByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'orderChannelId': self.searchInfo.orderChannelId,
                        'storeName': self.searchInfo.storeName,
                        'isSale': self.searchInfo.isSale,
                        'active': self.searchInfo.active,
                        'storeType': self.searchInfo.storeType
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
                    pageInfo: this.storePageOption,
                    'orderChannelId': '',
                    'storeName': '',
                    'active': '',
                    'isSale': '',
                    'storeType': ''
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
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openStoreAdd('add').then(function () {
                        self.search(1);
                    });
                } else {
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
        return StoreManagementController;
    })())
});
