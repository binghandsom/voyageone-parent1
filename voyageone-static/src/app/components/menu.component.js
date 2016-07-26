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
                main.menus = res.menuInfo;
            })
        }

        MenuController.prototype = {
            selectMenu: function (url) {
                sessionStorage.clear();
                this.$window.location = url;
            }
        }

        return MenuController;
    }()));
});