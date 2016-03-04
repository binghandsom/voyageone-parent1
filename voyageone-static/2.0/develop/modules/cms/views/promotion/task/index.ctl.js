/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskIndexController", (function () {
    
        function TaskIndexController(taskService) {
            this.taskService = taskService;
            
            this.tasks = [];
        }
    
        TaskIndexController.prototype = {
            init: function () {
                var ttt = this;
                ttt.taskService.page().then(function(res){
                    ttt.tasks = res.data;
                });
            },

            typeName: function(task) {
                switch (task.task_type) {
                    case 1:
                        return '价格披露';
                    default:
                        return '未知类型';
                }
            }
        };
            
        return TaskIndexController;
        
    })());
});
