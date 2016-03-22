/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskStockController", (function () {

        function TaskStockController($routeParams, taskStockService, cActions, confirm, alert, notify, $location, selectRowsFactory) {
            var urls = cActions.cms.task.taskStockService;
            var taskId = $routeParams['task_id'];
            if (_.isNaN(taskId)) {
                this.init = null;
                alert('TXT_MSG_UNVALID_URL').result.then(function () {
                    $location.path('/promotion/task');
                });
            }
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = taskId;
            this.readyNum = 0;
            this.waitSeparationNum = 0;
            this.separationOKNum = 0;
            this.separationFailNum = 0;
            this.waitRestoreNum = 0;
            this.revertOKNum = 0;
            this.revertFailNum = 0;
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
            this.realStockStatus = "";
            this.stockSelList = { selList: []};
            this.stockPageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getCommonStockList.bind(this)
            };
            this.realStockPageOption = {
                curr: 1,
                total: 0,
                size: 20,
                fetch: this.getRealStockList.bind(this)
            };
            this.selectRowsFactory = selectRowsFactory;
            this.tempStockListSelect = new selectRowsFactory();
            this.taskStockService = taskStockService;
            this.dynamicColNum = 0;

            this.downloadUrl = urls.root + "/" + urls.exportStockInfo;
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
                main.tempStockListSelect = new main.selectRowsFactory();
                if (status != undefined)  {
                    main.status = status;
                }
                if (status >= '0' && status <= '7') {
                    var allNumLabel = document.getElementById('allNum');
                    allNumLabel.setAttribute("class", "btn btn-default-vo");
                }
                main.taskStockService.searchStock({
                    "taskId" : main.taskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "qtyFrom" : main.qtyFrom,
                    "qtyTo" : main.qtyTo,
                    "status" : main.status,
                    "propertyList" : main.propertyList,
                    "platformList" : main.platformList,
                    "start1" :  0,
                    "length1" : 20,
                    "start2" :  0,
                    "length2" : 20
                }).then(function (res) {
                    main.tempStockListSelect.clearCurrPageRows();
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
                    main.realStockStatus = res.data.realStockStatus;
                    main.stockPageOption.curr = 1;
                    main.realStockPageOption.curr = 1;
                    _.forEach(res.data.stockList, function(stock) {
                        // 初始化数据选中需要的数组
                        main.tempStockListSelect.currPageRows({"id": stock.sku});
                    });
                    main.stockSelList = main.tempStockListSelect.selectRowsInfo;
                    main.dynamicColNum = 0;
                    _.forEach(res.data.propertyList, function(property) {
                        if (property.show) {
                            main.dynamicColNum++;
                        }

                    });
                    if (main.platformList.length + main.dynamicColNum <= 6) {
                        var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                        stockSeparateTbl.setAttribute("style", "width:100%;margin-bottom:0px");
                    } else {
                        var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                        stockSeparateTbl.setAttribute("style", "width:1800px;margin-bottom:0px");
                    }
                })
            },

            getCommonStockList: function () {
                var main = this;
                main.tempStockListSelect = new main.selectRowsFactory();
                main.taskStockService.getCommonStockList({
                    "taskId" : main.taskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "qtyFrom" : main.qtyFrom,
                    "qtyTo" : main.qtyTo,
                    "status" : main.status,
                    "propertyList" : main.propertyList,
                    "platformList" : main.platformList,
                    "start1" :  (main.stockPageOption.curr - 1) * main.stockPageOption.size,
                    "length1" : main.stockPageOption.size
                }).then(function (res) {
                    main.stockList = res.data.stockList;
                    _.forEach(res.data.stockList, function(stock) {
                        // 初始化数据选中需要的数组
                        main.tempStockListSelect.currPageRows({"id": stock.sku});
                    });
                    main.stockSelList = main.tempStockListSelect.selectRowsInfo;
                })
            },

            getRealStockList: function () {
                var main = this;
                main.taskStockService.getRealStockList({
                    "taskId" : main.taskId,
                    "model" : main.model,
                    "code" : main.code,
                    "sku" : main.sku,
                    "qtyFrom" : main.qtyFrom,
                    "qtyTo" : main.qtyTo,
                    "status" : main.status,
                    "propertyList" : main.propertyList,
                    "platformList" : main.platformList,
                    "start2" :  (main.realStockPageOption.curr - 1) * main.realStockPageOption.size,
                    "length2" : main.realStockPageOption.size
                }).then(function (res) {
                    main.realStockList = res.data.realStockList;;
                })
            },

            saveRecord: function (index) {
                var main = this;
                main.taskStockService.saveRecord({
                    "taskId" : main.taskId,
                    "stockList" : main.stockList,
                    "index" : index
                }).then(function (res) {
                    main.stockList = res.data.stockList;
                    main.notify.success('TXT_MSG_UPDATE_SUCCESS');
                }, function (err) {
                    if (err.displayType == null) {
                        main.alert('TXT_MSG_UPDATE_FAIL');
                    }
                })
            },

            setPercent: function () {
                var main = this;

                if(main.stockSelList.selList.length <= 0){
                    main.alert('TXT_MSG_NO_ROWS_SELECT');
                }
                var selPlatform = document.getElementById('selPlatform').value;
                var percent = document.getElementById('percent').value;
                if (percent.length > 0 && percent.substring(percent.length - 1) == '%') {
                    percent = percent.substring(0, percent.length - 1);
                }
                var percent = Number(percent);
                if (_.isNumber(percent) && percent > 0) {
                    _.each(main.stockSelList.selList, function (item) {
                        _.each(main.stockList, function (stock) {
                            if (item.id == stock.sku) {
                                _.each(stock.platformStock, function (platform) {
                                    if (platform.cartId == selPlatform && platform.status != null && platform.status != '') {
                                        platform.separationQty = (Math.floor(percent * 0.01 * stock.qty)).toString();
                                    }
                                });
                            }
                        });
                    });
                    main.notify.success('TXT_MSG_SET_SUCCESS');
                }
            },

            calculateCol: function (showFlg) {
                if (showFlg) {
                    this.dynamicColNum++;

                } else {
                    this.dynamicColNum--;
                }
                if (this.platformList.length + this.dynamicColNum <= 6) {
                    var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                    stockSeparateTbl.setAttribute("style", "width:100%;margin-bottom:0px");
                } else {
                    var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                    stockSeparateTbl.setAttribute("style", "width:1800px;margin-bottom:0px");
                }
            },

            delRecord: function (sku) {
                var main = this;
                main.confirm('TXT_MSG_DO_DELETE').result.then(function () {
                    main.taskStockService.delRecord({
                        "taskId": main.taskId,
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

            executeStockSeparation: function (sku) {
                var main = this;
                main.confirm('TXT_MSG_DO_SEPARATE').result.then(function () {
                    main.taskStockService.executeStockSeparation({
                        "taskId": main.taskId,
                        "model" : main.model,
                        "code" : main.code,
                        "sku" : main.sku,
                        "qtyFrom" : main.qtyFrom,
                        "qtyTo" : main.qtyTo,
                        "status" : main.status,
                        "selSku":sku,
                        "propertyList" : main.propertyList
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_SEPARATE_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_SEPARATE_FAIL');
                        }
                    })
                })
            },

            executeStockRevert: function (sku) {
                var main = this;
                main.confirm('TXT_MSG_DO_REVERT').result.then(function () {
                    main.taskStockService.executeStockRevert({
                        "taskId": main.taskId,
                        "model" : main.model,
                        "code" : main.code,
                        "sku" : main.sku,
                        "qtyFrom" : main.qtyFrom,
                        "qtyTo" : main.qtyTo,
                        "status" : main.status,
                        "selSku":sku,
                        "propertyList" : main.propertyList
                    }).then(function (res) {
                        main.notify.success('TXT_MSG_REVERT_SUCCESS');
                        main.search();
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_REVERT_FAIL');
                        }
                    })
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
                    "qtyFrom" : main.qtyFrom,
                    "qtyTo" : main.qtyTo,
                    "status" : main.status
                });
            }
        };

        return TaskStockController;

    })())
});