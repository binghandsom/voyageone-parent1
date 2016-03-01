/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskBeatController", (function () {

        function TaskBeatController($routeParams, taskBeatService) {
            this.taskBeatService = taskBeatService;

            this.task_id = $routeParams['task_id'];
            this.data = [];
            this.pageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getData.bind(this)
            };
        }

        TaskBeatController.prototype = {
            init: function() {
                this.getData();
            },

            getData: function () {
                var ttt = this;
                var po = ttt.pageOption;
                var offset = (po.curr - 1) * po.size;
                ttt.taskBeatService.page({
                    task_id: ttt.task_id,
                    offset: offset,
                    size: ttt.pageOption.size
                }).then(function (res) {
                    ttt.data = res.data.list;
                    ttt.pageOption.total = res.data.total;
                })
            }
        };

        return TaskBeatController;

    })())
});