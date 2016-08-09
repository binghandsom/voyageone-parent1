define(function () {
    return angular.module('vms.topbar', []).controller('TopBarController', (function () {
        function TopBarController($window,userService, menuService) {
            this.language = '';
            this.userName = '';
            this.selChannel = {};
            this.channelList = [];
            this.$window = $window;
            this.userService = userService;
            this.menuService = menuService;

            var main = this;
            menuService.getVendorMenuHeaderInfo().then(function (res) {
                main.language = res.userInfo.language;
                main.userName = res.userInfo.userName;
                main.selChannel = res.userInfo.selChannel;
                main.channelList = res.userInfo.channelList;
            })
        }

        TopBarController.prototype = {

            setChannel: function (channelId) {
                var main = this;
                main.menuService.setChannel({"channelId":channelId})
                    .then(function () {
                        main.$window.location = '/app/app.html';
                    });
            },

            logout: function () {
                var main = this;
                main.userService.logout('/core/access/user/logout')
                    .then(function () {
                        main.$window.location = '/login.html';
                    });
            }
        };

        return TopBarController;
    }()));
});