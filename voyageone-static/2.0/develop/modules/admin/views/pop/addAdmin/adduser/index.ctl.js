/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddUserController', (function () {
        function AddUserController(context, adminRoleService, adminOrgService, adminUserService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;

            this.popType = '修改用户';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
        }

        AddUserController.prototype = {
            init: function () {
                var self = this;
                if (self.sourceData == 'add') {
                    self.popType = '添加用户';
                    self.sourceData = {};
                    self.sourceData.active = '1';
                }
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.adminRoleService.getAllRole().then(function (res) {
                    self.allList = res.data;
                    self.roleAllList = [];
                    for (var item in self.allList) {
                        self.roleAllList.push({'roleId': item, 'roleName': self.allList[item]});
                    }
                    if (self.popType == '添加用户') return;
                    return call(self.roleAllList);
                });
                function call(roleAllList) {
                    self.roleList = self.sourceData.roleId.split(',');
                    _.forEach(self.roleList, function (item, index) {
                        _.map(roleAllList, function (role) {
                            if (role.roleId == item) {
                                self.roleList[index] = {'roleId':item, 'roleName':  role.roleName}
                            }
                            return self.roleList;
                        })
                    });
                    callback(roleAllList);
                }

                function callback(roleAllList) {
                    self.roleAllList = [];
                    if (self.roleList.length == 0) {
                        self.roleAllList = roleAllList;
                        return;
                    } else {
                        self.roleAllList = roleAllList;
                        _.forEach(self.roleList, function (item) {
                            self.data = _.find(self.roleAllList, function (role) {
                                return role.roleId == item.roleId;
                            });
                            self.roleAllList.splice(self.roleAllList.indexOf(self.data), 1);
                        });
                    }
                }

            },
            selected: function (item) {
                var self = this;
                self.selectedRoleId = item.roleId;
            },
            search: function (item) {
                var self = this;
                self.allList = [];
                _.filter(self.roleAllList, function (data) {
                    if (data.roleName.toUpperCase().indexOf(item.toUpperCase()) > -1) {
                        self.allList.push(data)
                    }
                });
                self.roleAllList = self.allList;
            },
            move: function (type) {
                var self = this;
                self.adminRoleService.getAllRole().then(function (res) {
                    self.ttroleList = res.data;
                    self.roleAllListCopy = [];
                    for (var item in self.ttroleList) {
                        self.roleAllListCopy.push({'roleId': item, 'roleName': self.ttroleList[item]});
                    }
                });
                self.roleList = self.roleList ? self.roleList : [];
                self.roleAllList = self.roleAllList ? self.roleAllList : [];
                switch (type) {
                    case '':
                        self.roleAllList = self.roleAllListCopy;
                        _.forEach(self.roleList, function (item) {
                            self.data = _.find(self.roleAllList, function (role) {
                                return role.roleId == item.roleId;
                            });
                            self.roleAllList.splice(self.roleAllList.indexOf(self.data), 1);
                        });
                        break;
                    case 'allInclude':
                        if (self.allList) {
                            self.roleAllList = self.allList;
                            _.forEach(self.roleAllList, function (item) {
                                self.roleList.push(item);
                            });
                            self.roleAllList = [];
                            break;
                        } else {
                            _.forEach(self.roleAllList, function (item) {
                                self.roleList.push(item);
                            });
                            self.roleAllList = [];
                            break;
                        }
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
                        _.forEach(self.roleList, function (item) {
                            self.roleAllList.push(item);
                        });
                        self.roleList = [];
                        break;
                }
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var tempRoleList = [];
                var tempRoleName = [];
                var result = {};
                // 设置roleIds
                _.forEach(self.roleList, function (item) {
                    tempRoleList.push(item.roleId);
                    tempRoleName.push(item.roleName);
                    _.extend(self.sourceData, {'roleId': tempRoleList.join(','), 'roleName': tempRoleName.join(',')});
                });
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
        return AddUserController;
    })())
});