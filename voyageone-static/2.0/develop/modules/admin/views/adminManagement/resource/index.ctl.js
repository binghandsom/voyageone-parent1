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
        function ResManagementController(popups, alert, confirm, adminResService, adminUserService) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.adminResService = adminResService;
            this.adminUserService = adminUserService;
            this.pageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

            this.selectedList = [];
            this.flatResList = [];
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
                self.selectedList = [];
                self.adminResService.searchRes({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'application': self.searchInfo.application
                    })
                    .then(function (res) {
                        self.resList = res.data.result;
                        self.pageOption.total = res.data.count;
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
                var selectedList = [];
                // 过滤选择框选中的数据
                _.filter(self.selectedList, function (item) {
                    return item.selected;
                }).forEach(function (item) {
                    if (selectedList.length < 1) {
                        selectedList.push(item);
                    } else {
                        if (selectedList.indexOf(item) < 0) {
                            selectedList.push(item);
                        }
                    }
                });
                editCallback(selectedList);
                function editCallback() {
                    if (type == 'add') {
                        self.popups.openRes('add').then(function (res) {
                            if (res.res == 'success') {
                                self.search(1);
                            }
                        });
                    } else {
                        if (selectedList.length < 1) {
                            self.alert('请选择一条数据！');
                        } else if (selectedList.length > 1) {
                            self.alert('只能选择一条数据哦！');
                        } else {
                            var data = _.filter(self.flatResList, function (Info) {
                                return Info.id == selectedList[0].id
                            });
                            return self.popups.openRes(data[0]);
                        }
                    }
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_INACTIVE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.selectedList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.adminResService.deleteRes(delList).then(function (res) {
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
