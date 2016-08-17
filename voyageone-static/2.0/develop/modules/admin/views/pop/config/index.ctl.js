/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ConfigController', (function () {
        function ConfigController(popups, context, confirm, channelService, storeService, selectRowsFactory) {
            this.popups = popups;
            this.sourceData = context;
            this.confirm = confirm;
            this.channelService = channelService;
            this.storeService = storeService;
            this.selectRowsFactory = selectRowsFactory;
            this.configPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.configSelList = {selList: []};
            this.tempConfigSelect = null;
            this.searchInfo = {
                orderChannelId: this.sourceData ? this.sourceData.orderChannelId : "",
                storeId: this.sourceData ? this.sourceData.storeId : "",
                configType: this.sourceData.configType,
                pageInfo: this.configPageOption,
                cfgName: '',
                cfgVal: ''
            };
        }

        ConfigController.prototype = {
            init: function () {
                var self = this;
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.configInfo = {};
                self.channelService.getAllChannel().then(function (res) {
                    self.channelList = res.data;
                });
                self.storeService.getAllStore().then(function (res) {
                    self.storeList = res.data;
                });
                var data = {
                    'pageNum': self.searchInfo.pageInfo.curr,
                    'pageSize': self.searchInfo.pageInfo.size,
                    'configType': self.searchInfo.configType,
                    'orderChannelId': self.searchInfo.orderChannelId,
                    'storeId': self.searchInfo.storeId,
                    'cfgName': self.searchInfo.cfgName,
                    'cfgVal': self.searchInfo.cfgVal
                };
                switch (self.searchInfo.configType) {
                    case 'Channel':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "code": configInfo.cfgName,
                                "orderChannelId": configInfo.orderChannelId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1
                            };
                        };
                        self.channelService.searchChannelConfigByPage(data).then(function (res) {
                            callback(res, selectKey);
                        });
                        break;
                    case 'Store':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "code": configInfo.storeName,
                                "storeId": configInfo.storeId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1
                            };
                        };
                        self.storeService.searchStoreConfigByPage(data).then(function (res) {
                            callback(res, selectKey);
                        });
                        break;
                }
                function callback(res, selectKey) {
                    self.cfgList = res.data.result;
                    self.configPageOption.total = res.data.count;

                    if (self.tempConfigSelect == null) {
                        self.tempConfigSelect = new self.selectRowsFactory();
                    } else {
                        self.tempConfigSelect.clearCurrPageRows();
                        self.tempConfigSelect.clearSelectedList();
                    }
                    _.forEach(self.cfgList, function (configInfo, index) {
                        if (configInfo.updFlg != 8) {
                            _.extend(configInfo, {mainKey: index});
                            self.tempConfigSelect.currPageRows(selectKey(configInfo));
                        }
                    });
                    self.configSelList = self.tempConfigSelect.selectRowsInfo;
                }
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    orderChannelId: "",
                    configType: 'Channel',
                    pageInfo: self.configPageOption,
                    channelName: "",
                    cfgName: '',
                    cfgVal: ''
                };
            },
            add: function (item) {
                var self = this;
                switch (self.searchInfo.configType) {
                    case 'Channel':
                        self.list = _.filter(self.channelList, function (listItem) {
                            return listItem.orderChannelId == item.orderChannelId;
                        });
                        _.extend(item, {'channelName': self.list[0].name, 'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search();
                        });
                        break;
                    case 'Store':
                        self.list = _.filter(self.storeList, function (listItem) {
                            return listItem.storeId == item.storeId;
                        });
                        _.extend(item, {'shortName': self.list[0].storeName, 'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search();
                        });
                        break;
                }
            },
            edit: function () {
                var self = this;
                _.forEach(self.cfgList, function (cfgInfo) {
                    if (cfgInfo.mainKey == self.configSelList.selList[0].id) {
                        _.extend(cfgInfo, {'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(cfgInfo).then(function () {
                            self.search(1);
                        });
                        return;
                    }
                });
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                    var delList = [];
                    _.forEach(self.configSelList.selList, function (delInfo) {
                        _.extend(delInfo, {'configType': 'Channel'});
                        delList.push(delInfo);
                    });
                    switch (self.searchInfo.configType) {
                        case 'Channel':
                            self.channelService.deleteChannelConfig(delList).then(function (res) {
                                if (res.data.success == false)self.confirm(res.data.message);
                                self.search();
                            });
                            break;
                        case 'Store':
                            self.storeService.deleteStoreConfig(delList).then(function (res) {
                                if (res.data.success == false)self.confirm(res.data.message);
                                self.search();
                            });
                            break;
                    }
                });
            },
            getConfigType: function (type) {
                switch (type) {
                    case 'Channel':
                        return "渠道";
                        break;
                    case 'Store':
                        return "仓库";
                        break;
                }
            }
        };
        return ConfigController;
    })())
});