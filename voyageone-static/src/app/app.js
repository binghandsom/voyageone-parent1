define('vo-vms', [
    'angularAMD',
    'underscore',
    'angular-sanitize',
    'angular-route',
    'angular-animate',
    'angular-cookies',
    'angular-translate',
    'angular-block-ui',
    'angular-ui-bootstrap',
    'vo-libs-angular'
], function (angularAMD, _) {

    var app = angular.module('vo.vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng'
    ]);

    return angularAMD.bootstrap(app);
});