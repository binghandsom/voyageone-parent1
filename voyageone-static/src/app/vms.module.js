define(function (require) {
    
    require('components/menu.component');
    
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
        'vms.menu'
    ]);  
    
    return angularAMD.bootstrap(app);
});