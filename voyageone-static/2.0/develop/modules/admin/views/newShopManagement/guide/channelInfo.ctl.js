/**
 * Created by sofia on 2016/8/19.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideConfigController', (function () {
        function GuideConfigController(popups, alert, confirm, channelService, carrierConfigService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.carrierConfigService = carrierConfigService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.carrierList = [];
            this.carrierSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                orderChannelId: '',
                carrier: '',
                usekd100Flg: '',
                active: '',
                pageInfo: this.channelPageOption
            }
        }

        GuideConfigController.prototype = {
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
                self.carrierConfigService.searchCarrierConfigByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'orderChannelId': self.searchInfo.orderChannelId,
                        'carrier': self.searchInfo.carrier,
                        'active': self.searchInfo.active,
                        'usekd100Flg': self.searchInfo.usekd100Flg
                    })
                    .then(function (res) {
                        self.carrierList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempChannelSelect == null) {
                            self.tempChannelSelect = new self.selectRowsFactory();
                        } else {
                            self.tempChannelSelect.clearCurrPageRows();
                            self.tempChannelSelect.clearSelectedList();
                        }
                        _.forEach(self.carrierList, function (channelInfo, index) {
                            if (channelInfo.updFlg != 8) {
                                _.extend(channelInfo, {"mainKey": index});
                                self.tempChannelSelect.currPageRows({
                                    "id": channelInfo.mainKey,
                                    "code": channelInfo.carrier,
                                    "orderChannelId": channelInfo.orderChannelId
                                });
                            }
                        });
                        self.carrierSelList = self.tempChannelSelect.selectRowsInfo;
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    pageInfo: this.channelPageOption,
                    orderChannelId: '',
                    carrier: '',
                    active: '',
                    usekd100Flg: ''
                }
            },
            edit: function (type) {
                var self = this;
                if (type == 'add') {
                    self.popups.openChannelCarrier('add').then(function () {
                        self.search(1);
                    });
                } else {
                    if (self.carrierSelList.selList.length <= 0) {
                        self.alert('TXT_MSG_NO_ROWS_SELECT');
                        return;
                    } else {
                        _.forEach(self.carrierList, function (Info) {
                            if (Info.mainKey == self.carrierSelList.selList[0].id) {
                                self.popups.openChannelCarrier(Info).then(function () {
                                    self.search(1);
                                });
                            }
                        })
                    }
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.carrierSelList.selList, function (delInfo) {
                            delList.push({'orderChannelId': delInfo.orderChannelId, 'carrier': delInfo.code});
                        });
                        self.carrierConfigService.deleteCarrierConfig(delList).then(function (res) {
                            if (res.data.success == false)self.confirm(res.data.message);
                            self.search(1);
                        })
                    }
                );
            }
        };
        return GuideConfigController;
    })());
});