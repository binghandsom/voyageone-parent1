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
                    self.sourceData = {}
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
                    self.roleList = self.sourceData.roleName.split(',');
                    _.forEach(self.roleList, function (item, index) {
                        _.map(roleAllList, function (role) {
                            if (role.roleName == item) {
                                self.roleList[index] = {'roleId': role.roleId, 'roleName': item}
                            }
                            return self.roleList;
                        })
                    });
                }

            },
            selected: function (item) {
                var self = this;
                self.selectedRoleId = item.roleId;
            },
            move: function (type) {
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
                    _.extend(self.sourceData, {'roleId': tempRoleList.join(','),'roleName': tempRoleName.join(',')});
                });
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
        return AddUserController;
    })())
});