/**
 * Created by sofia on 2016/8/23.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('CodeAttributeController', (function () {
        function CodeAttributeController(popups, alert, confirm, codeService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.codeService = codeService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.systemList = [];
            this.codeSelList = {selList: []};
            this.tempSelect = null;
            this.searchInfo = {
                id: '',
                code: '',
                name: '',
                des: '',
                active: '',
                pageInfo: this.channelPageOption
            };
        }

        CodeAttributeController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.search(1);
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.codeService.searchCodeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'id': self.searchInfo.id,
                        'code': self.searchInfo.code,
                        'name': self.searchInfo.name,
                        'des': self.searchInfo.des,
                        'active': self.searchInfo.active
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
                        _.forEach(self.systemList, function (Info, index) {
                            if (Info.updFlg != 8) {
                                _.extend(Info, {mainKey: index});
                                self.tempSelect.currPageRows({
                                    "id": Info.mainKey,
                                    "delId": Info.id,
                                    "delCode": Info.code
                                });
                            }
                        });
                        self.codeSelList = self.tempSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    id: '',
                    code: '',
                    name: '',
                    des: '',
                    active: '',
                    pageInfo: self.channelPageOption
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openTypeCode('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else {
                            return false;
                        }
                    });
                } else {
                    _.forEach(self.systemList, function (Info) {
                        if (Info.mainKey == self.codeSelList.selList[0].id) {
                            self.popups.openTypeCode(Info).then(function (res) {
                                if (res.res == 'success') {
                                    self.search(1);
                                }else {
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
                        _.forEach(self.codeSelList.selList, function (delInfo) {
                            delList.push({'id': delInfo.delId, 'code': delInfo.delCode});
                        });
                        self.codeService.deleteCode(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return CodeAttributeController;
    })())
});