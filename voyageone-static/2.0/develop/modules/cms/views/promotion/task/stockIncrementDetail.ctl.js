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
            this.taskId = "";

            this.stockPageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getStockList.bind(this)
            };

            this.downloadUrl = urls.root + "/" + urls.exportStockInfo;
        }

        TaskStockIncrementDetailController.prototype = {
            init: function () {
                this.search('search');
            },

            getStockList: function () {
                this.search('page');
            },

            search: function (param) {
                var main = this;
                if (param != 'search' && param != 'page')  {
                    main.status = param;
                }
                if (param == 'search') {
                    main.status = "";
                }
                if (main.status >= '0' && main.status <= '6') {
                    var allNumLabel = document.getElementById('allNum');
                    allNumLabel.setAttribute("class", "btn btn-default-vo");
                }
                var start = 0;
                var page =  false;
                if (param == 'page') {
                    start = (main.stockPageOption.curr - 1) * main.stockPageOption.size;
                    page = true;
                }
                main.taskStockIncrementDetailService.searchItem({
                    "taskId" : main.taskId,
                    "subTaskId" : main.subTaskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "status" : main.status,
                    "page" : page,
                    "propertyList" : main.propertyList,
                    "start" :  start,
                    "length" : main.stockPageOption.size
                }).then(function (res) {
                    if (param != 'page') {
                        main.hasAuthority = res.data.hasAuthority;
                        if (!main.hasAuthority) {
                            main.alert('没有权限访问！')
                            return;
                        }
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
                    } else {
                        main.stockList = res.data.stockList;
                    }
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