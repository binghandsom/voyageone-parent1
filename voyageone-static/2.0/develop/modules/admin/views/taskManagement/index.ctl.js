/**
 * @Date:    2016-08-19 13:57:18
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('TaskManagementController', (function () {
        function TaskManagementController(popups, alert, confirm, taskService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.taskService = taskService;
            this.taskPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.taskList = [];
            this.taskSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                taskType: '',
                taskName: '',
                taskComment: '',
                pageInfo: this.taskPageOption
            }
        }

        TaskManagementController.prototype = {
            init: function () {
                var self = this;
                self.taskService.getAllTaskType().then(function(res){
                    self.taskTypeList = res.data;
                });
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.taskService.searchTaskByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'taskType': self.searchInfo.taskType,
                        'taskName': self.searchInfo.taskName,
                        'taskComment': self.searchInfo.taskComment
                    })
                    .then(function (res) {
                        self.taskList = res.data.result;
                        self.taskPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.taskList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.taskId,
                                    "code": Info.taskName
                                });
                            }
                        });
                        self.taskSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.taskPageOption,
                    taskType: '',
                    taskName: '',
                    taskComment: ''
                }
            },
            config: function (type) {
                var self = this;
                if (self.taskSelList.selList.length < 1) {
                    self.popups.openConfig({'configType': type});
                    return;
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskId == self.taskSelList.selList[0].id) {
                            _.extend(Info, {'configType': type});
                            self.popups.openConfig(Info);
                        }
                    })
                }
            },
            edit: function () {
                var self = this;
                if (self.taskSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.taskList, function (Info) {
                        if (Info.taskId == self.taskSelList.selList[0].id) {
                            self.popups.openStoreAdd(Info).then(function () {
                                self.search(1);
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
                            delList.push( delInfo.id );
                        });
                        self.taskService.deleteStore(delList).then(function (res) {
                            self.search(1);
                        })
                    }
                );
            }
        };
        return TaskManagementController;
    })())
});
