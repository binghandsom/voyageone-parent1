/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskStockController", (function () {

        function TaskStockController($routeParams, taskStockService, cActions, alert, notify, $location) {
            var urls = cActions.cms.task.taskStockService;
            var taskId = parseInt($routeParams['task_id']);
            if (_.isNaN(taskId)) {
                this.init = null;
                alert('TXT_MSG_UNVALID_URL').result.then(function () {
                    $location.path('/promotion/task');
                });
            }
            this.alert = alert;
            this.notify = notify;
            this.taskId = taskId;
            this.readyNum = 0;
            this.waitSeparationNum = 0;
            this.separationOKNum = 0;
            this.separationFailNum = 0;
            this.waitRestoreNum = 0;
            this.restoreOKNum = 0;
            this.restoreFailNum = 0;
            this.changedNum = 0;
            this.allNum = 0;
            this.model = "";
            this.code = "";
            this.sku = "";
            this.qtyFrom = "";
            this.qtyTo = "";
            this.status = "";
            this.propertyList = [];
            this.platformList = [];
            this.stockList = [];
            this.realStockList = [];
            this.stockListSelList = { selList: []};
            this.stockPageOption = {
                curr: 1,
                total: 0,
                size: 3,
                fetch: this.getCommonStockList.bind(this)
            };
            this.realStockPageOption = {
                curr: 1,
                total: 0,
                size: 3,
                fetch: this.getRealStockList.bind(this)
            };

            this.taskStockService = taskStockService;
        }

        TaskStockController.prototype = {
            init: function () {
                this.search();
            },
            clear: function () {
                this.model = "";
                this.code = "";
                this.sku = "";
                this.qtyFrom = "";
                this.qtyTo = "";
                this.status = "";
                _.each(this.propertyList, function (property) {
                    property.value = "";
                });
            },

        search: function (status) {
                var main = this;
                if (status != undefined)  {
                    main.status = status;
                }
                if (status >= '0' && status <= '7') {
                    var allNumLabel = document.getElementById('allNum');
                    allNumLabel.setAttribute("class", "btn btn-default-vo");
                }
                this.taskStockService.searchStock({
                    "taskId" : this.taskId,
                    "model" : this.model,
                    "code" : this.code,
                    "sku" : this.sku,
                    "qtyFrom" : this.qtyFrom,
                    "qtyTo" : this.qtyTo,
                    "status" : this.status,
                    "propertyList" : this.propertyList,
                    "platformList" : this.platformList,
                    "start1" :  0,
                    "length1" : 3,
                    "start2" :  0,
                    "length2" : 3
                }).then(function (res) {
                    main.readyNum = res.data.readyNum;
                    main.waitSeparationNum = res.data.waitSeparationNum;
                    main.separationOKNum = res.data.separationOKNum;
                    main.separationFailNum = res.data.separationFailNum;
                    main.waitRestoreNum = res.data.waitRestoreNum;
                    main.revertOKNum = res.data.revertOKNum;
                    main.revertFailNum = res.data.revertFailNum;
                    main.changedNum = res.data.changedNum;
                    main.allNum = res.data.allNum;
                    main.stockPageOption.total = res.data.skuNum;
                    main.realStockPageOption.total = res.data.skuNum;
                    main.propertyList = res.data.propertyList;
                    main.platformList = res.data.platformList;
                    main.stockList = res.data.stockList;
                    main.realStockList = res.data.realStockList;
                    main.stockPageOption.curr = 1;
                    main.realStockPageOption.curr = 1;
                })
            },

            getCommonStockList: function () {
                var main = this;
                this.taskStockService.getCommonStockList({
                    "taskId" : this.taskId,
                    "model" : this.model,
                    "code" : this.code,
                    "sku" : this.sku,
                    "qtyFrom" : this.qtyFrom,
                    "qtyTo" : this.qtyTo,
                    "status" : this.status,
                    "propertyList" : this.propertyList,
                    "platformList" : this.platformList,
                    "start1" :  (this.stockPageOption.curr - 1) * this.stockPageOption.size,
                    "length1" : this.stockPageOption.size
                }).then(function (res) {
                    main.stockList = res.data.stockList;;
                })
            },

            getRealStockList: function () {
                var main = this;
                this.taskStockService.getRealStockList({
                    "taskId" : this.taskId,
                    "model" : this.model,
                    "code" : this.code,
                    "sku" : this.sku,
                    "qtyFrom" : this.qtyFrom,
                    "qtyTo" : this.qtyTo,
                    "status" : this.status,
                    "propertyList" : this.propertyList,
                    "platformList" : this.platformList,
                    "start2" :  (this.realStockPageOption.curr - 1) * this.realStockPageOption.size,
                    "length2" : this.realStockPageOption.size
                }).then(function (res) {
                    main.stockList = res.data.stockList;;
                })
            }
        };

        return TaskStockController;

    })())
});