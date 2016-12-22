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
            this._selall = false;
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
                    self.tempList = res.data;
                    self.storeList = [];
                    _.forEach(self.tempList, function (item) {
                        var data = '(' + item.channelId + ')' + item.storeName;
                        self.storeList.push({'storeId': item.storeId, 'storeName': data});
                    })
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
                    self.popups.openRole('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    var Info = _.filter(self.adminRoleList, function (role) {
                        return role.id == self.adminUserSelList.selList[0].id
                    });
                    if (type == 'copy') {
                        _.extend(Info[0], {isCopyRole: true});
                        self.popups.openRole(Info[0]).then(function (res) {
                            if (res.res == 'success') {
                                self.search(1);
                            }else{
                                return false;
                            }
                        });
                    } else if (type == 'edit') {
                        Info[0].isCopyRole == true ? Info[0].isCopyRole = false : Info[0].isCopyRole = false;
                        self.popups.openRole(Info[0]).then(function (res) {
                            if (res.res == 'success') {
                                self.search(1);
                            }else{
                                return false;
                            }
                        });
                    }
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.adminUserSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.adminRoleService.deleteRole(delList).then(function () {
                            self.search();
                        })
                    }
                );
            },
            authority: function (type) {
                var self = this;
                if (self._selall == false && self.adminUserSelList.selList.length == 0) {
                    self.alert('请至少选择一条数据 或 勾选全量操作！');
                    return;
                }
                if (self._selall == true) {
                    var configInfo = {
                        roleIds: [],
                        form: {
                            'roleName': self.searchInfo.roleName,
                            'roleType': self.searchInfo.roleType - 0,
                            'active': self.searchInfo.active - 0,
                            'channelId': self.searchInfo.channelId,
                            'storeId': self.searchInfo.storeId - 0,
                            'application': self.searchInfo.application
                        }
                    };
                    if (configInfo.form.roleType == 0) {
                        delete configInfo.form.roleType;
                    }
                    if (configInfo.form.active == 0) {
                        delete configInfo.form.active;
                    }
                    if (configInfo.form.storeId == 0) {
                        delete configInfo.form.storeId;
                    }
                    self.popups.openRoleEdit(configInfo).then(function (res) {
                        console.log(res)
                    });
                } else {
                    var configInfo = {roleIds: [], application: ''}, setInfo = [];
                    _.forEach(self.adminRoleList, function (Info) {
                        _.forEach(self.adminUserSelList.selList, function (item) {
                            if (Info.id == item.id) {
                                setInfo.push(Info);
                            }
                        });
                    });
                    _.forEach(setInfo, function (item) {
                        configInfo.roleIds.push(item.id);
                    });
                }
                switch (type) {
                    case 'set':
                        _.extend(configInfo, {'type': 'set'});
                        self.popups.openRoleEdit(configInfo).then(function (res) {
                            console.log(res)
                        });
                        break;
                    case 'delete':
                        _.extend(configInfo, {'type': 'delete'});
                        self.popups.openRoleEdit(configInfo).then(function (res) {
                            console.log(res)
                        });
                        break;
                    case 'add':
                        _.extend(configInfo, {'type': 'add'});
                        self.popups.openRoleEdit(configInfo).then(function (res) {
                            console.log(res)
                        });
                        break;
                }
            },
            getRoleType: function (type) {
                switch (type) {
                    case 1:
                        return '管理员';
                        break;
                    case 2:
                        return '客服';
                        break;
                    case 3:
                        return '客服主管';
                        break;
                    case 4:
                        return '仓库';
                        break;
                    case 5:
                        return '运营';
                        break;
                    case 6:
                        return 'Vendor';
                        break;
                    case 7:
                        return '翻译';
                        break;
                    case 99:
                        return '其他';
                        break;
                }
            }
        };
        return RoleManagementController;
    })())
});
