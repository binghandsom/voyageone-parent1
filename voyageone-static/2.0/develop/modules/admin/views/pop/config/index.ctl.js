/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ConfigController', (function () {
        function ConfigController(popups, context, confirm, $uibModalInstance, channelService, AdminChannelService, selectRowsFactory) {
            this.popups = popups;
            this.sourceData = context;
            this.confirm = confirm;
            this.$uibModalInstance = $uibModalInstance;
            this.channelService = channelService;
            this.AdminChannelService = AdminChannelService;
            this.selectRowsFactory = selectRowsFactory;
            this.configPageOption = {curr: 1, size: 10, total: 0, fetch: this.search.bind(this)};
            this.configSelList = {selList: []};
            this.tempConfigSelect = null;
            this.show = false;
            this.searchInfo = {
                channelId: this.sourceData ? this.sourceData.orderChannelId : "",
                configType: 'Channel',
                pageInfo: this.configPageOption,
                cfgName: '',
                cfgVal: ''
            };
            this.tempChannelList = [];
        }

        ConfigController.prototype = {
            init: function () {
                var self = this;
                self.channelService.getAllChannel().then(function (res) {
                    self.channelList = res.data;
                });
            },
            search: function () {
                var self = this;
                self.show = true;
                self.configInfo = {};
                self.AdminChannelService.searchConfigByPage({
                    'pageNum': self.searchInfo.pageInfo.curr,
                    'pageSize': self.searchInfo.pageInfo.size,
                    'configType': self.searchInfo.configType,
                    'channelId': self.searchInfo.channelId,
                    'cfgName': self.searchInfo.cfgName,
                    'cfgVal': self.searchInfo.cfgVal
                }).then(function (res) {
                    self.cfgList = res.data.result;
                    self.configPageOption.total = res.data.count;

                    if (self.tempConfigSelect == null) {
                        self.tempConfigSelect = new self.selectRowsFactory();
                    } else {
                        self.tempConfigSelect.clearCurrPageRows();
                    }
                    _.forEach(self.cfgList, function (configInfo, index) {
                        if (configInfo.updFlg != 8) {
                            _.extend(configInfo, {mainKey: index});
                            self.tempConfigSelect.currPageRows({
                                "id": configInfo.mainKey,
                                "code": configInfo.cfgName,
                                "channelId": configInfo.orderChannelId,
                                "cfgName": configInfo.cfgName,
                                'cfgVal1': configInfo.cfgVal1
                            });
                        }
                    });
                    self.configSelList = self.tempConfigSelect.selectRowsInfo;

                    for (var l = 0; l < self.channelList.length; l++) {
                        self.tempChannelList.push({
                            'channelName': self.channelList[l].name,
                            'channelId': self.channelList[l].orderChannelId
                        })
                    }
                    for (var i = 0; i < self.cfgList.length; i++) {
                        self.tempChannelList.map(function (item) {
                            if (item.channelId === self.cfgList[i].orderChannelId) {
                                _.extend(self.cfgList[i], {'channelName': item.channelName});
                            }
                        });
                    }
                })
            },
            clear: function () {
                var self = this;
                self.searchInfo = {
                    channelId: "",
                    configType: 'Channel',
                    pageInfo: self.configPageOption,
                    channelName: "",
                    cfgName: '',
                    cfgVal: ''
                };
            },
            edit: function () {
                var self = this;
                _.forEach(self.cfgList, function (cfgInfo) {
                    if (cfgInfo.mainKey == self.configSelList.selList[0].id) {
                        self.popups.openCreateEdit(cfgInfo);
                        return;
                    }
                });
            },
            delete: function () {
                var self = this;
                var delList = [];
                _.forEach(self.configSelList.selList, function (delInfo) {
                    _.extend(delInfo, {'configType': 'Channel'});
                    delList.push(delInfo);
                });
                self.AdminChannelService.deleteConfig(delList).then(function (res) {
                    console.log(res);
                })
            },
            cancel: function () {
                this.$uibModalInstance.dismiss();
            },
            save: function () {

            }
        };
        return ConfigController;
    })())
});