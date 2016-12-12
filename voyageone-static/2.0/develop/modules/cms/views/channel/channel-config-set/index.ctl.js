define([
    'cms',
    './service.dev'
], function (cms) {

    cms.controller("channelConfigSet", (function () {

        function ChannelConfigSet(channelConfigService,confirm,notify) {
            this.channelConfigService = channelConfigService;
            this.confirm = confirm;
            this.notify = notify;
            this.searchInfo = {config_key:''}
        }

        ChannelConfigSet.prototype.init = function () {
            var self = this,
                channelConfigService = self.channelConfigService;

            channelConfigService.getChannelConfigs().then(function(res){
                self.dataList = res.data;
            });
        };

        ChannelConfigSet.prototype.clear = function(){
            this.searchInfo.config_key = '';
        };

        ChannelConfigSet.prototype.refresh = function(){
            this.init();
        };

        ChannelConfigSet.prototype.save = function(){
            var self = this,
                notify = self.notify,
                confirm = self.confirm,
                channelConfigService = self.channelConfigService;

            confirm("您是否要保存当前配置信息？").then(function(){
                channelConfigService.save().then(function(){
                    notify.success("保存成功！");
                });
            });
        };


        return ChannelConfigSet;

    })());

});