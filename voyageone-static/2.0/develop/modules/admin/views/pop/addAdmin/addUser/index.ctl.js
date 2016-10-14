/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddUserController', (function () {
        function AddUserController(context, alert, adminRoleService, channelService, adminOrgService, adminUserService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.alert = alert;
            this.adminRoleService = adminRoleService;
            this.channelService = channelService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;

            this.popType = '修改用户';
            this.companyId = this.sourceData.companyId;
            this.$uibModalInstance = $uibModalInstance;
            this.saveInfo = {
                id: this.sourceData.id,
                userAccount: this.sourceData.userAccount,
                userName: this.sourceData.userName,
                orgId: this.sourceData.orgId,
                email: this.sourceData.email,
                active: this.sourceData.active,
                description: this.sourceData.description,
                companyId: this.sourceData.companyId + ''
            };
            this.leftSelectedFlg = false;
            this.rightSelectedFlg = false;
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

                self.channelService.getAllCompany().then(function (res) {
                    self.companyList = res.data;
                });
                self.adminRoleService.getAllRole().then(function (res) {
                    self.allList = res.data;
                    self.roleAllListCopy = res.data;
                    self.roleList = self.sourceData.roleId != null ? self.sourceData.roleId.split(',') : [];
                    _.forEach(self.roleList, function (item, index) {
                        _.map(self.allList, function (role) {
                            if (role.roleId == item) {
                                self.roleList[index] = {'roleId': item, 'roleName': role.roleName}
                            }
                            return self.roleList;
                        })
                    });
                    callback(self.allList);
                });
                function callback(roleAllList) {
                    self.roleAllList = [];
                    if (self.roleList.length == 0) {
                        self.roleAllList = roleAllList;
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
                if (item.value) {
                    self.selectedRoleId = item.value.roleId;
                    item.direction == 'left' ? self.leftSelectedFlg = true : self.rightSelectedFlg = true;
                } else {
                    item.direction == 'left' ? self.leftSelectedFlg = false : self.rightSelectedFlg = false;
                }
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
                        if (self.rightSelectedFlg == true) {
                            self.data = _.find(self.roleAllList, function (role) {
                                return role.roleId == self.selectedRoleId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在可选择角色区 选择角色后再点此按钮!');
                                return;
                            } else {
                                self.roleList.push(self.data);
                                self.roleAllList.splice(self.roleAllList.indexOf(self.data), 1);
                                self.selectedRoleId = '';
                                break;
                            }
                        } else {
                            self.alert('请在可选择角色区 选择角色后再点此按钮!');
                            return;
                        }
                    case 'exclude':
                        if (self.leftSelectedFlg == true) {
                            self.data = _.find(self.roleList, function (role) {
                                return role.roleId == self.selectedRoleId;
                            });
                            if (self.data == undefined) {
                                self.alert('请在已选择角色区 选择角色后再点此按钮!');
                                return;
                            } else {
                                self.roleAllList.push(self.data);
                                self.roleList.splice(self.roleList.indexOf(self.data), 1);
                                self.selectedRoleId = '';
                                break;
                            }
                        } else {
                            self.alert('请在已选择角色区 选择角色后再点此按钮!');
                            return;
                        }
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
                    _.extend(self.saveInfo, {'roleId': tempRoleList.join(','), 'roleName': tempRoleName.join(',')});
                });
                self.saveInfo.companyId = self.saveInfo.companyId ? self.saveInfo.companyId - 0 : '';
                _.extend(self.sourceData, self.saveInfo);
                if (self.append == true) {
                    self.adminUserService.addUser(self.saveInfo).then(function (res) {
                        if (res.data == false) {
                            self.confirm(res.data.message);
                            return;
                        }
                        _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                        self.$uibModalInstance.close(result);
                    })
                } else {
                    self.adminUserService.updateUser(self.saveInfo).then(function (res) {
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