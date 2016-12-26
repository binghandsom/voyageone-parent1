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
                roleId: '',
                active: '',
                channelId: '',
                orgId: '',
                application: '',
                storeId: '',
                company: '',
                pageInfo: this.pageOption
            }
        }

        UserManagementController.prototype = {
            init: function () {
                var self = this;
                self.storeService.getAllStore(null).then(function (res) {
                    self.tempList = res.data;
                    self.storeList = [];
                    _.forEach(self.tempList, function (item) {
                        var data = '(' + item.channelId + ')' + item.storeName;
                        self.storeList.push({'storeId': item.storeId, 'storeName': data});
                    })
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
                self.channelService.getAllCompany().then(function (res) {
                    self.companyList = res.data;
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
                    'roleId': self.searchInfo.roleId,
                    'active': self.searchInfo.active,
                    'channelId': self.searchInfo.channelId,
                    'orgId': self.searchInfo.orgId,
                    'application': self.searchInfo.application,
                    'companyId': self.searchInfo.company,
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
                    roleId: '',
                    active: '',
                    channelId: '',
                    orgId: '',
                    application: '',
                    company: '',
                    storeId: ''
                }
            },
            edit: function (item) {
                var self = this;
                if (item == 'add') {
                    self.popups.openAddUser('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    self.popups.openAddUser(item).then(function (res) {
                        if (res.res == 'success') {
                            self.search(1)
                        }else{
                            return false;
                        }
                    });
                }
            },
            addRoles: function () {
                var self = this;
                var ids = [];
                _.forEach(self.adminUserSelList.selList, function (sel) {
                    ids.push(sel.id);
                });
                self.popups.openAddRoles(ids).then(function (res) {
                    if (res.res == 'success') {
                        self.search(1);
                    }else{
                        return false;
                    }
                });
            },
            vieAuthority: function (item) {
                var self = this,popInfo = [];
                popInfo.push(item);
                self.popups.openUserAuthority(popInfo);
            },
            delete: function (item) {
                var self = this,delList = [];
                delList.push(item);
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        self.adminUserService.deleteUser(delList).then(function (res) {
                            if(res.data==true) self.search(1);
                        })
                    }
                );
            },
            resetPass: function () {
                var self = this;
                self.confirm('确认要重置密码吗？').then(function () {
                    var idList = [];
                    _.forEach(self.adminUserSelList.selList, function (sel) {
                        idList.push(sel.id);
                    });
                    self.adminUserService.resetPass(idList).then(function (res) {
                        if (res.data.password) {
                            self.alert('密码重置成功：' + res.data.password + "。");
                        } else {
                            self.alert('密码重置失败，请重试');
                        }
                    });
                })
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
