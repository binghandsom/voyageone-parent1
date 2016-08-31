/**
 * @Date:    2016-08-24 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('UserManagementController', (function () {
        function UserManagementController(popups, alert, confirm, adminUserService, storeService, adminOrgService, channelService, adminRoleService, selectRowsFactory) {
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

        UserManagementController.prototype = {
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
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openAddUser('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.adminUserSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.adminList, function (Info) {
                            if (Info.id == self.adminUserSelList.selList[0].id) {
                                self.popups.openAddUser(Info).then(function () {
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
                    _.forEach(self.adminList, function (Info) {
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
                            delList.push({'id': delInfo.id});
                        });
                        self.adminUserService.deleteUser(delList).then(function (res) {
                            self.search();
                        })
                    }
                );
            },
            resetPass: function () {
                var self = this;
                if (self.adminUserSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    self.confirm('确认要重置密码吗？').then(function () {
                        _.forEach(self.adminList, function (Info) {
                            if (Info.id == self.adminUserSelList.selList[0].id) {
                                self.adminUserService.resetPass(Info.userAccount).then(function (res) {
                                    console.log(res);
                                });
                            }
                        })
                    })
                }
            },
            getName: function (item) {
                var self = this;
                var orgNameList = [];
                _.map(self.orgList, function (org) {
                    if (org.id == item) {
                        orgNameList.push(org.orgName);
                    }
                });
                return orgNameList.join(',');
            },
            getActive: function (active) {
                switch (active) {
                    case 1:
                        return '启用';
                        break;
                    case 0:
                        return '禁用';
                        break;
                    case 2:
                        return '锁定';
                        break;
                }
            }
        };
        return UserManagementController;
    })())
});
