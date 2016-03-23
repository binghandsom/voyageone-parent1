/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskStockIncrementDetailController", (function () {

        function TaskStockIncrementDetailController($routeParams, taskStockIncrementDetailService, cActions, confirm, alert, notify) {
            var urls = cActions.cms.task.taskStockIncrementDetailService;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = $routeParams['task_id'];
            this.taskStockIncrementDetailService = taskStockIncrementDetailService;

            this.model = "";
            this.code = "";
            this.sku = "";
            this.status = "";

            this.propertyList = [];
            this.platformList = {};

            this.downloadUrl = urls.root + "/" + urls.exportStockInfo;
        }

        TaskStockIncrementDetailController.prototype = {
            init: function () {
                this.search();
            },

            search: function (status) {
                var main = this;

                main.taskStockIncrementDetailService.searchItem({
                    "taskId" : main.taskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "status" : main.status,
                    "propertyList" : main.propertyList,
                    "platformList" : main.platformList,
                }).then(function (res) {
                    main.propertyList = res.data.propertyList;
                    main.platformList = res.data.platformList;
                })
            },

            download: function () {
                var main = this;
                $.download.post(main.downloadUrl, {
                    task_id: main.taskId,
                    propertyList: JSON.stringify(main.propertyList),
                    platformList: JSON.stringify(main.platformList),
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "status" : main.status
                });
            }
        };

        return TaskStockIncrementDetailController;

    })())
});