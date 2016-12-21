define([
    'cms'
], function (cms) {
    cms.controller("feedConfigSet", (function () {

        function feedConfigSet(cmsFeedConfigService, confirm, notify) {
            this.cmsFeedConfigService = cmsFeedConfigService;
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo = {configKey: ''}
        }

        /**
         * 数据初始化
         */
        feedConfigSet.prototype.init = function () {
            var self = this;
            self.search();
        };
        feedConfigSet.prototype.search = function () {
            var self = this,
                searchInfo = self.searchInfo,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.search(searchInfo).then(function (res) {
                self.dataList=res.data.configs;
            });
        };

        /**
        * 数据保存
        */
        feedConfigSet.prototype.save = function (item) {
            var self = this,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.save(item).then(function (res) {
                self.search();
        });
        };

        feedConfigSet.prototype.refresh = function () {
            this.init();
        };
        return feedConfigSet;
    })());
});