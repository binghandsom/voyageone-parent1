/**
 * Created by sofia on 2016/10/17.
 */
define([
    'admin'
], function (admin) {
    admin.controller('RoleCopyController', (function () {
        function RoleCopyController(context, adminRoleService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.adminRoleService = adminRoleService;
            this.$uibModalInstance = $uibModalInstance;
            this.saveInfo = {
                roleId: this.sourceData.roleId,
                copyRoleName: ''
            }
        }

        RoleCopyController.prototype = {
            cancel: function () {
                this.$uibModalInstance.close();
            },
            save: function () {
                var self = this;
                self.adminRoleService.copyRole(self.saveInfo).then(function (res) {
                    console.log(res);
                })
            }
        };
        return RoleCopyController;
    })())
});