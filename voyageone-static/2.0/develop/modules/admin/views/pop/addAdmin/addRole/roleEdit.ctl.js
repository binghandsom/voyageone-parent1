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
            this.hasAllAuth = false;
        }

        RoleEditController.prototype = {
            init: function (app) {
                var self = this;
                self.hasAllAuth = false;
                var getInfo = self.sourceData;
                getInfo.application = app ? app : '';
                self.adminRoleService.getAuthByRoles(getInfo).then(function (res) {
                    self.resList = res.data.res;
                    self.permsStatus = res.data.perms;
                    _.map(self.permsStatus, function (ps) {
                        if (ps.application === self.saveInfo.application && ps.selected == 1) {
                            self.hasAllAuth = true;
                        }
                    })
                });
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                switch (self.sourceData.type) {
                    case 'delete':
                        self.popType = '删除权限';
                        break;
                    case 'add':
                        self.popType = '新增权限';
                        break;
                }
            },
            changeApp: function (app) {
                var self = this;
                self.selectedList = [];
                self.init(app);
            },
            save: function () {
                var self = this, saveInfo = {};
                _.extend(saveInfo, {applications: [], roleIds: self.sourceData.roleIds, resIds: [], hasAllAuth: false});
                saveInfo.applications.push(self.saveInfo.application);
                saveInfo.hasAllAuth = self.hasAllAuth == true ? true : false;
                _.filter(self.selectedList, function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    saveInfo.resIds.push(item.id);
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