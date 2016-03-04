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

            this.searchInfo={};

            this.taskType=[{"name":"特价宝","value":"0"},{"name":"价格披露","value":"1"}]
        }
    
        TaskIndexController.prototype = {
            init: function () {
                var ttt = this;
                ttt.taskService.page(ttt.searchInfo).then(function(res){
                    ttt.tasks = res.data;
                });
            },

            typeName: function(task) {
                switch (task.task_type) {
                    case 0:
                        return '特价宝';
                    case 1:
                        return '价格披露';
                    default:
                        return '未知类型';
                }
            },
            search: function(){
                this.taskService.page(this.searchInfo).then(function(res){
                    this.tasks = res.data;
                });
            }
        };
            
        return TaskIndexController;
        
    })());
});
