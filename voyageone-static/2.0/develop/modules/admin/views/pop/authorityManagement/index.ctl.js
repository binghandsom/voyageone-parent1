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
                if (self.sourceData.length == 1) {
                    self.show = true;
                    self.adminUserService.getAuthByUser({
                        'userAccount': self.sourceData[0].userAccount,
                        'application': self.sourceData[0].application
                    }).then(function (res) {
                        self.authList = res.data;
                    })
                } else {
                    self.show = false;
                    var roleIds = [];
                    var application = [];
                    _.forEach(self.sourceData, function (item) {
                        roleIds.push(item.roleId);
                        application.push(item.application);
                    });
                    self.adminRoleService.getAuthByRoles({
                        'roleIds': roleIds,
                        'application': application
                    }).then(function (res) {
                        console.log(res);
                    });
                }
            }
        };
        return authorityController;
    })())
});