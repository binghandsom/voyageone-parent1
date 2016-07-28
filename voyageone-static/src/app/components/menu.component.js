define(function () {
    return angular.module('vms.menu', []).component('voMenu', {
        templateUrl: 'components/menu.html',
        controller: 'MenuController'
    }).controller('MenuController', (function () {
        function MenuController(menuService, $window) {
            this.menus = [];
            this.$window = $window;
            var main = this;
            menuService.getVendorMenuHeaderInfo().then(function (res) {
                main.menus = res.menuInfo.map(function (menu) {
                    menu.open = false;
                    return menu;
                })
            })
        }

        MenuController.prototype = {
            selectMenu: function (url) {
                sessionStorage.clear();
                this.$window.location = url;
            }
        };

        return MenuController;
    }()));
});