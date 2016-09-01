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
        }

        AddRoleController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加角色';
                    self.sourceData = {}
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });

                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                    if (self.popType == '添加角色') return;
                    return call(self.channelAllList);
                });
                function call(channelAllList) {
                    var channelAllListCopy = angular.copy(channelAllList);
                    self.channelList = self.sourceData.channelName.split(',');
                    _.forEach(self.channelList, function (item, index) {
                        _.map(channelAllList, function (channel) {
                            if (channel.name == item) {
                                self.channelList[index] = {
                                    'orderChannelId': channel.orderChannelId,
                                    'name': item
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
                    self.storeAllList = res.data;
                    if (self.popType == '添加角色') return;
                    return storeCall(self.storeAllList);
                });
                function storeCall(storeAllList) {
                    var storeAllListCopy = angular.copy(storeAllList);
                    self.storeList = self.sourceData.storeName.split(',');
                    _.forEach(self.storeList, function (item, index) {
                        _.map(storeAllList, function (store) {
                            if (store.storeName == item) {
                                self.storeList[index] = {'storeId': store.storeId, 'storeName': item}
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
                        return;
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
                            if (data.channelName.toUpperCase().indexOf(item.value.toUpperCase()) > -1) {
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
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllListCopy = res.data;
                });
                self.channelList = self.channelList ? self.channelList : [];
                self.channelAllList = self.channelAllList ? self.channelAllList : [];
                switch (type) {
                    case '':
                        self.channelAllList = self.channelAllListCopy;
                        _.forEach(self.channelList, function (item) {
                            var index = self.channelAllList.indexOf(item.orderChannelId);
                            if (index >= 0) self.channelAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.channelTempAllList) {
                            self.channelAllList = self.channelTempAllList;
                            _.extend(self.channelList, self.channelAllList);
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
            storeMove: function (type) {
                var self = this;
                self.storeService.getAllStore(null).then(function (res) {
                    self.storeAllListCopy = res.data;
                });
                self.storeList = self.storeList ? self.storeList : [];
                self.storeAllList = self.storeAllList ? self.storeAllList : [];
                switch (type) {
                    case '':
                        self.storeAllList = self.storeAllListCopy;
                        _.forEach(self.storeList, function (item) {
                            var index = self.storeAllList.indexOf(item.storeId);
                            if (index >= 0) self.storeAllList.splice(index, 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.storeTempAllList) {
                            self.storeAllList = self.storeTempAllList;
                            _.extend(self.storeList, self.storeAllList);
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
                if (self.sourceData.allChannel == '1' || self.sourceData.allStore == '1') {
                    self.sourceData.channelId = '';
                    self.sourceData.channelName = '';
                    self.sourceData.storeId = '';
                    self.sourceData.storeName = '';
                } else {
                    var tempChannelId = [];
                    var tempChannelName = [];
                    var tempStoreId = [];
                    var tempStoreName = [];
                    _.forEach(self.channelList, function (item) {
                        tempChannelId.push(item.orderChannelId);
                        tempChannelName.push(item.channelName);
                        _.extend(self.sourceData, {
                            'channelId': tempChannelId.join(','),
                            'channelName': tempChannelName.join(',')
                        });
                    });
                    _.forEach(self.storeList, function (item) {
                        tempStoreId.push(item.storeId);
                        tempStoreName.push(item.storeName);
                        _.extend(self.sourceData, {
                            'storeId': tempStoreId.join(','),
                            'storeName': tempStoreName.join(',')
                        });
                    });
                }
                var result = {};
                if (self.append == true) {
                    self.adminRoleService.addRole(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
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