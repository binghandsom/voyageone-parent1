define(function () {
    return angular.module('vms.topbar', []).controller('TopBarController', (function () {
        function TopBarController() {
            this.language = 'EN';
            this.channel = {name: 'JEWELRY'};
        }
        return TopBarController;
    }()));
});