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
        function RoleManagementController(popups, alert, confirm, adminUserService, storeService, channelService, adminRoleService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminUserService = adminUserService;
            this.storeService = storeService;
            this.channelService = channelService;
            this.adminRoleService = adminRoleService;
            this.selectRowsFactory = selectRowsFactory;
            this.storePageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.adminRoleList = [];
            this.adminUserSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                roleName: '',
                roleType: '',
                active: '',
                channelId: '',
                storeId: '',
                application: '',
                pageInfo: this.storePageOption
            }
        }

        RoleManagementController.prototype = {
            init: function () {
                var self = this;
                self.storeService.getAllStore(null).then(function (res) {
                    self.storeList = res.data;
                });
                self.adminRoleService.getAllRoleType().then(function (res) {
                    self.roleTypeList = res.data;
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
                self.adminRoleService.init().then(function (res) {
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
                self.adminRoleService.searchRole({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'roleName': self.searchInfo.roleName,
                        'roleType': self.searchInfo.roleType,
                        'active': self.searchInfo.active,
                        'channelId': self.searchInfo.channelId,
                        'storeId': self.searchInfo.storeId,
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
                    roleName: '',
                    roleType: '',
                    active: '',
                    channelId: '',
                    storeId: '',
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
            authority: function (type) {
                var self = this;
                switch (type) {
                    case 'set':
                        self.adminRoleService.setAuth().then(function (res) {
                            console.log(res)
                        });
                        break;
                    case 'delete':
                        self.adminRoleService.removeAuth().then(function (res) {

                        });
                        break;
                    case 'add':
                        self.adminRoleService.addAuth().then(function (res) {

                        });
                        break;
                }
            },
            getRoleType: function (type) {
                switch (type) {
                    case 1:
                        return 'IT管理员';
                        break;
                    case 2:
                        return '运营人员';
                        break;
                    case 3:
                        return '客服人员';
                        break;
                    case 4:
                        return '财务人员';
                        break;
                }
            }
        };
        return RoleManagementController;
    })())
});
