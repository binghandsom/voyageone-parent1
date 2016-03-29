/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller("taskStockController", (function () {

        function TaskStockController($routeParams, taskStockService, cActions, confirm, alert, notify, selectRowsFactory,blockUI) {
            this.blockUI = blockUI;
            var urls = cActions.cms.task.taskStockService;
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.taskId = $routeParams['task_id'];
            //this.hasAuthority = true;
            this.readyNum = 0;
            this.waitingSeparateNum = 0;
            this.separatingNum = 0;
            this.separateSuccessNum = 0;
            this.separateFailureNum = 0;
            this.waitRevertNum = 0;
            this.revertingNum = 0;
            this.revertSuccessNum = 0;
            this.revertFailureNum = 0;
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
            this.selPlatform = "";
            this.percent = "";
            this.style= "";
            this.allNumClass = "btn btn-default-vo btn-vo";

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
                if (status == '') {
                    main.status = '';
                }
                if (status >= '0' && status <= '9') {
                    main.status = status;
                    main.allNumClass = "btn btn-default-vo";
                    //var allNumLabel = document.getElementById('allNum');
                    //allNumLabel.setAttribute("class", "btn btn-default-vo");
                }
                var start1 = 0;
                var start2 = 0;
                if (status == 'page') {
                    start1 = (main.stockPageOption.curr - 1) * main.stockPageOption.size;
                    start2 = (main.realStockPageOption.curr - 1) * main.realStockPageOption.size;
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
                    "start1" :  start1,
                    "start2" :  start2,
                    "length1" : main.stockPageOption.size,
                    "length2" : main.realStockPageOption.size
                }).then(function (res) {
                    //main.hasAuthority = res.data.hasAuthority;
                    //if (!main.hasAuthority) {
                    //    main.alert('没有权限访问！')
                    //    return;
                    //}
                    main.tempStockListSelect.clearCurrPageRows();
                    main.readyNum = res.data.readyNum;
                    main.waitingSeparateNum = res.data.waitingSeparateNum;
                    main.separatingNum = res.data.separatingNum;
                    main.separateSuccessNum = res.data.separateSuccessNum;
                    main.separateFailureNum = res.data.separateFailureNum;
                    main.waitRevertNum = res.data.waitRevertNum;
                    main.revertingNum = res.data.revertingNum;
                    main.revertSuccessNum = res.data.revertSuccessNum;
                    main.revertFailureNum = res.data.revertFailureNum;
                    main.changedNum = res.data.changedNum;
                    main.allNum = res.data.allNum;
                    if (!isNaN(res.data.skuNum)) {
                        main.stockPageOption.total = res.data.skuNum;
                        main.realStockPageOption.total = res.data.skuNum;
                    }
                    main.propertyList = res.data.propertyList;
                    main.platformList = res.data.platformList;
                    if (res.data.platformList.length > 0) {
                        main.selPlatform = res.data.platformList[0].cartId;
                    }
                    main.stockList = res.data.stockList;
                    main.realStockList = res.data.realStockList;
                    main.realStockStatus = res.data.realStockStatus;
                    if (status != 'page') {
                        main.stockPageOption.curr = 1;
                        main.realStockPageOption.curr = 1;
                    }
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
                        //var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                        //stockSeparateTbl.setAttribute("style", "width:100%;margin-bottom:0px");
                        main.style = "100%";
                    } else {
                        //var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                        //stockSeparateTbl.setAttribute("style", "width:1800px;margin-bottom:0px");
                        main.style = "1800px";
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
                if(main.stockSelList.selList.length <= 0) {
                    main.alert('TXT_MSG_NO_ROWS_SELECT');
                }

                var percent = main.percent;
                if (percent.length > 0 && percent.substring(percent.length - 1) == '%') {
                    percent = percent.substring(0, percent.length - 1);
                }
                var percent = Number(percent);
                if (_.isNumber(percent) && percent > 0) {
                    _.each(main.stockSelList.selList, function (item) {
                        _.each(main.stockList, function (stock) {
                            if (item.id == stock.sku) {
                                _.each(stock.platformStock, function (platform) {
                                    if (platform.cartId == main.selPlatform && platform.status != null && platform.status != '') {
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
                    //var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                    //stockSeparateTbl.setAttribute("style", "width:100%;margin-bottom:0px");
                    this.style = "100%";
                } else {
                    //var stockSeparateTbl = document.getElementById('stock_separate_tbl');
                    //stockSeparateTbl.setAttribute("style", "width:1800px;margin-bottom:0px");
                    this.style = "1800px";
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
                        main.search('page');
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
                        main.search('page');
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
                        main.search('page');
                    }, function (err) {
                        if (err.displayType == null) {
                            main.alert('TXT_MSG_REVERT_FAIL');
                        }
                    })
                })
            },

            download: function () {
                var main = this;
                //main.blockUI.start();
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
                },function(){
                    //main.blockUI.stop();
                    //var test= "";
                });
            }
        };

        return TaskStockController;

    })())
});