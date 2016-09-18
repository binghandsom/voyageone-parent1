/**
 * Created by sofia on 2016/9/18.
 */
define([
    'admin',
    'modules/admin/controller/treeTable.ctrl'
], function (admin) {
    admin.controller('RoleEditController', (function () {
        function RoleEditController(context, adminRoleService, adminOrgService, adminUserService, channelService, storeService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.append = context == 'add' ? true : false;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;
            this.channelService = channelService;
            this.storeService = storeService;
            this.popType = '分配权限';
            this.$uibModalInstance = $uibModalInstance;
            this.selectedList = [];
            this.applicationList = [
                {'id': 1, 'application': 'Admin', 'valid': false},
                {'id': 2, 'application': 'CMS', 'valid': false},
                {'id': 3, 'application': 'OMS', 'valid': false},
                {'id': 4, 'application': 'WMS', 'valid': false}
            ];
        }

        RoleEditController.prototype = {
            init: function () {
                var self = this;
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                if (self.sourceData.roleIds.length == 1) {
                    self.adminUserService.getAuthByUser({
                        'userAccount': self.sourceData[0].userAccount,
                        'application': self.sourceData[0].application
                    }).then(function (res) {
                        self.resList = res.data;
                    })
                } else {
                    self.adminRoleService.getAuthByRoles({
                        'roleIds': self.sourceData.roleIds,
                        'application': "admin"
                    }).then(function (res) {
                        self.resList = res.data.res;
                        console.log(self.resList)
                    });
                }
                switch (self.sourceData.type) {
                    case 'delete':
                        return self.popType = '删除权限';
                        break;
                    case 'add':
                        return self.popType = '新增权限';
                        break;
                }
            },
            save: function () {
                var self = this;
                var saveInfo = {resIds: [], applications: [], roleIds: self.sourceData.roleIds};
                _.filter(self.selectedList, function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    saveInfo.resIds.push(item.id);
                    saveInfo.applications.push(item.application);
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
                            console.log(res);
                            if (res.data == true) {
                                self.$uibModalInstance.close();
                            }
                        });
                        break;
                    case 'add':
                        self.adminRoleService.addAuth(saveInfo).then(function (res) {
                            console.log(res);
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