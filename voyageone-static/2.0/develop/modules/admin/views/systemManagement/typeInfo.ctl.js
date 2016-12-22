/**
 * Created by sofia on 2016/8/22.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('TypeInfoManagementController', (function () {
        function TypeInfoManagementController(popups, alert, confirm, typeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.typeService = typeService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.systemList = [];
            this.sysTypeInfoSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                id: '',
                name: '',
                comment: '',
                active: '',
                pageInfo: this.channelPageOption
            }
        }

        TypeInfoManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.typeService.searchTypeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'id': self.searchInfo.id,
                        'name': self.searchInfo.name,
                        'active': self.searchInfo.active,
                        'comment': self.searchInfo.comment
                    })
                    .then(function (res) {
                        self.systemList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempSelect == null) {
                            self.tempSelect = new self.selectRowsFactory();
                        } else {
                            self.tempSelect.clearCurrPageRows();
                            self.tempSelect.clearSelectedList();
                        }
                        _.forEach(self.systemList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempSelect.currPageRows({
                                    "id": Info.id,
                                    "code": Info.comment
                                });
                            }
                        });
                        self.sysTypeInfoSelList = self.tempSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    id: '',
                    name: '',
                    comment: '',
                    active: '',
                    pageInfo: self.channelPageOption
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openTypeAdd('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    _.forEach(self.systemList, function (Info) {
                        if (Info.id == self.sysTypeInfoSelList.selList[0].id) {
                            self.popups.openTypeAdd(Info).then(function (res) {
                                if (res.res == 'success') {
                                    self.search(1);
                                }else{
                                    return false;
                                }
                            });
                        }
                    })
                }
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.sysTypeInfoSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.typeService.deleteType(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return TypeInfoManagementController;
    })())
});