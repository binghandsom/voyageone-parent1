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
                self.channelService.getAllChannel().then(function (res) {
                    self.allList = res.data;
                    self.channelAllList = [];
                    _.forEach(self.allList, function (item) {
                        self.channelAllList.push({'orderChannelId': item.orderChannelId, 'channelName': item.name});
                    });
                    if (self.popType == '添加角色') return;
                    return call(self.channelAllList);
                });
                function call(channelAllList) {
                    self.channelList = self.sourceData.channelName.split(',');
                    _.forEach(self.channelList, function (item, index) {
                        _.map(channelAllList, function (channel) {
                            if (channel.channelName == item) {
                                self.channelList[index] = {'orderChannelId': channel.orderChannelId, 'channelName': item}
                            }
                            return self.channelList;
                        })
                    });
                }

                self.storeService.getAllStore().then(function (res) {
                    self.List = res.data;
                    self.storeAllList = [];
                    _.forEach(self.List, function (item) {
                        self.storeAllList.push({'storeId': item.storeId, 'storeName': item.storeName});
                        return;
                    });
                    if (self.popType == '添加角色') return;
                    console.log(self.storeAllList)
                    return storeCall(self.storeAllList);
                });
                function storeCall(storeAllList) {
                    self.storeList = self.sourceData.storeName.split(',');
                    _.forEach(self.storeList, function (item, index) {
                        _.map(storeAllList, function (store) {
                            if (store.storeName == item) {
                                self.storeList[index] = {'storeId': store.storeId, 'storeName': item}
                            }
                            return self.storeList;
                        })
                    });
                }
            },
            selected: function (item) {
                var self = this;
                self.selectedChannelId = item.orderChannelId;
                self.selectedStoreId = item.storeId;
            },
            channelMove: function (type) {
                var self = this;
                self.roleList = self.roleList ? self.roleList : [];
                self.roleAllList = self.roleAllList ? self.roleAllList : [];
                switch (type) {
                    case 'allInclude':
                        _.extend(self.roleList, self.roleAllList);
                        self.roleAllList = null;
                        break;
                    case 'include':
                        self.data = _.find(self.roleAllList, function (role) {
                            return role.roleId == self.selectedRoleId;
                        });
                        self.roleList.push(self.data);
                        self.roleAllList.splice(self.roleAllList.indexOf(self.data), 1);
                        break;
                    case 'exclude':
                        self.data = _.find(self.roleList, function (role) {
                            return role.roleId == self.selectedRoleId;
                        });
                        self.roleAllList.push(self.data);
                        self.roleList.splice(self.roleList.indexOf(self.data), 1);
                        break;
                    case 'allExclude':
                        _.extend(self.roleAllList, self.roleList);
                        self.roleList = null;
                        break;
                }
            },
            storeMove: function (type) {
                var self = this;
                self.storeList = self.storeList ? self.storeList : [];
                self.storeAllList = self.storeAllList ? self.storeAllList : [];
                switch (type) {
                    case 'allInclude':
                        _.extend(self.storeList, self.storeAllList);
                        self.storeAllList = null;
                        break;
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
                        _.extend(self.storeAllList, self.storeList);
                        self.storeList = null;
                        break;
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
                if (self.append == true) {
                    self.adminUserService.addUser(self.sourceData).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminUserService.updateUser(self.sourceData).then(function (res) {
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