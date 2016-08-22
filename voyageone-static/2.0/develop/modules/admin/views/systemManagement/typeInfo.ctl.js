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
            this.tempChannelSelect = null;
            this.searchInfo = {
                id: '',
                name: '',
                comment: '',
                pageInfo: this.channelPageOption
            }
        }

        TypeInfoManagementController.prototype = {
            init: function () {
                var self = this;
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.typeService.searchTypeByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'id': self.searchInfo.id,
                        'name': self.searchInfo.name,
                        'comment': self.searchInfo.comment
                    })
                    .then(function (res) {
                        self.systemList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempChannelSelect == null) {
                            self.tempChannelSelect = new self.selectRowsFactory();
                        } else {
                            self.tempChannelSelect.clearCurrPageRows();
                            self.tempChannelSelect.clearSelectedList();
                        }
                        _.forEach(self.systemList, function (channelInfo) {
                            if (channelInfo.updFlg != 8) {
                                self.tempChannelSelect.currPageRows({
                                    "id": channelInfo.id,
                                    "code": channelInfo.comment
                                });
                            }
                        });
                        self.sysTypeInfoSelList = self.tempChannelSelect.selectRowsInfo;

                        // 设置cartName
                        if (!self.systemList) return;
                        for (var i = 0; i < self.systemList.length; i++) {
                            var tempCartList = [];
                            if (self.systemList[i].carts == null) return;
                            self.systemList[i].carts.map(function (item) {
                                tempCartList.push(item.name);
                            });
                            _.extend(self.systemList[i], {'cartName': tempCartList.join('/')});
                        }
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    id: '',
                    name: '',
                    comment: ''
                }
            },
            config: function (type) {
                var self = this;
                if (self.sysTypeInfoSelList.selList.length < 1) {
                    self.popups.openConfig({'configType':type});
                    return;
                } else {
                    _.forEach(self.systemList, function (channelInfo) {
                        if (channelInfo.orderChannelId == self.sysTypeInfoSelList.selList[0].id) {
                            _.extend(channelInfo,{'configType':type});
                            self.popups.openConfig(channelInfo);
                        }
                    })
                }
            },
            edit: function () {
                var self = this;
                if (self.sysTypeInfoSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.systemList, function (channelInfo) {
                        if (channelInfo.orderChannelId == self.sysTypeInfoSelList.selList[0].id) {
                            self.popups.openAdd(channelInfo).then(function(){
                                self.search(1);
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
                        self.channelService.deleteChannel(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search();
                        })
                    }
                );
            }
        };
        return TypeInfoManagementController;
    })())
});