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

        ///**
        // * 数据初始化
        // */
        //feedConfigSet.prototype.init = function () {
        //    var self = this;
        //    self.search();
        //};
        //ChannelConfigSet.prototype.search = function () {
        //    var self = this,
        //        searchInfo = self.searchInfo,
        //        cmsFeedConfigService = self.cmsFeedConfigService;
        //
        //    cmsFeedConfigService.search(searchInfo).then(function (res) {
        //    });
        //};
        //
        ///**
        // * 数据保存
        // */
        //ChannelConfigSet.prototype.save = function () {
        //
        //};
        return feedConfigSet;
    })());
});