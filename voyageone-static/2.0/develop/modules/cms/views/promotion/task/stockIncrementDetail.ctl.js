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
            //this.hasAuthority = true;
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
            this.allNumClass = "btn btn-default-vo btn-vo";

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
                this.search('init');
            },

            getStockList: function () {
                this.search('page');
            },

            clear: function () {
                this.model = "";
                this.code = "";
                this.sku = "";
                _.each(this.propertyList, function (property) {
                    property.value = "";
                });
            },

            search: function (param) {
                var main = this;
                if (param >= '0' && param <= '6')  {
                    main.status = param;
                    main.allNumClass = "btn btn-default-vo";
                    //var allNumLabel = document.getElementById('allNum');
                    //allNumLabel.setAttribute("class", "btn btn-default-vo");
                }
                if (param == 'init' || param == '') {
                    main.status = "";
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
                    "cartId" : main.cartId,
                    "cartName" : main.cartName,
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
                        //main.hasAuthority = res.data.hasAuthority;
                        //if (!main.hasAuthority) {
                        //    main.alert('没有权限访问！')
                        //    return;
                        //}
                        main.taskId = res.data.taskId;
                        main.cartId = res.data.cartId;
                        main.cartName = res.data.cartName;
                        main.readyNum = res.data.readyNum;
                        main.waitIncrementNum = res.data.waitIncrementNum;
                        main.increasingNum = res.data.increasingNum;
                        main.incrementSuccessNum = res.data.incrementSuccessNum;
                        main.incrementFailureNum = res.data.incrementFailureNum;
                        main.revertNum = res.data.revertNum;
                        main.allNum = res.data.allNum;
                        if (!isNaN(res.data.skuNum)) {
                            main.stockPageOption.total = res.data.skuNum;
                        }
                        main.propertyList = res.data.propertyList;
                        main.stockList = res.data.stockList;
                        main.stockPageOption.curr = 1;
                    } else {
                        main.stockList = res.data.stockList;
                    }
                })
            },

            saveItem: function (stock) {
                var main = this;
                main.taskStockIncrementDetailService.saveItem({
                    "taskId" : main.taskId,
                    "subTaskId" : main.subTaskId,
                    "cartId" : main.cartId,
                    "stockInfo" : stock
                }).then(function (res) {
                    main.notify.success('TXT_MSG_UPDATE_SUCCESS');
                }, function (err) {
                    if (err.displayType == null) {
                        main.alert('TXT_MSG_UPDATE_FAIL');
                    }
                })
            },

            delItem: function (sku) {
                var main = this;
                main.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    main.taskStockIncrementDetailService.delItem({
                        "taskId": main.taskId,
                        "subTaskId": main.subTaskId,
                        "cartId": main.cartId,
                        "sku": sku
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

            executeStockIncrementSeparation: function (sku) {
                var main = this;
                main.confirm('TXT_MSG_DO_INCREASE').result.then(function () {
                    main.taskStockIncrementDetailService.executeStockIncrementSeparation({
                        "taskId": main.taskId,
                        "subTaskId" : main.subTaskId,
                        "cartId": main.cartId
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_INCREMENT_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_INCREMENT_FAIL');
                        }
                    })
                })
            },

            openImport: function (openImportStock) {
                var main = this;
                openImportStock('md', {
                    'task_id': main.taskId,
                    'subTaskId': main.subTaskId,
                    'platformList': {'cartId': main.cartId, 'cartName': main.cartName},
                    'propertyList': main.propertyList,
                    'parent_id': '2'
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