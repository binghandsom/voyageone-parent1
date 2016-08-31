/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ViewLogController', (function () {
        function ViewLogController(context, adminOrgService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.adminOrgService = adminOrgService;

            this.popType = '日志详情';
            this.$uibModalInstance = $uibModalInstance;
        }

        ViewLogController.prototype = {
            init: function () {
                var self = this;
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });

            }
        };
        return ViewLogController;
    })())
});