/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('UnifiedConfigController', (function () {
        function UnifiedConfigController(popups, alert, confirm, channelService, AdminCartService, storeService, taskService, typeAttrService, cartShopService, portConfigService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.alert = alert;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.AdminCartService = AdminCartService;
            this.storeService = storeService;
            this.taskService = taskService;
            this.typeAttrService = typeAttrService;
            this.cartShopService = cartShopService;
            this.portConfigService = portConfigService;
            this.configPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.configSelList = [];
            this.configSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                pageInfo: this.configPageOption,
                orderChannelId: '',
                storeId: '',
                taskId: '',
                cartId: '',
                port: '',
                configType: '',
                cfgName: '',
                cfgVal: ''
            }
        }

        UnifiedConfigController.prototype = {
            init: function () {
                var self = this;
                switch (self.searchInfo.configType) {
                    case 'Channel':
                        self.channelService.getAllChannel().then(function (res) {
                            self.channelList = res.data;
                        });
                        break;
                    case 'Store':
                        self.storeService.getAllStore(null).then(function (res) {
                            self.storeList = res.data;
                        });
                        break;
                    case 'Task':
                        self.taskService.getAllTask().then(function (res) {
                            self.taskList = res.data;
                        });
                        break;
                    case 'Shop':
                        self.channelService.getAllChannel().then(function (res) {
                            self.channelAllList = res.data;
                        });
                        self.AdminCartService.getAllCart(null).then(function (res) {
                            self.cartAllList = res.data;
                        });
                        break;
                    case 'Port':
                        self.portConfigService.getAllPort().then(function (res) {
                            self.portList = res.data;
                        });
                        break;
                }
                self.search(1)
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.configInfo = {};
                var data = {
                    'pageNum': self.searchInfo.pageInfo.curr,
                    'pageSize': self.searchInfo.pageInfo.size,
                    'configType': self.searchInfo.configType,
                    'orderChannelId': self.searchInfo.orderChannelId,
                    'storeId': self.searchInfo.storeId,
                    'taskId': self.searchInfo.taskId,
                    'cartId': self.searchInfo.cartId,
                    'port': self.searchInfo.port,
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
                    case 'Task':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "code": configInfo.taskName,
                                "taskId": configInfo.taskId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1,
                                'cfgVal2': configInfo.cfgVal2
                            };
                        };
                        self.taskService.searchTaskConfigByPage(data).then(function (res) {
                            callback(res, selectKey);
                        });
                        break;
                    case 'Shop':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "orderChannelId": configInfo.orderChannelId,
                                "cartId": configInfo.cartId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1,
                                'cfgVal2': configInfo.cfgVal2
                            };
                        };
                        self.cartShopService.searchCartShopConfigByPage(data).then(function (res) {
                            callback(res, selectKey);
                        });
                        break;
                    case 'Port':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "code": configInfo.taskName,
                                "port": configInfo.port,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1,
                                'cfgVal2': configInfo.cfgVal2
                            };
                        };
                        self.portConfigService.searchPortConfigByPage(data).then(function (res) {
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
                    storeId: "",
                    taskId: "",
                    cartId: "",
                    port: '',
                    configType: "",
                    cfgName: '',
                    cfgVal: '',
                    pageInfo: self.configPageOption
                };
            },
            add: function (item) {
                var self = this;
                switch (self.searchInfo.configType) {
                    case 'Channel':
                        if (item.orderChannelId == undefined || item.orderChannelId == '') {
                            self.alert('请选择一个渠道！');
                            return;
                        }
                        self.list = _.filter(self.channelList, function (listItem) {
                            return listItem.orderChannelId == item.orderChannelId;
                        });
                        _.extend(item, {'channelName': self.list[0].name, 'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search();
                        });
                        break;
                    case 'Store':
                        if (item.storeId == undefined || item.storeId == '') {
                            self.alert('请选择一个仓库！');
                            return;
                        }
                        self.list = _.filter(self.storeList, function (listItem) {
                            return listItem.storeId == item.storeId;
                        });
                        _.extend(item, {'shortName': self.list[0].storeName, 'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search();
                        });
                        break;
                    case 'Task':
                        if (item.taskId == undefined || item.taskId == '') {
                            self.alert('请选择一个任务！');
                            return;
                        }
                        self.list = _.filter(self.taskList, function (listItem) {
                            return listItem.taskId == item.taskId;
                        });
                        _.extend(item, {'taskName': self.list[0].taskName, 'configType': self.searchInfo.configType});
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search();
                        });
                        break;
                    case 'Shop':
                        if (item.orderChannelId == undefined || item.cartId == undefined || item.orderChannelId == '' || item.cartId == '') {
                            self.alert('请选择渠道和Cart！');
                            return;
                        }
                        self.channelLlist = _.filter(self.channelAllList, function (listItem) {
                            return listItem.orderChannelId == item.orderChannelId;
                        });
                        self.cartList = _.filter(self.cartAllList, function (listItem) {
                            return listItem.cartId == item.cartId;
                        });
                        _.extend(item, {
                            'channelName': self.channelLlist[0].name,
                            'cartName': self.cartList[0].name,
                            'configType': self.searchInfo.configType
                        });
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') self.search(1);
                        });
                        break;
                    case 'Port':
                        if (item.port == undefined || item.port == '') {
                            self.alert('请选择一个港口！');
                            return;
                        }
                        self.list = _.filter(self.portList, function (listItem) {
                            return listItem.code == item.port;
                        });
                        _.extend(item, {'port': self.list[0].port, 'configType': self.searchInfo.configType});
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
                        self.popups.openCreateEdit(cfgInfo);
                    }
                });
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                    var delList = [];
                    _.forEach(self.configSelList.selList, function (delInfo) {
                        _.extend(delInfo, {'configType': self.searchInfo.configType, 'seq': delInfo.id + 1});
                        delList.push(delInfo);
                    });
                    switch (self.searchInfo.configType) {
                        case 'Channel':
                            self.channelService.deleteChannelConfig(delList).then(function (res) {
                                if (res.data == false)self.alert(res.data.message);
                                self.search(1);
                            });
                            break;
                        case 'Store':
                            self.storeService.deleteStoreConfig(delList).then(function (res) {
                                if (res.data == false)self.alert(res.data.message);
                                self.search(1);
                            });
                            break;
                        case 'Task':
                            self.taskService.deleteTaskConfig(delList).then(function (res) {
                                if (res.data == false)self.alert(res.data.message);
                                self.search(1);
                            });
                            break;
                        case 'Shop':
                            self.cartShopService.deleteCartShopConfig(delList).then(function (res) {
                                if (res.data == false)self.alert(res.data.message);
                                self.search(1);
                            });
                            break;
                        case 'Port':
                            var seqList = [];
                            _.map(delList, function (item) {
                                seqList.push(item.seq);
                            });
                            self.portConfigService.deletePortConfig(seqList).then(function (res) {
                                if (res.data == false)self.alert(res.data.message);
                                self.search(1);
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
                    case 'Task':
                        return "任务";
                        break;
                    case 'Shop':
                        return "Cart";
                        break;
                    case 'Port':
                        return "港口";
                        break;
                }
            }
        };
        return UnifiedConfigController;
    })())
});