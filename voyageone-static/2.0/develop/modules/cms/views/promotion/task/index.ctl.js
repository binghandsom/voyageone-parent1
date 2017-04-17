/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskIndexController", (function () {
    
        function TaskIndexController(taskService, taskStockService, cActions, confirm, notify) {
            this.taskService = taskService;
            this.taskStockService = taskStockService;
            var urls = cActions.cms.task.taskStockService;
            this.tasks = [];
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo={};

            this.taskType=[{"name":"特价宝","value":"0"},{"name":"价格披露","value":"1"},{"name":"库存隔离","value":"2"}];
            this.datePicker = [];

            this.downloadUrl = urls.root + "/" + urls.exportErrorInfo;
        }
    
        TaskIndexController.prototype = {
            init: function () {
                var self = this;
                self.taskService.page(self.searchInfo).then(function(res){
                    self.tasks = res.data;
                });
            },

            typeName: function(task) {
                switch (task.taskType) {
                    case 0:
                        return '特价宝';
                    case 1:
                        return '价格披露';
                    case 2:
                        return '库存隔离';
                    default:
                        return '未知类型';
                }
            },
            search: function(){
                var self = this;
                this.taskService.page(this.searchInfo).then(function(res){
                    self.tasks = res.data;
                });
            },
            clear: function(){
                this.searchInfo={};
            },
            delete: function(task){
                var self = this;
                self.confirm('TXT_MSG_DO_DELETE').then(function () {
                    // 库存隔离
                    if (task.taskType == '2') {
                        self.taskStockService.delTask({
                            "taskId" : task.id
                        }).then(function (res) {
                            self.notify.success('TXT_MSG_DELETE_SUCCESS');
                            self.search();
                        }, function (err) {
                            if (err.displayType == null) {
                                self.alert('TXT_MSG_DELETE_FAIL');
                            }
                        })
                    }
                })
            },
            download: function (taskId) {
                var main = this;
                $.download.post(main.downloadUrl, {
                    "taskId" : taskId
                });
            }
        };
            
        return TaskIndexController;
        
    })());
});
