define(function (require) {

    require('components/menu.component');
    require('controllers/topbar.controller');
    require('./popups.factory');

    var angularAMD = require('angularAMD');
    var services = require('./actions.services');
    var routes = require('./routes');
    var _ = require('underscore');

    var en = require('./translate/en');
    var zh = require('./translate/zh');

    var app = angular.module('vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'ngStorage',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng',
        'localytics.directives',
        'angular-md5',
        'vms.menu',
        'vms.topbar',
        'vms.popups'
    ]).config(function ($routeProvider, $translateProvider) {

        $translateProvider.translations('zh', zh);
        $translateProvider.translations('en', en);

        _.each(routes, function (module) {
            return $routeProvider.when(module.hash, angularAMD.route(module));
        });
    });

    function eachDeclareService(_services) {
        _.each(_services, function (content, key) {
            if (content instanceof CommonDataService) {
                app.service(key, content.getDeclare());
            } else if (_.isObject(content)) {
                eachDeclareService(content);
            }
        });
    }

    eachDeclareService(services);

    return angularAMD.bootstrap(app);
});