/**
 * Created by sofia on 2016/8/29.
 */
define([
    'admin',
    'modules/admin/controller/treeTable.ctrl'
], function (admin) {
    admin.controller('authorityController', (function () {
        function authorityController(context, adminOrgService, adminRoleService, adminUserService) {
            this.sourceData = context;
            this.adminOrgService = adminOrgService;
            this.adminRoleService = adminRoleService;
            this.adminUserService = adminUserService;
            this.flatResList = [];
        }

        authorityController.prototype = {
            init: function () {
                var self = this;
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
            },
            search: function (value) {
                var self = this;
                self.adminUserService.getAuthByUser({
                    'userAccount': self.sourceData[0].userAccount,
                    'application': value != null ? value : ''
                }).then(function (res) {
                    self.authList = res.data;
                })
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
        return authorityController;
    })())
});