/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('ChannelSmsCogController', (function () {
        function ChannelSmsCogController(popups, alert, confirm, channelService, smsConfigService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.smsConfigService = smsConfigService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.channelList = [];
            this.channelSmsSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                smsType: '',
                content: '',
                smsCode: '',
                active: '',
                pageInfo: this.channelPageOption
            }
        }

        ChannelSmsCogController.prototype = {
            init: function () {
                var self = this;
                self.activeList = [{active: true, value: '启用'}, {active: false, value: '禁用'}];
                self.channelService.getAllChannel().then(function (res) {
                    self.channelAllList = res.data;
                });
                self.search();
            },
            search: function (page) {
                var self = this;
                page == 1 ? self.searchInfo.pageInfo.curr = 1 : page;
                self.smsConfigService.searchSmsConfigByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'orderChannelId': self.searchInfo.orderChannelId,
                        'smsType': self.searchInfo.smsType,
                        'content': self.searchInfo.content,
                        'active': self.searchInfo.active,
                        'smsCode': self.searchInfo.smsCode
                    })
                    .then(function (res) {
                        self.channelList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempChannelSelect == null) {
                            self.tempChannelSelect = new self.selectRowsFactory();
                        } else {
                            self.tempChannelSelect.clearCurrPageRows();
                            self.tempChannelSelect.clearSelectedList();
                        }
                        _.forEach(self.channelList, function (Info) {
                            if (Info.updFlg != 8) {
                                self.tempChannelSelect.currPageRows({
                                    "id": Info.seq,
                                    "code": Info.smsType
                                });
                            }
                        });
                        self.channelSmsSelList = self.tempChannelSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.channelPageOption,
                    orderChannelId: '',
                    smsType: '',
                    content: '',
                    active: '',
                    smsCode: ''
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openChannelSms('add').then(function (res) {
                        if (res.res == 'success') {
                            self.search(1);
                        }else{
                            return false;
                        }
                    });
                } else {
                    _.forEach(self.channelList, function (Info) {
                        if (Info.seq == self.channelSmsSelList.selList[0].id) {
                            self.popups.openChannelSms(Info).then(function (res) {
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
                        _.forEach(self.channelSmsSelList.selList, function (delInfo) {
                            delList.push(delInfo.id);
                        });
                        self.smsConfigService.deleteSmsConfig(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search();
                        })
                    }
                );
            }
        };
        return ChannelSmsCogController;
    })())
});