define([
    'cms',
    'underscore'
], function (cms, _) {
    cms.controller('popNewBeatCtl', (function () {

        function PopNewBeatCtl(context, $filter, $uibModalInstance, taskBeatService, $location) {

            var self = this;
            var task = context.task;
            var promotion = context.promotion;

            if (!promotion && task) {
                self.isEdit = true;
                promotion = task.promotion;
            }

            self.promotion = promotion;
            self.$uibModalInstance = $uibModalInstance;
            self.taskBeatService = taskBeatService;
            self.$location = $location;

            // 将字符串日期转换为 Date 日期。
            // 因为 input type=date 后, ng-model 只接受 Date 类型
            // 否则会报错

            if (task) {
                task.activityStart = new Date(task.activityStart);
                task.activityEnd = new Date(task.activityEnd);
                self.taskBean = task;
                if (_.isString(task.config))
                    task.config = JSON.parse(task.config);
            } else {
                self.taskBean = task || {
                        taskName: '',
                        promotionId: promotion.id,
                        activityStart: new Date(promotion.activityStart),
                        activityEnd: new Date(promotion.activityEnd),
                        config: {
                            need_vimage: false,
                            beat_template: null,
                            revert_template: null,
                            beat_vtemplate: null,
                            revert_vtemplate: null
                        }
                    };
            }

            taskBeatService.getTemplates({promotionId: promotion.id}).then(function (res) {
                self.templates = res.data;
            });

            self.formatDate = function (date) {
                return $filter('date')(date, 'yyyy-MM-dd');
            }
        }

        PopNewBeatCtl.prototype = {

            ok: function () {

                var self = this;
                var task = angular.copy(self.taskBean);
                var start = task.activityStart;
                var end = task.activityEnd;

                task.promotion = null;

                // 确保日期格式正确
                // 如果日期控件不选择的话, 则输出的将是 Date 格式
                if (_.isDate(start))
                    task.activityStart = self.formatDate(start);

                if (_.isDate(end))
                    task.activityEnd = self.formatDate(end);

                if (self.isEdit)
                    task.update = true;

                self.taskBeatService.create(task).then(function (res) {
                    var newBean = res.data;
                    self.$uibModalInstance.close(newBean);
                    self.$location.path('/promotion/task/beat/' + newBean.id);
                });
            },

            cancel: function () {
                this.$uibModalInstance.dismiss();
            }
        };

        return PopNewBeatCtl;
    })());
});