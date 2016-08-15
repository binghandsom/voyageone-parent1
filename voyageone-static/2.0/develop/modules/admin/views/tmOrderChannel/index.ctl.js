/**
 * @Date:    2016-08-10 11:16:14
 * @User:    sofia
 * @Version: 1.0.0
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('TmOrderChannelController', (function () {
        function TmOrderChannelController(popups, alert, confirm, channelService, selectRowsFactory) {
            this.popups = popups;
            this.alert = alert;
            this.confirm = confirm;
            this.selectRowsFactory = selectRowsFactory;
            this.channelService = channelService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.channelList = [];
            this.channelSelList = {selList: []};
            this.tempChannelSelect = null;
            this.searchInfo = {
                channelId: '',
                channelName: '',
                isUsjoi: '',
                pageInfo: this.channelPageOption
            }
        }

        TmOrderChannelController.prototype = {
            init: function () {
                var self = this;
                self.search();
            },
            search: function () {
                var self = this;
                self.channelService.searchChannelByPage({
                        'pageNum': self.searchInfo.pageInfo.curr,
                        'pageSize': self.searchInfo.pageInfo.size,
                        'channelId': self.searchInfo.channelId,
                        'channelName': self.searchInfo.channelName,
                        'isUsjoi': self.searchInfo.isUsjoi
                    })
                    .then(function (res) {
                        self.channelList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        // 设置勾选框
                        if (self.tempChannelSelect == null) {
                            self.tempChannelSelect = new self.selectRowsFactory();
                        } else {
                            self.tempChannelSelect.clearCurrPageRows();
                        }
                        _.forEach(self.channelList, function (channelInfo) {
                            if (channelInfo.updFlg != 8) {
                                self.tempChannelSelect.currPageRows({
                                    "id": channelInfo.orderChannelId,
                                    "code": channelInfo.name
                                });
                            }
                        });
                        self.channelSelList = self.tempChannelSelect.selectRowsInfo;

                        // 设置cartName
                        for (var i = 0; i < self.channelList.length; i++) {
                            var tempCartList = [];
                            if (self.channelList[i].carts == null) return;
                            self.channelList[i].carts.map(function (item) {
                                tempCartList.push(item.name);
                            });
                            _.extend(self.channelList[i], {'cartName': tempCartList.join('/')});
                        }
                    })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    channelId: '',
                    channelFullName: '',
                    isUsjoi: ''
                }
            },
            config: function () {
                var self = this;
                if (self.channelSelList.selList.length < 1) {
                    self.popups.openConfig();
                    return;
                } else {
                    _.forEach(self.channelList, function (channelInfo) {
                        if (channelInfo.orderChannelId == self.channelSelList.selList[0].id) {
                            self.popups.openConfig(channelInfo);
                        }
                    })
                }
            },
            edit: function () {
                var self = this;
                if (self.channelSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                } else {
                    _.forEach(self.channelList, function (channelInfo) {
                        if (channelInfo.orderChannelId == self.channelSelList.selList[0].id) {
                            self.popups.openAdd(channelInfo);
                        }
                    })
                }

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG').then(function () {
                        var delList = [];
                        _.forEach(self.channelSelList.selList, function (delInfo) {
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
        return TmOrderChannelController;
    })())
});
