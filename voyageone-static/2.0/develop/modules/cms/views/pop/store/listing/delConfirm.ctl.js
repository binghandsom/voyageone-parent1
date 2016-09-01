/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'cms'
], function (cms) {

    cms.controller("popDelConfirmCtl", (function () {

        function DelConfirm($uibModalInstance,context, notify ,imageGroupService,sizeChartService) {
            this.$uibModalInstance = $uibModalInstance;
            this.context = context;
            this.notify = notify;
            this.imageGroupService = imageGroupService;
            this.sizeChartService = sizeChartService;
        }

        DelConfirm.prototype.init = function () {
            this.context.flag = false;
        };

        DelConfirm.prototype.confirm = function () {
            var self = this,
                context = self.context,
                imageGroupService = self.imageGroupService,
                $uibModalInstance = self.$uibModalInstance,
                sizeChartService = self.sizeChartService;

            var upEntity = {
                imageGroupId: context.imageGroupId,
                sizeChartId:context.sizeChartId,
                isDelSizeChart:!!context.flag,
                isDelImageGroup:!!context.flag
            };

            if(context.from == 'image'){
                imageGroupService.delete(upEntity).then(function () {
                    $uibModalInstance.close(true);
                },function(err){
                    $uibModalInstance.close(err);
                });
            }else{
                sizeChartService.delete(upEntity).then(function () {
                    $uibModalInstance.close(true);
                });
            }

        };

        return DelConfirm;
    })());
});