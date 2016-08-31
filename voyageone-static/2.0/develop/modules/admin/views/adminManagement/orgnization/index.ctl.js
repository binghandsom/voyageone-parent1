/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('OrgManagementController', (function () {
        function OrgManagementController(popups, alert, confirm, adminUserService, adminOrgService, channelService, adminRoleService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.adminOrgService = adminOrgService;
            this.channelService = channelService;
            this.adminRoleService = adminRoleService;
            this.selectRowsFactory = selectRowsFactory;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.adminList = [];
            this.adminOrgSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                orgName: '',
                active: '',
                pageInfo: this.pageOption
            }
        }

        OrgManagementController.prototype = {
            init: function () {
                var self = this;
                self.adminOrgService.init().then(function (res) {
                    self.orgDataList = res.data.result;
                    // 设置勾选框
                    if (self.tempSelect == null) {
                        self.tempSelect = new self.selectRowsFactory();
                    } else {
                        self.tempSelect.clearCurrPageRows();
                        self.tempSelect.clearSelectedList();
                    }
                    _.forEach(self.orgDataList, function (Info, index) {
                        if (Info.updFlg != 8) {
                            self.tempSelect.currPageRows({
                                "id": Info.id
                            });
                        }
                    });
                    self.adminOrgSelList = self.tempSelect.selectRowsInfo;
                    // End 设置勾选框
                })
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.adminOrgService.searchOrg({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'orgName': self.searchInfo.orgName,
                        'active': self.searchInfo.active
                    })
                    .then(function (res) {
                        self.orgDataList = res.data.result;
                        self.pageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.orgDataList, function (Info, index) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id
                                });
                            }
                        });
                        self.adminOrgSelList = self.tempSelect.selectRowsInfo;
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
                if (type == 'add') {
                    self.popups.openOrg('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.adminOrgSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.orgDataList, function (Info) {
                            if (Info.id == self.adminOrgSelList.selList[0].id) {
                                self.popups.openOrg(Info).then(function () {
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
                        _.forEach(self.adminOrgSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.adminOrgService.deleteOrg(delList).then(function (res) {
                            self.search(1);
                        })
                    }
                );
            }
        };
        return OrgManagementController;
    })())
});
