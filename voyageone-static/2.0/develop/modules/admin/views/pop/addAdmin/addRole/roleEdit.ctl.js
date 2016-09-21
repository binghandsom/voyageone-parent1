/**
 * Created by sofia on 2016/9/18.
 */
define([
    'admin',
    'modules/admin/controller/treeTable.ctrl'
], function (admin) {
    admin.controller('RoleEditController', (function () {
        function RoleEditController(context, alert, adminRoleService, adminUserService, $uibModalInstance) {
            this.alert = alert;
            this.sourceData = context ? context : {};
            this.adminRoleService = adminRoleService;
            this.adminUserService = adminUserService;
            this.popType = '分配权限';
            this.$uibModalInstance = $uibModalInstance;
            this.selectedList = [];
            this.flatResList = [];
            this.saveInfo = {
                application: ''
            };
            this.applicationList = [
                {'id': 1, 'application': 'Admin', 'valid': false},
                {'id': 2, 'application': 'CMS', 'valid': false},
                {'id': 3, 'application': 'OMS', 'valid': false},
                {'id': 4, 'application': 'WMS', 'valid': false}
            ];
        }

        RoleEditController.prototype = {
            init: function (app) {
                var self = this;
                var getInfo = self.sourceData._selall == true ? self.sourceData : {
                    'roleIds': self.sourceData.roleIds,
                    'application': app ? app : (self.sourceData.application ? self.sourceData.application : "admin")
                };
                self.adminRoleService.getAuthByRoles({getInfo}).then(function (res) {
                    self.resList = res.data.res;
                    self.permsStatus = res.data.perms;
                    _.forEach(self.applicationList, function (item) {
                        _.map(self.permsStatus, function (ps) {
                            if (ps.application === item.application.toLocaleLowerCase()) {
                                item.selected = ps.selected;
                                item.selected !== 2 ? item.selected == 1 ? item.valid = true : item.valid = false : item.selected = 2;
                            }
                        })
                    })
                });
                switch (self.sourceData.type) {
                    case 'delete':
                        return self.popType = '删除权限';
                        break;
                    case 'add':
                        return self.popType = '新增权限';
                        break;
                }
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
            },
            changeApp: function (app) {
                var self = this;
                self.init(app);
            },
            save: function () {
                var self = this;
                _.extend(saveInfo, {resIds: [], applications: [], roleIds: self.sourceData.roleIds});
                _.filter(self.applicationList, function (e) {
                    return e.valid;
                }).forEach(function (e) {
                    saveInfo.applications.push(e.application.toLocaleLowerCase());
                });
                _.filter(self.selectedList, function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    saveInfo.resIds.push(item.id);
                    if (saveInfo.applications.indexOf(item.application) < 0) {
                        saveInfo.applications.push(item.application);
                    }
                });
                switch (self.sourceData.type) {
                    case 'set':
                        self.adminRoleService.setAuth(saveInfo).then(function (res) {
                            if (res.data == true) {
                                self.$uibModalInstance.close();
                            }
                        });
                        break;
                    case 'delete':
                        self.adminRoleService.removeAuth(saveInfo).then(function (res) {
                            if (res.data == true) {
                                self.$uibModalInstance.close();
                            }
                        });
                        break;
                    case 'add':
                        self.adminRoleService.addAuth(saveInfo).then(function (res) {
                            if (res.data == true) {
                                self.$uibModalInstance.close();
                            }
                        });
                        break;
                }
            },
            getResType: function (type) {
                switch (type) {
                    case 0:
                        return '系统';
                        break;
                    case 1:
                        return '菜单';
                        break;
                    case 2:
                        return 'Action';
                        break;
                }
            }
        };
        return RoleEditController;
    })())
});