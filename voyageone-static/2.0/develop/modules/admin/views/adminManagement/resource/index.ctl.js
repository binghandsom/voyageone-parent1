/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl',
    'modules/admin/controller/treeTable.ctrl'
], function (admin) {
    admin.controller('ResManagementController', (function () {
        function ResManagementController(popups, alert, confirm, adminResService, adminUserService, adminOrgService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminResService = adminResService;
            this.adminUserService = adminUserService;
            this.adminOrgService = adminOrgService;
            this.selectRowsFactory = selectRowsFactory;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.selectedList = [];
            this.resSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                application: '',
                pageInfo: this.pageOption
            }
        }

        ResManagementController.prototype = {
            init: function () {
                var self = this;
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                self.adminResService.init().then(function (res) {
                    self.resList = res.data.treeList;
                    self.pageOption.total = res.data.count;
                })
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.adminResService.searchRes({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'application': self.searchInfo.application
                    })
                    .then(function (res) {
                        self.resList = res.data.result;
                        self.pageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.resList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id
                                });
                            }
                        });
                        self.resSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: self.pageOption,
                    orgName: '',
                    active: ''
                }
            },
            edit: function (type) {
                var self = this;
                _.filter(self.selectedList, function (item) {
                    return item.selected;
                }).forEach(function (item, index) {
                    self.selectedList[index] = item;
                });
                if (type == 'add') {
                    self.popups.openRes('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.selectedList.length > 1) {
                        self.alert('衹能選擇一條數據哦！');
                    } else {
                        _.forEach(self.resList, function (Info) {
                            if (Info.id == self.selectedList[0].id) {
                                self.popups.openRes(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.resSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.adminOrgService.deleteOrg(delList).then(function (res) {
                            self.search(1);
                        })
                    }
                );
            },
            getResType: function (type) {
                switch (type) {
                    case 0:
                        return '系统';
                        break;
                    case 1:
                        return '菜单';
                        break;
                    case 2:
                        return 'Action';
                        break;
                }
            }
        };
        return ResManagementController;
    })())
});
