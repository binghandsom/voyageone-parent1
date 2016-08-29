/**
 * Created by sofia on 2016/8/29.
 */
define([
    'admin',
    'modules/admin/controller/treeTable.ctrl'
], function (admin) {
    admin.controller('authorityController', (function () {
        function authorityController(context, adminRoleService, adminOrgService, adminUserService, $uibModalInstance) {
            this.sourceData = context;
            this.adminRoleService = adminRoleService;
            this.adminOrgService = adminOrgService;
            this.adminUserService = adminUserService;

            this.$uibModalInstance = $uibModalInstance;
        }

        authorityController.prototype = {
            init: function () {
                var self = this;
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
            },
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                var result = {};
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
        return authorityController;
    })())
});