/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('ActionLogManagementController', (function () {
        function ActionLogManagementController(popups, alert, confirm, adminUserService, storeService, adminOrgService, channelService, adminRoleService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.storeService = storeService;
            this.adminOrgService = adminOrgService;
            this.channelService = channelService;
            this.adminRoleService = adminRoleService;
            this.selectRowsFactory = selectRowsFactory;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.adminList = [];
            this.adminUserSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                userAccount: '',
                roleName: '',
                active: '',
                channelId: '',
                orgId: '',
                application: '',
                storeId: '',
                pageInfo: this.pageOption
            }
        }

        ActionLogManagementController.prototype = {
            init: function () {
                var self = this;
                self.storeService.getAllStore().then(function (res) {
                    self.storeList = res.data;
                });
                self.adminOrgService.getAllOrg().then(function (res) {
                    self.orgList = res.data;
                });
                self.channelService.getAllChannel().then(function (res) {
                    self.channelList = res.data;
                });
                self.adminUserService.getAllApp().then(function (res) {
                    self.appList = res.data;
                });
                self.adminRoleService.getAllRole().then(function (res) {
                    self.roleList = res.data;
                });
                self.adminUserService.init().then(function (res) {
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
                                "id": Info.id,
                                "code": Info.storeName
                            });
                        }
                    });
                    self.adminUserSelList = self.tempSelect.selectRowsInfo;
                    // End 设置勾选框
                })
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.adminUserService.searchUser({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'userAccount': self.searchInfo.userAccount,
                        'roleName': self.searchInfo.roleName,
                        'active': self.searchInfo.active,
                        'channelId': self.searchInfo.channelId,
                        'orgId': self.searchInfo.orgId,
                        'application': self.searchInfo.application,
                        'storeId': self.searchInfo.storeId
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
                                    "id": Info.id,
                                    "code": Info.storeName
                                });
                            }
                        });
                        self.adminUserSelList = self.tempSelect.selectRowsInfo;
                        // End 设置勾选框
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: self.pageOption,
                    userAccount: '',
                    roleName: '',
                    active: '',
                    channelId: '',
                    orgId: '',
                    application: '',
                    storeId: ''
                }
            },
            viewDetail: function () {
                var self = this;
                self.popups.openLogDetail().then(function () {
                    self.search(1);
                });
                    // if (self.adminUserSelList.selList.length <= 0) {
                    //     self.alert('TXT_MSG_NO_ROWS_SELECT');
                    //     return;
                    // } else {
                    //     _.forEach(self.adminList, function (Info) {
                    //         if (Info.id == self.adminUserSelList.selList[0].id) {
                    //             self.popups.openLogDetail(Info).then(function () {
                    //                 self.search(1);
                    //             });
                    //         }
                    //     })
                    // }
            }
        };
        return ActionLogManagementController;
    })())
});
