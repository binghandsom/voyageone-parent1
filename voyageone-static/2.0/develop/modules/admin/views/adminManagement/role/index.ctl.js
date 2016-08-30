/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('RoleManagementController', (function () {
        function RoleManagementController(popups, alert, confirm, adminUserService, storeService, adminOrgService, channelService, adminRoleService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.storeService = storeService;
            this.adminOrgService = adminOrgService;
            this.channelService = channelService;
            this.adminRoleService = adminRoleService;
            this.selectRowsFactory = selectRowsFactory;
            this.storePageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.adminRoleList = [];
            this.adminUserSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                userAccount: '',
                roleId: '',
                active: '',
                channelId: '',
                orgId: '',
                application: '',
                pageInfo: this.storePageOption
            }
        }

        RoleManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
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
                self.adminRoleService.init().then(function(res){
                    self.adminRoleList = res.data.result;
                    // 设置勾选框
                    if (self.tempSelect == null) {
                        self.tempSelect = new self.selectRowsFactory();
                    } else {
                        self.tempSelect.clearCurrPageRows();
                        self.tempSelect.clearSelectedList();
                    }
                    _.forEach(self.adminRoleList, function (Info) {
                        if (Info.updFlg != 8) {
                            self.tempSelect.currPageRows({
                                "id": Info.id,
                                "code": Info.storeName,
                                "orderChannelId": Info.orderChannelId
                            });
                        }
                    });
                    self.adminUserSelList = self.tempSelect.selectRowsInfo;
                    // End 设置勾选框
                });
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.adminRoleService.init({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'userAccount': self.searchInfo.userAccount,
                        'roleId': self.searchInfo.roleId,
                        'active': self.searchInfo.active,
                        'channelId': self.searchInfo.channelId,
                        'orgId': self.searchInfo.orgId,
                        'application': self.searchInfo.application
                    })
                    .then(function (res) {
                        self.adminRoleList = res.data.result;
                        self.storePageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.adminRoleList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id
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
                    pageInfo: self.storePageOption,
                    userAccount: '',
                    roleId: '',
                    active: '',
                    channelId: '',
                    orgId: '',
                    application: ''
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openRole('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.adminUserSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.adminRoleList, function (Info) {
                            if (Info.id == self.adminUserSelList.selList[0].id) {
                                self.popups.openRole(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }
            },
            vieAuthority: function () {
                var self = this;
                if (self.adminUserSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.adminRoleList, function (Info) {
                        if (Info.id == self.adminUserSelList.selList[0].id) {
                            self.popups.openUserAuthority(Info).then(function () {
                                self.search(1);
                            });
                        }
                    });
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.adminUserSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.adminRoleService.deleteRole(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
            },
        };
        return RoleManagementController;
    })())
});
