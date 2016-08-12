/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ConfigController', (function () {
        function ConfigController(context, confirm, channelService, AdminChannelService, selectRowsFactory) {
            this.sourceData = context;
            this.confirm = confirm;
            this.channelService = channelService;
            this.AdminChannelService = AdminChannelService;
            this.selectRowsFactory = selectRowsFactory;
            this.configPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.configSelList = {selList: []};
            this.tempConfigSelect = null;
            this.show = false;
            this.searchInfo = {
                channelName: this.sourceData ? this.sourceData.name : "",
                configType: 'Channel',
                pageInfo: this.configPageOption

            }
        }

        ConfigController.prototype = {
            init: function () {
                var self = this;
                self.channelService.getAllChannel().then(function (res) {
                    self.channelList = res.data;
                })
            },
            search: function () {
                var self = this;
                self.show = true;
                self.configInfo = {};
                self.AdminChannelService.searchConfigByPage({
                    'pageNum': self.searchInfo.pageInfo.curr,
                    'pageSize': self.searchInfo.pageInfo.size,
                    'configType': self.searchInfo.configType
                }).then(function (res) {
                    self.cfgList = res.data.result;
                    self.configPageOption.total = res.data.count;
                    
                    if (self.tempConfigSelect == null) {
                        self.tempConfigSelect = new self.selectRowsFactory();
                    } else {
                        self.tempConfigSelect.clearCurrPageRows();
                    }
                    _.forEach(self.cfgList, function (configInfo) {
                        if (configInfo.updFlg != 8) {
                            self.tempConfigSelect.currPageRows({
                                "id": configInfo.orderChannelId,
                                "code": configInfo.cfgName
                            });
                        }
                    });
                    self.configSelList = self.tempConfigSelect.selectRowsInfo;
                })

            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG');
            }
        };
        return ConfigController;
    })())
});