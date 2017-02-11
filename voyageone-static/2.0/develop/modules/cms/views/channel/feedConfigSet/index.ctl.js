define([
    'cms'
], function (cms) {
    cms.controller("feedConfigSet", (function () {

        function feedConfigSet(cmsFeedConfigService, confirm, notify,cActions,popups) {
            this.cmsFeedConfigService = cmsFeedConfigService;
            this.confirm = confirm;
            this.notify = notify;
            this.cActions = cActions;
            this.popups = popups;
            this.displayCode = true;
            this.displaySku = true;
        }

        /**
         * Feed-Master属性一览*数据初始化
         */
        feedConfigSet.prototype.init = function () {
            var self = this;
            self.search();
        };
        feedConfigSet.prototype.search = function () {
            var self = this,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.search().then(function (res) {
                self.dataList = res.data.feedConfigList;
                self.feedList = res.data.feedList;
            });
        };

        /**
         * Feed-Master属性一览*保存按钮
         */
        feedConfigSet.prototype.save = function (item) {
            var self = this,
                notify = self.notify,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.save(item).then(function (res) {
                self.search();
                notify.success("保存成功！");
            });
        };

        /**
         * Feed-Master属性一览*刷新按钮
         */
        feedConfigSet.prototype.refresh = function () {
            this.init();
        };
        /**
         * Feed属性一览*按模板导出按钮
         */
        feedConfigSet.prototype.export = function (item) {
            var self = this,
                cActions = self.cActions;
                self.saveFeed(item);
                $.download.post(cActions.cms.channel.FeedConfig.cmsFeedConfigService.root + "/"
                + cActions.cms.channel.FeedConfig.cmsFeedConfigService.export);
        };

        /**
         * Feed属性一览*导入按钮
         */
        feedConfigSet.prototype.import = function (item) {
            var self = this,
                popups=self.popups;
                popups.openFeedConfigImport().then(function(){
                    self.search();
                });

        };

        /**
         * Feed属性一览*新增feed属性按钮
         */
        feedConfigSet.prototype.add = function (item) {
            //新增feed属性
            this.feedList.unshift({});
        };

        /**
         * Feed属性一览*删除按钮
         */
        feedConfigSet.prototype.delete = function (id,index) {
            var self = this,
                confirm = self.confirm,
                cmsFeedConfigService = self.cmsFeedConfigService;

            confirm("您是否要删除当前数据？").then(function(){
                if(id== undefined) {
                    self.feedList.splice(index,1);
                    return;
                }
                cmsFeedConfigService.delete(id).then(function (res) {
                    self.search();
                });
            });
        };

        /**
         * Feed属性一览*feed属性保存按钮
         */
        feedConfigSet.prototype.saveFeed = function (item) {
            var self = this,
                notify = self.notify,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.saveFeed(item).then(function (res) {
                self.search();
                notify.success("保存成功！");
            });
        };

        /**
         * Feed属性一览*feed表保存按钮
         */
        feedConfigSet.prototype.createFeed = function (item) {
            var self = this,
                notify = self.notify,
                cmsFeedConfigService = self.cmsFeedConfigService;

            cmsFeedConfigService.createFeed(item).then(function (res) {
                self.search();
                notify.success("保存成功！");
            });
        };
        return feedConfigSet;
    })());
});