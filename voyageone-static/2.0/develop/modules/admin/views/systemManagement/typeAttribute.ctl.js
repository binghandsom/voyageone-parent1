/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('TypeAttributeManagementController', (function () {
        function TypeAttributeManagementController(popups, alert, confirm, typeService, typeAttrService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.typeService = typeService;
            this.typeAttrService = typeAttrService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.systemList = [];
            this.sysTypeAttrSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                typeId: '',
                langId: '',
                name: '',
                value: '',
                active: '',
                pageInfo: this.channelPageOption
            }
        }

        TypeAttributeManagementController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.typeService.getAllType().then(function (res) {
                    self.typeList = res.data;
                });
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.typeAttrService.searchTypeAttributeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'typeId': self.searchInfo.typeId,
                        'langId': self.searchInfo.langId,
                        'value': self.searchInfo.value,
                        'active': self.searchInfo.active,
                        'name': self.searchInfo.name
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
                                    "code": Info.typeName,
                                    "typeId": Info.typeId
                                });
                            }
                        });
                        self.sysTypeAttrSelList = self.tempSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    typeId: '',
                    langId: '',
                    name: '',
                    value: '',
                    active: '',
                    pageInfo: self.channelPageOption
                }
            },
            edit: function (type) {
                var self = this;
                switch (type) {
                    case 'edit':
                        if (self.sysTypeAttrSelList.selList.length <= 0) {
                            self.alert('TXT_MSG_NO_ROWS_SELECT');
                            return;
                        } else {
                            _.forEach(self.systemList, function (Info) {
                                if (Info.id == self.sysTypeAttrSelList.selList[0].id) {
                                    self.popups.openTypeAttr(Info).then(function () {
                                        self.search(1);
                                    });
                                }
                            });
                        }
                        break;
                    case 'add':
                        self.popups.openTypeAttr('add').then(function () {
                            self.typeService.getAllType().then(function (res) {
                                self.typeList = res.data;
                            });
                            self.search(1);
                        });
                        break;
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.sysTypeAttrSelList.selList, function (delInfo) {
                            delList.push(delInfo.typeId);
                        });
                        self.typeAttrService.deleteTypeAttribute(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return TypeAttributeManagementController;
    })())
});