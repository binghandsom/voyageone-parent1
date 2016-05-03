define([
    'cms'
], function (cms) {
    cms.controller('popNewBeatCtl', (function () {
        
        function PopNewBeatCtl(context, $uibModalInstance, taskBeatService, $location) {
            
            this.promotion = context;
            this.$uibModalInstance = $uibModalInstance;
            this.taskBeatService = taskBeatService;
            this.$location = $location;

            this.taskBean = {
                task_name: '',
                promotion_id: context.id,
                activity_start: context.activityStart,
                activity_end: context.activityEnd,
                config: {
                    need_vimage: false,
                    beat_template: '',
                    revert_template: '',
                    beat_vtemplate: '',
                    revert_vtemplate: ''
                }
            };
        }

        PopNewBeatCtl.prototype = {
            
            ok: function () {
                var ttt = this;
                ttt.taskBeatService.create(ttt.taskBean).then(function(res) {
                    var newBean = res.data;
                    ttt.$uibModalInstance.close(newBean);
                    ttt.$location.path('/promotion/task/beat/' + newBean.task_id);
                });
            },

            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };
        
        return PopNewBeatCtl;
    })());
});