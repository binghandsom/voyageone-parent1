/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskStockIncrementController", (function () {

        function TaskStockIncrementController($routeParams, taskStockIncrementService, confirm, alert, notify) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = $routeParams['task_id'];
            this.subTaskName = "";
            this.cartId = "";
            this.platformList = [];
            this.taskList = [];
            this.taskStockIncrementService = taskStockIncrementService;
        }

        TaskStockIncrementController.prototype = {
            init: function () {
                this.getPlatFormList();
            },
            clear: function () {
                this.subTaskName = "";
                this.cartId = "";
            },
            getPlatFormList: function (status) {
                var main = this;
                main.taskStockIncrementService.getPlatFormList({
                    "taskId" : main.taskId
                }).then(function (res) {
                    //main.hasAuthority = res.data.hasAuthority;
                    //if (!main.hasAuthority) {
                    //    main.alert('没有权限访问！')
                    //    return;
                    //}
                    main.platformList = res.data.platformList;
                    main.search();
                })
            },

            delTask: function (subTaskId) {
                var main = this;
                main.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    main.taskStockIncrementService.delTask({
                        "taskId" : main.taskId,
                        "subTaskId": subTaskId
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_DELETE_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_DELETE_FAIL');
                        }
                    })
                })
            },

            search: function (status) {
                var main = this;
                main.taskStockIncrementService.searchTask({
                    "taskId" : main.taskId,
                    "subTaskName" : main.subTaskName,
                    "cartId" : main.cartId
                }).then(function (res) {
                    main.taskList = res.data.taskList;
                })
            }
        };

        return TaskStockIncrementController;

    })())
});