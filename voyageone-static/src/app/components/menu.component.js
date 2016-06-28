define(function () {
    return angular.module('vms.menu', []).component('voMenu', {
        templateUrl: 'components/menu.html',
        controller: function MenuController(menuService,notify,alert) {
            this.menus = [];
            var main = this;
            menuService.getVendorMenuHeaderInfo().then(function (res) {
                main.menus = res.menuInfo;
            })
        }
    });
});