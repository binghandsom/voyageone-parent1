/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ConfigController', (function () {
        function ConfigController(popups, context, confirm, alert, channelService, storeService, taskService, AdminCartService, cartShopService, selectRowsFactory) {
            this.popups = popups;
            this.sourceData = context;
            this.confirm = confirm;
            this.alert = alert;
            this.channelService = channelService;
            this.storeService = storeService;
            this.taskService = taskService;
            this.AdminCartService = AdminCartService;
            this.cartShopService = cartShopService;
            this.selectRowsFactory = selectRowsFactory;

            var self = this;
            this.configPageOption = {
                curr: 1, size: 10, total: 0, fetch: function (page) {
                    self.search(page)
                }
            };
            this.configSelList = {selList: []};
            this.tempConfigSelect = null;
            this.searchInfo = {
                orderChannelId: this.sourceData ? this.sourceData.orderChannelId : "",
                storeId: this.sourceData ? this.sourceData.storeId : "",
                taskId: this.sourceData ? this.sourceData.taskId : "",
                cartId: this.sourceData ? this.sourceData.cartId : "",
                configType: this.sourceData.configType,
                pageInfo: this.configPageOption,
                cfgName: '',
                cfgVal: ''
            };
        }

        ConfigController.prototype = {
            init: function () {
                var self = this;
                self.storeCfgList = [];
                self.taskCfgList = [];
                switch (self.searchInfo.configType) {
                    case 'Channel':
                        if (self.sourceData.isReadOnly == true) {
                            self.channelList = [self.sourceData.sourceData];
                        } else {
                            self.channelService.getAllChannel().then(function (res) {
                                self.channelList = res.data;
                            });
                        }
                        break;
                    case 'Store':
                        if (self.sourceData.isReadOnly == true) {
                            self.storeList = self.sourceData.sourceData;
                            _.forEach(self.sourceData.sourceData, function (item) {
                                _.forEach(item.storeConfig, function (storeConfig) {
                                    self.storeCfgList.push(storeConfig);
                                });
                            });
                        } else {
                            self.storeService.getAllStore(null).then(function (res) {
                                self.storeList = res.data;
                            });
                        }
                        break;
                    case
                    'Task':
                        if (self.sourceData.isReadOnly == true) {
                            self.taskList = self.sourceData.sourceData;
                            _.forEach(self.sourceData.sourceData, function (item) {
                                _.forEach(item.taskConfig, function (taskConfig) {
                                    self.taskCfgList.push(taskConfig);
                                });
                            });
                        } else {
                            self.taskService.getAllTask().then(function (res) {
                                self.taskList = res.data;
                            });
                        }
                        break;
                    case
                    'Shop':
                        self.cartCfgList = [];
                        if (self.sourceData.isReadOnly == true) {
                            var channel = self.sourceData.channelInfo;
                            self.channelAllList = [channel];
                            self.AdminCartService.getCartByIds({cartIds: channel.cartIds}).then(function (res) {
                                self.cartAllList = res.data;
                            });
                            _.forEach(self.sourceData.sourceData, function (item) {
                                _.forEach(item.cartShopConfig, function (cartShopConfig) {
                                    self.cartCfgList.push(cartShopConfig);
                                });
                            });
                        } else {
                            self.channelService.getAllChannel().then(function (res) {
                                self.channelAllList = res.data;
                            });
                            self.AdminCartService.getAllCart(null).then(function (res) {
                                self.cartAllList = res.data;
                            });
                        }
                        break;
                }
                self.search(1,{taskName:self.sourceData.taskName});
            },
            search: function (page, options) {
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
                        if (self.sourceData.isReadOnly == true) {
                            res = self.getConfigPaginationData(self.sourceData.sourceData.channelConfig);
                            callback(res, selectKey);
                        } else {
                            self.channelService.searchChannelConfigByPage(data).then(function (res) {
                                callback(res, selectKey);
                            });
                        }
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
                        if (self.sourceData.isReadOnly == true) {
                            res = self.getConfigPaginationData(self.storeCfgList, function (e) {
                                if (self.searchInfo.storeId != null && self.searchInfo.storeId != '') {
                                    if (e.storeId != null && e.storeId != self.searchInfo.storeId) {
                                        return false;
                                    }
                                }
                                return true;
                            });
                            callback(res, selectKey);
                        } else {
                            self.storeService.searchStoreConfigByPage(data).then(function (res) {
                                callback(res, selectKey);
                            });
                        }
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
                        if (self.sourceData.isReadOnly == true) {
                            var taskName = options ? options.taskName : angular.element('#taskId option:selected').text();
                            res = self.getConfigPaginationData(self.taskCfgList, function (e) {
                                if (taskName != null && taskName != '') {
                                    if (e.taskId != null && e.taskId != taskName) {
                                        return false;
                                    }
                                }
                                return true;
                            });
                            callback(res, selectKey);
                        } else {
                            self.taskService.searchTaskConfigByPage(data).then(function (res) {
                                callback(res, selectKey);
                            });
                        }
                        break;
                    case 'Shop':
                        var selectKey = function (configInfo) {
                            return {
                                "id": configInfo.mainKey,
                                "code": configInfo.taskName,
                                "orderChannelId": configInfo.orderChannelId,
                                "cartId": configInfo.cartId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1,
                                'cfgVal2': configInfo.cfgVal2
                            };
                        };
                        if (self.sourceData.isReadOnly == true) {
                            res = self.getConfigPaginationData(self.cartCfgList);
                            callback(res, selectKey);
                        } else {
                            self.cartShopService.searchCartShopConfigByPage(data).then(function (res) {
                                callback(res, selectKey);
                            });
                        }
                        break;
                }
                function callback(res, selectKey) {
                    self.cfgList = res.data ? res.data.result : res;
                    self.configPageOption.total = res.data ? res.data.count : self.cfgList.length;

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
                    configType: self.sourceData.configType,
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
                        _.extend(item, {
                            'channelName': self.list[0].name,
                            'configType': self.searchInfo.configType,
                            'isReadOnly': self.sourceData.isReadOnly
                        });
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') {
                                self.search();
                            } else {
                                var list = self.sourceData.sourceData.channelConfig;
                                list.push(res);
                                self.search(1);
                            }
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
                        _.extend(item, {
                            'shortName': self.list[0].storeName,
                            'configType': self.searchInfo.configType,
                            'isReadOnly': self.sourceData.isReadOnly,
                            'storeName': self.list[0].storeName
                        });
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') {
                                self.search();
                            } else {
                                var list = self.storeCfgList;
                                list.push(res);
                                _forEachAdd(self.sourceData.sourceData, 'storeConfig', res);
                                self.search(1);
                            }
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
                        _.extend(item, {
                            'taskName': self.list[0].taskName,
                            'configType': self.searchInfo.configType,
                            'isReadOnly': self.sourceData.isReadOnly
                        });
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') {
                                self.search()
                            } else {
                                var list = self.taskCfgList;
                                list.push(res);
                                _forEachAdd(self.sourceData.sourceData, 'taskConfig', res);
                                self.search(1);
                            }
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
                            'configType': self.searchInfo.configType,
                            'isReadOnly': self.sourceData.isReadOnly
                        });
                        self.popups.openCreateEdit(item).then(function (res) {
                            if (res.res == 'success') {
                                self.search();
                            } else {
                                var list = self.cartCfgList;
                                list.push(res);
                                _forEachAdd(self.sourceData.sourceData, 'cartShopConfig', res);
                                self.search(1);
                            }
                        });
                        break;
                }
            },
            edit: function () {
                var self = this;
                _.forEach(self.cfgList, function (cfgInfo) {
                    if (cfgInfo.mainKey == self.configSelList.selList[0].id) {
                        _.extend(cfgInfo, {
                            'configType': self.searchInfo.configType,
                            'isReadOnly': self.sourceData.isReadOnly
                        });
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
                        _.extend(delInfo, {'configType': self.searchInfo.configType});
                        delList.push(delInfo);
                    });
                    switch (self.searchInfo.configType) {
                        case 'Channel':
                            if (self.sourceData.isReadOnly == true) {
                                _.forEach(delList, function (item) {
                                    var source = self.sourceData.sourceData.channelConfig;
                                    var dataIndex = -1;
                                    _.forEach(source, function (sItem, i) {
                                        if (sItem.mainKey == item.id) {
                                            dataIndex = i;
                                        }
                                    });
                                    if (dataIndex > -1) {
                                        source.splice(dataIndex, 1);
                                    }
                                });
                                self.search(1);
                            } else {
                                self.channelService.deleteChannelConfig(delList).then(function (res) {
                                    if (res.data == false)self.alert(res.data.message);
                                    self.search(1);
                                });
                            }
                            break;
                        case 'Store':
                            if (self.sourceData.isReadOnly == true) {
                                _.forEach(delList, function (item) {
                                    var source = self.storeCfgList;
                                    var data = _.find(source, function (sItem) {
                                        return sItem.storeId == item.storeId;
                                    });
                                    if (source.indexOf(data) > -1) {
                                        source.splice(source.indexOf(data), 1);
                                    }
                                    _forEach(self.sourceData.sourceData, 'storeConfig', data);
                                });
                                self.search(1);
                            } else {
                                self.storeService.deleteStoreConfig(delList).then(function (res) {
                                    if (res.data == false)self.alert(res.data.message);
                                    self.search(1);
                                });
                            }
                            break;
                        case 'Task':
                            if (self.sourceData.isReadOnly == true) {
                                _.forEach(delList, function (item) {
                                    var source = self.taskCfgList;
                                    var data = _.find(source, function (sItem) {
                                        return sItem.taskId == item.taskId;
                                    });
                                    if (source.indexOf(data) > -1) {
                                        source.splice(source.indexOf(data), 1);
                                    }
                                    _forEach(self.sourceData.sourceData, 'taskConfig', data);
                                });
                                self.search(1);
                            } else {
                                self.taskService.deleteTaskConfig(delList).then(function (res) {
                                    if (res.data == false)self.alert(res.data.message);
                                    self.search(1);
                                });
                            }
                            break;
                        case 'Shop':
                            if (self.sourceData.isReadOnly == true) {
                                _.forEach(delList, function (item) {
                                    var source = self.cartCfgList;
                                    var data = _.find(source, function (sItem) {
                                        return sItem.cartId == item.cartId;
                                    });
                                    if (source.indexOf(data) > -1) {
                                        source.splice(source.indexOf(data), 1);
                                    }
                                    _forEach(self.sourceData.sourceData, 'cartShopConfig', data);
                                });
                                self.search(1);
                            } else {
                                self.cartShopService.deleteCartShopConfig(delList).then(function (res) {
                                    if (res.data == false)self.alert(res.data.message);
                                    self.search(1);
                                });
                            }
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
            },
            getConfigPaginationData: function (data, filterFn) {
                var self = this;
                var res = {
                    data: {count: data.length, result: []}
                };
                var pageNum = self.searchInfo.pageInfo.curr,
                    pageSize = self.searchInfo.pageInfo.size;
                if (res.data.count > 0) {
                    var unionFilterFn = function (e) {
                        if (e.cfgName != null && e.cfgName.indexOf(self.searchInfo.cfgName) == -1) {
                            return false;
                        } else if ((e.cfgVal1 != null && e.cfgVal1.indexOf(self.searchInfo.cfgVal) == -1)
                            && (e.cfgVal2 != null && e.cfgVal2.indexOf(self.searchInfo.cfgVal) == -1)) {
                            return false;
                        } else if (typeof filterFn === "function") {
                            if (filterFn(e) === false) {
                                return false;
                            }
                        }
                        return true;
                    };
                    res.data.count = 0;
                    _.forEach(data, function (e, i) {
                        if (unionFilterFn(e, i)) {
                            res.data.count++;
                            res.data.result.push(e);
                        }
                    });
                }
                var result = [];
                _.forEach(res.data.result, function (e, i) {
                    if (i >= pageSize * (pageNum - 1) && i < pageSize * pageNum) {
                        result.push(e);
                    }
                });
                res.data.result = result;
                return res;
            },
            show: function (item) {
                console.log(item);
            }
        };
        function _forEach(parentData, subData, target) {
            _.forEach(parentData, function (item, x) {
                var source = parentData;
                var targetData = target;
                var aim = [targetData.cfgName, targetData.cfgVal1, targetData.cfgVal2].join("|");
                switch (subData) {
                    case 'storeConfig':
                        _.forEach(item[subData], function (subItem, y) {
                            if (!subItem) return;
                            var compare = [subItem.cfgName, subItem.cfgVal1, subItem.cfgVal2].join("|");
                            if (aim === compare) {
                                source[x].storeConfig.splice(y, 1);
                            }
                        });
                        break;
                    case 'cartShopConfig':
                        _.forEach(item[subData], function (subItem, y) {
                            if (!subItem) return;
                            var compare = [subItem.cfgName, subItem.cfgVal1, subItem.cfgVal2].join("|");
                            if (aim === compare) {
                                source[x].cartShopConfig.splice(y, 1);
                            }
                        });
                        break;
                    case 'taskConfig':
                        _.forEach(item[subData], function (subItem, y) {
                            if (!subItem) return;
                            var compare = [subItem.cfgName, subItem.cfgVal1, subItem.cfgVal2].join("|");
                            if (aim === compare) {
                                source[x].taskConfig.splice(y, 1);
                            }
                        });
                        break;
                }
            });
        }

        function _forEachAdd(parentData, subData, target) {
            _.forEach(parentData, function (item, x) {
                var source = parentData;
                var targetData = target;
                switch (subData) {
                    case 'storeConfig':
                        if (item.storeName === targetData.storeName) {
                            source[x].storeConfig.push(targetData);
                        }
                        break;
                    case 'cartShopConfig':
                        if (item.channelName === targetData.channelName && item.cartName === targetData.cartName) {
                            source[x].cartShopConfig.push(targetData);
                        }
                        break;
                    case 'taskConfig':
                        if (item.taskName === targetData.taskName) {
                            source[x].taskConfig.push(targetData);
                        }
                        break;
                }

            })

        }

        return ConfigController;
    })())
});