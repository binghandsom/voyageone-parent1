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
            search:function(value){
            	var self = this;
                self.adminUserService.getAuthByUser({
                    'userAccount': self.sourceData[0].userAccount,
                    'application': value
                }).then(function (res) {
                    self.authList = res.data;
                })
            }
        };
        return authorityController;
    })())
});