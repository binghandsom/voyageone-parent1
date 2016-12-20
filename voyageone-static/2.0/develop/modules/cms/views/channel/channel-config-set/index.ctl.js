define([
    'cms'
], function (cms) {

    var checkEntity = {
        '0': false,
        '1': true
    };

    cms.controller("channelConfigSet", (function () {

        function ChannelConfigSet(cmsMTChannelConfigService, confirm, notify) {
            this.cmsMTChannelConfigService = cmsMTChannelConfigService;
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo = {configKey: ''}
        }

        ChannelConfigSet.prototype.init = function () {
            var self = this;

            self.search();
        };

        ChannelConfigSet.prototype.search = function () {
            var self = this,
                searchInfo = self.searchInfo,
                cmsMTChannelConfigService = self.cmsMTChannelConfigService;

            cmsMTChannelConfigService.search(searchInfo).then(function (res) {
                self.dataList = _.filter(res.data, function (item) {
                    return item.isChecked === checkEntity[searchInfo.isChecked] || !searchInfo.isChecked;
                });
            });
        };

        ChannelConfigSet.prototype.clear = function () {
            this.searchInfo.configKey = '';
            this.searchInfo.isChecked = null;
        };

        ChannelConfigSet.prototype.refresh = function () {
            this.init();
        };

        ChannelConfigSet.prototype.changeValueList = function (element) {

            element.selected = true;
        };

        ChannelConfigSet.prototype.save = function () {
            var self = this,
                notify = self.notify,
                confirm = self.confirm,
                selectEles = [],
                cmsMTChannelConfigService = self.cmsMTChannelConfigService;

            selectEles = _.filter(self.dataList, function (item) {
                return item.selected
            });

            _.each(self.dataList, function (ele) {
                delete ele.selected;
            });

            confirm("您是否要保存当前配置信息？").then(function () {
                cmsMTChannelConfigService.saveList({
                    list: selectEles
                }).then(function () {
                    self.search();
                    notify.success("保存成功！");
                });
            });
        };

        return ChannelConfigSet;

    })());

});