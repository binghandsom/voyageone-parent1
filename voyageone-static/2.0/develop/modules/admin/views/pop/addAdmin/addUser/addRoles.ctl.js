/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('AddRolesController', (function () {
        function AddRolesController(context, alert, adminRoleService, adminUserService, $uibModalInstance) {

            this.alert = alert;
            this.adminRoleService = adminRoleService;
            this.adminUserService = adminUserService;

            this.popType = '批量添加角色';

            this.$uibModalInstance = $uibModalInstance;
            this.saveInfo = {
                userIds: context
            };
            this.leftSelectedFlg = false;
            this.rightSelectedFlg = false;
        }

        AddRolesController.prototype = {
            init: function () {
                var self = this;

                self.sourceData = {};
                self.sourceData.roleIds = [];


                self.adminRoleService.getAllRole().then(function (res) {
                    self.allList = res.data;
                    self.roleAllListCopy = res.data;
                    self.roleList = [];
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
                _.filter(self.roleAllListCopy, function (data) {
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
                var result = {res: 'failure'};
                this.$uibModalInstance.close(result);
            },
            save: function () {
                var self = this;
                var result = {};
                var roleIds = [];
                // 设置roleIds
                _.forEach( self.roleList, function (role) {
                    roleIds.push(role.roleId);
                });

                self.saveInfo.roleIds = roleIds;

                _.extend(self.sourceData, self.saveInfo);

                self.adminUserService.addRoles(self.saveInfo).then(function (res) {
                    if (res.data == false) {
                        self.confirm(res.data.message);
                        return;
                    }
                    _.extend(result, {'res': 'success', 'sourceData': self.sourceData});
                    self.$uibModalInstance.close(result);
                })

            }
        };
        return AddRolesController;
    })())
});