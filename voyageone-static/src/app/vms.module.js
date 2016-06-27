define(function (require) {
    
    require('components/menu.component');
    require('controllers/topbar.controller');
    
    var angularAMD = require('angularAMD');
    
    var app = angular.module('vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng',
        'localytics.directives',
        'vms.menu',
        'vms.topbar'
    ]);  
    
    return angularAMD.bootstrap(app);
});