define([
    'cms'
], function (cms) {

    cms.controller("channelConfigSet", (function () {

        function ChannelConfigSet(cmsMTChannelConfigService,confirm,notify) {
            this.cmsMTChannelConfigService = cmsMTChannelConfigService;
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo = {configKey:''}
        }

        ChannelConfigSet.prototype.init = function () {
            var self = this,
                cmsMTChannelConfigService = self.cmsMTChannelConfigService;

            cmsMTChannelConfigService.search(self.searchInfo).then(function(res){
                self.dataList = res.data;
            });
        };

        ChannelConfigSet.prototype.clear = function(){
            this.searchInfo.configKey = '';
        };

        ChannelConfigSet.prototype.refresh = function(){
            this.init();
        };

        ChannelConfigSet.prototype.save = function(){
            var self = this,
                notify = self.notify,
                confirm = self.confirm,
                cmsMTChannelConfigService = self.cmsMTChannelConfigService;

            confirm("您是否要保存当前配置信息？").then(function(){
                cmsMTChannelConfigService.save().then(function(){
                    notify.success("保存成功！");
                });
            });
        };


        return ChannelConfigSet;

    })());

});