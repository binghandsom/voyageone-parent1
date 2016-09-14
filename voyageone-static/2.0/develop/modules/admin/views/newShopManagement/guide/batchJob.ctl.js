/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideBatchJobController', (function () {
        function GuideBatchJobController(popups, alert, confirm, selectRowsFactory, newShopService) {
            this.context = JSON.parse(window.sessionStorage.getItem('valueBean'));
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.newShopService = newShopService;
            this.tempTaskSelect = null;
            this.taskSelList = {selList: []};
        }

        GuideBatchJobController.prototype = {
            init: function () {
                var self = this;
                self.taskList = self.context.task;

                // 设置勾选框
                if (self.tempTaskSelect == null) {
                    self.tempTaskSelect = new self.selectRowsFactory();
                } else {
                    self.tempTaskSelect.clearCurrPageRows();
                    self.tempTaskSelect.clearSelectedList();
                }
                _.forEach(self.taskList, function (Info) {
                    if (Info.updFlg != 8) {
                        self.tempTaskSelect.currPageRows({
                            "id": Info.taskName
                        });
                    }
                });
                self.taskSelList = self.tempTaskSelect.selectRowsInfo;
                // End 设置勾选框
            },
            config: function (type) {
                var self = this;
                if (self.taskSelList.selList.length < 1) {
                    self.popups.openConfig({
                        'configType': type, 'isReadOnly': true,
                        'sourceData': self.context.task,
                        'orderChannelId': self.context.channel.orderChannelId
                    });
                    return;
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskName == self.taskSelList.selList[0].id) {
                            var data = {
                                'sourceData': self.context.task,
                                'configType': type,
                                'taskId': Info.taskId,
                                'isReadOnly': true,
                                'taskName': Info.taskName
                            };
                            self.popups.openConfig(data);
                        }
                    })
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openTask({
                        'kind': 'add',
                        'isReadOnly': true,
                        'orderChannelId': self.context.channel.orderChannelId
                    }).then(function (res) {
                        res.taskConfig = [{
                            'taskId': res.taskName,
                            'cfgName': 'run_flg',
                            'cfgVal1': res.runFlg,
                            'cfgVal2': '',
                            'endTime': null,
                            'comment': 'Run flag of task'
                        }];
                        res.taskId = 'X' + Math.random();
                        var list = self.taskList;
                        list.push(res);
                        self.init(1);
                    });
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskName == self.taskSelList.selList[0].id) {
                            _.extend(Info, {'isReadOnly': true});
                            self.popups.openTask(Info).then(function () {
                                self.init(1);
                            });
                        }
                    })
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.taskSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        _.forEach(delList, function (item) {
                                var source = self.taskList;
                                var data = _.find(source, function (sItem) {
                                    return sItem.taskName == item;
                                });
                                if (source.indexOf(data) > -1) {
                                    source.splice(source.indexOf(data), 1);
                                    self.init();
                                }
                            }
                        );
                    }
                );
            },
            run: function (item) {
                var self = this;
                if (item.type == 'Start') {
                    self.confirm('确定启动该任务吗？').then(function () {
                        if (item.data.runFlg == "0") {
                            item.data.runFlg = "1"
                        } else(
                            item.data.runFlg = "0"
                        );
                        forEachTaskList(item.data.taskConfig, item.data.runFlg);
                    })
                } else {
                    self.confirm('确定停止该任务吗？').then(function () {
                        if (item.data.runFlg == "0") {
                            item.data.runFlg = "1"
                        } else(
                            item.data.runFlg = "0"
                        );
                        forEachTaskList(item.data.taskConfig, item.data.runFlg);
                    })
                }
            },
            complete: function () {
                var self = this;
                self.confirm('您确定要提交全部新店的数据吗？').then(function () {
                    self.newShopService.saveChannelSeries(self.context).then(function (res) {
                        if (res.data == true) {
                            window.location.href = "#/newShop/history";
                        }
                    })
                })
            }
        };
        function forEachTaskList(source, target) {
            _.forEach(source, function (parentItem) {
                if (parentItem.cfgName == 'run_flg') {
                    parentItem.cfgVal1 = target;
                }
            })
        }

        return GuideBatchJobController;
    })())
});