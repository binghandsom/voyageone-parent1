/**
 * Created by sofia on 2016/8/10.
 */
define([
    'admin'
], function (admin) {
    admin.controller('ConfigController', (function () {
        function ConfigController(confirm, channelService) {
            this.confirm = confirm;
            this.channelService = channelService;
            this.show = false;
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
            },
            delete: function () {
                var self = this;
                self.confirm('TXT_CONFIRM_DELETE_MSG');
            }
        };
        return ConfigController;
    })())
});