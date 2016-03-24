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
            this.taskId = "";
            this.subTaskId = $routeParams['sub_task_id'];
            this.taskStockIncrementDetailService = taskStockIncrementDetailService;
            this.hasAuthority = true;
            this.readyNum = 0;
            this.waitIncrementNum = 0;
            this.increasingNum = 0;
            this.incrementSuccessNum = 0;
            this.incrementFailureNum = 0;
            this.revertNum = 0;
            this.allNum = 0;

            this.model = "";
            this.code = "";
            this.sku = "";
            this.status = "";

            this.propertyList = [];
            this.cartId = "";
            this.cartName = "";

            this.stockPageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.search.bind(this)
            };

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
                    "subTaskId" : main.subTaskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "status" : main.status,
                    "cartId" : main.cartId,
                    "cartName" : main.cartName,
                    "propertyList" : main.propertyList,
                    "start1" :  0,
                    "length1" : 20
                }).then(function (res) {
                    //main.hasAuthority = res.data.hasAuthority;
                    //if (!main.hasAuthority) {
                    //    main.alert('没有权限访问！')
                    //    return;
                    //}
                    main.taskId = res.data.taskId;
                    main.readyNum = res.data.readyNum;
                    main.waitIncrementNum = res.data.waitIncrementNum;
                    main.increasingNum = res.data.increasingNum;
                    main.incrementSuccessNum = res.data.incrementSuccessNum;
                    main.incrementFailureNum = res.data.incrementFailureNum;
                    main.revertNum = res.data.revertNum;
                    main.allNum = res.data.allNum;
                    main.stockPageOption.total = res.data.allNum;
                    main.cartId = res.data.cartId;
                    main.cartName = res.data.cartName;
                    main.propertyList = res.data.propertyList;
                    main.stockList = res.data.stockList;
                    main.stockPageOption.curr = 1;
                })
            },

            download: function () {
                var main = this;
                $.download.post(main.downloadUrl, {
                    "taskId" : main.taskId,
                    "subTaskId" : main.subTaskId,
                    "propertyList": JSON.stringify(main.propertyList),
                    "cartId" : main.cartId,
                    "cartName" : main.cartName,
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