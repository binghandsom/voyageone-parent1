/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl'
], function (cms, carts) {
    cms.controller("taskIndexController", (function () {
    
        function TaskIndexController(taskService, taskStockService, taskJiagepiluService, promotionService, cActions, confirm, notify, popups, $translate, alert,$location) {
            this.taskService = taskService;
            this.taskStockService = taskStockService;
            this.promotionService = promotionService;
            this.taskJiagepiluService = taskJiagepiluService;
            this.$location = $location;
            var urls = cActions.cms.task.taskStockService;
            this.tasks = [];
            this.confirm = confirm;
            this.notify = notify;
            this.popups = popups;
            this.$translate = $translate;
            this.alert = alert;
            this.searchInfo = {status: "0"};

            this.taskType = [{"name": "特价宝", "value": "0"}, {"name": "价格披露", "value": "1"}, {"name": "库存隔离", "value": "2"}];
            this.taskStatus = [{"name": "Open", "value": 0}, {"name": "Close", "value": 1}];

            this.datePicker = [];

            this.downloadUrl = urls.root + "/" + urls.exportErrorInfo;
            this.platformTypeList = [];
            this.platformStatus = {};
        }

        TaskIndexController.prototype = {
            init: function () {
                var self = this;
                self.promotionService.init().then(function (res) {
                    self.platformTypeList = res.data.platformTypeList;
                    self.taskService.page(self.searchInfo).then(function(res){
                        self.tasks = res.data;
                    });
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

            cartName: function(cartId) {
                return carts.valueOf(cartId).desc;

            },

            search: function(){
                var self = this;
                var cartIds = [];
                _.each(self.platformStatus,function (value, key) {
                    if(value){
                        cartIds.push(key);
                    }
                });
                this.searchInfo.cartIds = cartIds;
                this.taskService.page(this.searchInfo).then(function(res){
                    self.tasks = res.data;
                });
            },

            clear: function(){
                this.searchInfo={};
            },

            updateStatus: function (task, status) {
                var self = this;
                if (status == 1) {
                    self.confirm("是否关闭活动%s".replace("%s", task.taskName)).then(function () {
                        self.taskService.updateStatus({taskId: task.id, status: 1}).then(function (res) {
                            task.status = 1;
                            self.notify.success('TXT_MSG_SET_SUCCESS');
                        });
                    });
                } else {
                    self.confirm("是否打开任务%s".replace("%s", task.taskName)).then(function () {
                        self.taskService.updateStatus({taskId: task.id, status: 0}).then(function (res) {
                            task.status = 0;
                            self.notify.success('TXT_MSG_SET_SUCCESS');
                        });
                    });
                }

                self.taskService.page(self.searchInfo).then(function (res) {
                    self.tasks = res.data;
                });
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
            },

            // 启动/停止/还原所有
            controlAll: function (taskId, flag) {

                var self = this;
                self.confirm("确定要操作所有吗?")
                    .then(function () {
                        self.$controlAll(true, flag, taskId);
                    });
                return;
            },
            $controlAll: function (force, flag, taskId) {
                var self = this;

                self.taskJiagepiluService.operateProduct({
                    task_id: taskId,
                    force: force,
                    flag: flag
                }).then(function (res) {
                    if (!res.data){
                        self.notify.success('TXT_MSG_UPDATE_FAIL');
                    }else {
                        self.notify.success('TXT_MSG_SET_SUCCESS');
                    }
                });
            },

            addOrUpdateTask: function (task) {
                var self = this;
                self.popups.openNewBeatTask({task: task, platformTypeList:self.platformTypeList}).then(function(newTask) {
                    // self.task = newTask;
                    if (task) {
                        // 编辑，当task不存在也就是新增后页面直接在pop处跳转了
                        self.search();
                    } else {
                        self.$location.path('/task/jiagepilu/detail/' + newTask.id);
                    }

                });
            },

            delTask: function (task) {
                var self = this;
                if (task.taskType == 1) {
                    self.confirm(self.$translate.instant('TXT_DELETE_JIAGEPILU_TASK').replace("%s", task.taskName)).then(function () {
                        self.taskJiagepiluService.getSummary({task_id:task.id}).then(function (resp) {
                            if (resp.data) {
                                var summary = resp.data;
                                var notDeleted = _.find(summary, function (item) {
                                    return item.flag == "SUCCESS" || item.flag == "RE_FAIL";
                                });
                                if (notDeleted) {
                                    var mesage = "有状态为 <" +　self.$translate.instant('SUCCESS')　+ " 或 " + self.$translate.instant('RE_FAIL') + "> 的商品，不能删除任务";
                                    self.alert(mesage);
                                } else {
                                    self.taskJiagepiluService.deleteJiagepiluTask({task_id:task.id}).then(function (res) {
                                        if (res.data) {
                                            self.notify.success("Delete successfully");
                                            self.search();
                                        }
                                    });
                                }
                            }
                        })
                    });
                }
            }
        };

        return TaskIndexController;
    })());
});
