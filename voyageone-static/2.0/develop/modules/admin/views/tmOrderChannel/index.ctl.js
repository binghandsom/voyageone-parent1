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
            this.tempFeedSelect = null;
            this.searchInfo = {
                channelId: '',
                channelFullName: '',
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
                        'pageSize': self.searchInfo.pageInfo.size
                    })
                    .then(function (res) {
                        self.channelList = res.data.result;
                        self.channelPageOption.total = res.data.count;

                        if (self.tempFeedSelect == null) {
                            self.tempFeedSelect = new self.selectRowsFactory();
                        } else {
                            self.tempFeedSelect.clearCurrPageRows();
                        }
                        _.forEach(self.channelList, function (channelInfo) {
                            if (channelInfo.updFlg != 8) {
                                self.tempFeedSelect.currPageRows({ "id": channelInfo.orderChannelId, "code": channelInfo.name });
                            }
                        });
                        self.channelSelList = self.tempFeedSelect.selectRowsInfo;
                    })
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
                }
                self.popups.openAdd();

            },
            delete: function () {
                var self = this;
                if (self.channelSelList.selList.length <= 0) {
                    self.alert('TXT_MSG_NO_ROWS_SELECT');
                    return;
                }
                self.confirm('TXT_CONFIRM_DELETE_MSG');
            }
        };
        return TmOrderChannelController;
    })())
});
