/**
 * Created by sofia on 2016/9/1.
 */
define([
    'admin',
    'modules/admin/controller/popup.ctl'
], function (admin) {
    admin.controller('GuideChannelCogInfoController', (function () {
        function GuideChannelCogInfoController(smsConfigService, selectRowsFactory) {
            this.selectRowsFactory = selectRowsFactory;
            this.smsConfigService = smsConfigService;
            this.channelPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};

        }

        GuideChannelCogInfoController.prototype = {
            init: function () {
                var self = this;
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
            }
        }
        return GuideChannelCogInfoController;
    })())
});