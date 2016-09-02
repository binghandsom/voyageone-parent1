/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'cms'
], function (cms) {

    cms.controller("ApproveConfirmController", (function () {

        function ApproveConfirm($uibModalInstance,context, notify ,imageGroupService,sizeChartService) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.notify = notify;
            this.imageGroupService = imageGroupService;
            this.sizeChartService = sizeChartService;
        }

        ApproveConfirm.prototype.init = function () {
            this.context.flag = false;
        };


        return ApproveConfirm;
    })());
});