/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ViewLogController', (function () {
        function ViewLogController(context, adminLogService, $uibModalInstance) {
            this.sourceData = context ? context : {};
            this.adminLogService = adminLogService;

            this.popType = '日志详情';
            this.$uibModalInstance = $uibModalInstance;
        }

        ViewLogController.prototype = {
            init: function () {
                var self = this;
                self.adminLogService.getLogDetail({'logId':self.sourceData.id}).then(function (res) {
                    self.logList = res.data;
                });
            }
        };
        return ViewLogController;
    })())
});