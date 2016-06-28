define(function () {
    return angular.module('vms.topbar', []).controller('TopBarController', (function () {
        function TopBarController($window, $ajax, menuService,alert) {
            //this.language = 'EN';
            //this.channel = {name: 'JEWELRY'};

            this.language = '';
            this.userName = '';
            this.selChannel = {};
            this.channelList = [];
            this.$ajax = $ajax;
            this.$window = $window;
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

            logout: function () {
                var main = this;
                main.$ajax.post('/core/access/user/logout')
                    .then(function () {
                        main.$window.location = '/login.html';
                    });
            }
        }

        return TopBarController;
    }()));
});