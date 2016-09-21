/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddRoleController', (function () {
        function AddRoleController(context, adminRoleService, adminOrgService, adminUserService, channelService, storeService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;
            this.channelService = channelService;
            this.storeService = storeService;
            this.popType = '修改角色';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
            this.sourceData.allChannel = this.sourceData !== 'add' ? (this.sourceData.channelId != null ? this.sourceData.channelId.indexOf('ALL') > -1 ? '1' : '' : '') : '';
            this.sourceData.allStore = this.sourceData !== 'add' ? (this.sourceData.storeId != null ? this.sourceData.storeId.indexOf('ALL') > -1 ? '1' : '' : '') : '';
            this.applicationList = [
                {'id': 1, 'application': 'Admin', 'valid': false},
                {'id': 2, 'application': 'CMS', 'valid': false},
                {'id': 3, 'application': 'OMS', 'valid': false},
                {'id': 4, 'application': 'WMS', 'valid': false}
            ];
            this.sourceData.roleType = this.sourceData.roleType + '';
        }

        AddRoleController.prototype = {
            init: function () {
                var self = this;
                self.adminRoleService.getAllRoleType().then(function (res) {
                    self.roleTypeList = res.data;
                });
                if (self.sourceData == 'add') {
                    self.popType = '添加角色';
                    self.sourceData = {};
                    self.sourceData.active = '1';
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });

                self.channelService.getAllChannel(null).then(function (res) {
                    self.channelAllListCopy = res.data;
                    self.channelAllList = res.data;
                    if (self.popType == '添加角色') return;
                    return call(self.channelAllList);
                });
                function call(channelAllList) {
                    var channelAllListCopy = angular.copy(channelAllList);
                    self.channelList = self.sourceData.channelId != null ? self.sourceData.channelId.split(',') : [];
                    _.forEach(self.channelList, function (item, index) {
                        _.map(channelAllList, function (channel) {
                            if (channel.orderChannelId == item) {
                                self.channelList[index] = {
                                    'orderChannelId': item,
                                    'name': channel.name
                                }
                            }
                            return self.channelList;
                        });
                    });
                    channelCallback(channelAllListCopy);
                }

                function channelCallback(channelAllListCopy) {
                    self.channelAllList = [];
                    if (self.channelList.length == 0) {
                        self.channelAllList = channelAllListCopy;
                        return;
                    } else {
                        self.channelAllList = channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            self.data = _.find(self.channelAllList, function (channel) {
                                return channel.orderChannelId == item.orderChannelId;
                            });
                            self.channelAllList.splice(self.channelAllList.indexOf(self.data), 1);
                        });
                    }
                }

                self.storeService.getAllStore(null).then(function (res) {
                    self.storeAllListCopy = res.data;
                    self.storeAllList = res.data;
                    _.forEach(self.storeAllList, function (item) {
                        item.storeName = '(' + item.channelId + ')' + item.storeName;
                    });
                    if (self.popType == '添加角色') return;
                    return storeCall(self.storeAllList);
                });
                function storeCall(storeAllList) {
                    var storeAllListCopy = angular.copy(storeAllList);
                    self.storeList = self.sourceData.storeId != null ? self.sourceData.storeId.split(',') : [];
                    _.forEach(self.storeList, function (item, index) {
                        _.map(storeAllList, function (store) {
                            if (store.storeId == item) {
                                self.storeList[index] = {
                                    'storeId': item,
                                    'storeName': '(' + store.channelId + ')' + store.storeName
                                }
                            }
                            return self.storeList;
                        });
                    });
                    storeCallback(storeAllListCopy);
                }

                function storeCallback(storeAllListCopy) {
                    self.storeAllList = [];
                    if (self.storeList.length == 0) {
                        self.storeAllList = storeAllListCopy;
                    } else {
                        self.storeAllList = storeAllListCopy;
                        _.forEach(self.storeList, function (item) {
                            self.data = _.find(self.storeAllList, function (store) {
                                return store.storeId == item.storeId;
                            });
                            self.storeAllList.splice(self.storeAllList.indexOf(self.data), 1);
                        });
                    }
                }

                if (!self.sourceData.application)return;
                var appList = self.sourceData.application.split(',');
                _.forEach(appList, function (item) {
                    _.forEach(self.applicationList, function (i) {
                        if (i.application.toLocaleLowerCase() == item) {
                            i.valid = true;
                        }
                    })
                })
            },
            selected: function (item) {
                var self = this;
                self.selectedChannelId = item.orderChannelId;
                self.selectedStoreId = item.storeId;
            },
            search: function (item) {
                var self = this;
                self.channelTempAllList = [];
                self.storeTempAllList = [];
                switch (item.type) {
                    case'channel':
                        _.filter(self.channelAllList, function (data) {
                            if (data.name.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
                                self.channelTempAllList.push(data)
                            }
                        });
                        self.channelAllList = self.channelTempAllList;
                        break;
                    case'store':
                        _.filter(self.storeAllList, function (data) {
                            if (data.storeName.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
                                self.storeTempAllList.push(data)
                            }
                        });
                        self.storeAllList = self.storeTempAllList;
                        break;
                }
            },
            channelMove: function (type) {
                var self = this;
                self.channelList = self.channelList ? self.channelList : [];
                self.channelAllList = self.channelAllList ? self.channelAllList : [];
                switch (type) {
                    case '':
                        self.channelAllList = self.channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            var index = -1;
                            _.forEach(self.channelAllList, function (allItem, i) {
                                if (allItem.orderChannelId == item.orderChannelId) {
                                    index = i;
                                }
                            });
                            if (index > -1) self.channelAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.channelTempAllList) {
                            self.channelAllList = self.channelTempAllList;
                            _.forEach(self.channelAllList, function (item) {
                                self.channelList.push(item);
                            });
                            self.channelAllList = [];
                            break;
                        } else {
                            _.forEach(self.channelAllList, function (item) {
                                self.channelList.push(item);
                            });
                            self.channelAllList = [];
                            break;
                        }
                    case 'include':
                        self.data = _.find(self.channelAllList, function (channel) {
                            return channel.orderChannelId == self.selectedChannelId;
                        });
                        self.channelList.push(self.data);
                        self.channelAllList.splice(self.channelAllList.indexOf(self.data), 1);
                        break;
                    case 'exclude':
                        self.data = _.find(self.channelList, function (channel) {
                            return channel.orderChannelId == self.selectedChannelId;
                        });
                        self.channelAllList.push(self.data);
                        self.channelList.splice(self.channelList.indexOf(self.data), 1);
                        break;
                    case 'allExclude':
                        _.forEach(self.channelList, function (item) {
                            self.channelAllList.push(item);
                        });
                        self.channelList = [];
                        break;
                }
            },
            storeInit: function () {
                var self = this;
                if (self.channelList) {
                    _.forEach(self.channelList, function (item) {
                        self.search({'type': 'store', 'value': item.orderChannelId});
                    })
                }
            },
            storeMove: function (type) {
                var self = this;
                self.storeList = self.storeList ? self.storeList : [];
                self.storeAllList = self.storeAllList ? self.storeAllList : [];
                switch (type) {
                    case '':
                        self.storeAllList = self.storeAllListCopy;
                        _.forEach(self.storeList, function (item) {
                            var index = -1;
                            _.forEach(self.storeAllList, function (allItem, i) {
                                if (allItem.storeId == item.storeId) {
                                    index = i;
                                }
                            });
                            if (index > -1) self.storeAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.storeTempAllList) {
                            self.storeAllList = self.storeTempAllList;
                            _.forEach(self.storeAllList, function (item) {
                                self.storeList.push(item);
                            });
                            self.storeAllList = [];
                            break;
                        } else {
                            _.forEach(self.storeAllList, function (item) {
                                self.storeList.push(item);
                            });
                            self.storeAllList = [];
                            break;
                        }
                    case 'include':
                        self.data = _.find(self.storeAllList, function (store) {
                            return store.storeId == self.selectedStoreId;
                        });
                        self.storeList.push(self.data);
                        self.storeAllList.splice(self.storeAllList.indexOf(self.data), 1);
                        break;
                    case 'exclude':
                        self.data = _.find(self.storeList, function (store) {
                            return store.storeId == self.selectedStoreId;
                        });
                        self.storeAllList.push(self.data);
                        self.storeList.splice(self.storeList.indexOf(self.data), 1);
                        break;
                    case 'allExclude':
                        _.forEach(self.storeList, function (item) {
                            self.storeAllList.push(item);
                        });
                        self.storeList = [];
                        break;
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var selectedAppList = _.filter(self.applicationList, function (selectedApp) {
                    return selectedApp.valid;
                });
                self.sourceData.application = selectedAppList;

                self.sourceData.channelId = [];
                self.sourceData.storeId = [];
                if (self.sourceData.allChannel == '0') {
                    _.forEach(self.channelList, function (item) {
                        self.sourceData.channelId.push(item.orderChannelId);
                    });
                }
                if (self.sourceData.allStore == '0') {
                    _.forEach(self.storeList, function (item) {
                        self.sourceData.storeId.push(item.storeId);
                    });
                }


                var result = {};
                if (self.append == true) {
                    self.adminRoleService.addRole(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        var channelName = [], storeName = [];
                        _.forEach(self.channelList, function (item) {
                            channelName.push(item.name);
                        });
                        _.forEach(self.storeList, function (item) {
                            storeName.push(item.storeName);
                        });
                        self.sourceData.channelName = channelName.join(',');
                        self.sourceData.storeName = storeName.join(',');
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminRoleService.updateRole(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                }
            }
        };
        return AddRoleController;
    })())
});