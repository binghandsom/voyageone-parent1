/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('LoginLogManagementController', (function () {
        function LoginLogManagementController(popups, alert, confirm, adminUserService, adminLoginLogService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.adminLoginLogService = adminLoginLogService;
            this.selectRowsFactory = selectRowsFactory;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.adminList = [];
            this.loginLogSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                modifier: '',
                application: '',
                ip: '',
                startTime: '',
                endTime: '',
                pageInfo: this.pageOption
            }
        }

        LoginLogManagementController.prototype = {
            init: function () {
                var self = this;
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                self.adminLoginLogService.init().then(function (res) {
                    self.adminList = res.data.result;
                    self.pageOption.total = res.data.count;
                    // 设置勾选框
                    if (self.tempSelect == null) {
                        self.tempSelect = new self.selectRowsFactory();
                    } else {
                        self.tempSelect.clearCurrPageRows();
                        self.tempSelect.clearSelectedList();
                    }
                    _.forEach(self.adminList, function (Info) {
                        if (Info.updFlg != 8) {
                            self.tempSelect.currPageRows({
                                "id": Info.id
                            });
                        }
                    });
                    self.loginLogSelList = self.tempSelect.selectRowsInfo;
                    // End 设置勾选框
                })
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                if (self.searchInfo.startTime != '' ) {
                    var startTime = self.searchInfo.startTime.getTime();
                }
                if (self.searchInfo.endTime != '') {
                    var endTime = self.searchInfo.endTime.getTime();
                }
                self.adminLoginLogService.searchLog({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'modifier': self.searchInfo.modifier,
                        'application': self.searchInfo.application,
                        'ip': self.searchInfo.ip,
                        'startTime': startTime,
                        'endTime': endTime
                    })
                    .then(function (res) {
                        self.adminList = res.data.result;
                        self.pageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.adminList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id
                                });
                            }
                        });
                        self.loginLogSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: self.pageOption,
                    modifier: '',
                    application: '',
                    ip: '',
                    startTime: '',
                    endTime: ''
                }
            }
        };
        return LoginLogManagementController;
    })())
});
