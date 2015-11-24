/**
 * @Description
 * Bootstrap Main App
 * @Date:    2015-11-19 20:31:14
 * @User:    Jonas
 * @Version: 0.2.0
 */

require.config({
  paths: {
    'voyageone-angular-com': 'components/dist/voyageone.angular.com',
    'voyageone-com': 'components/dist/voyageone.com',
    'angular-animate': 'libs/angular.js/1.4.7/angular-animate',
    'angular-route': 'libs/angular.js/1.4.7/angular-route',
    'angular-sanitize': 'libs/angular.js/1.4.7/angular-sanitize',
    'angular-cookies': 'libs/angular.js/1.4.7/angular-cookies',
    'angular': 'libs/angular.js/1.4.7/angular',
    'angular-translate': 'libs/angular-translate/2.8.1/angular-translate',
    'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
    'angularAMD': 'libs/angularAMD/0.2.1/angularAMD.min',
    'ngload': 'libs/angularAMD/0.2.1/ngload.min',
    'jquery': 'libs/jquery/2.1.4/jquery',
    'underscore': 'libs/underscore.js/1.8.3/underscore',
    'css': 'libs/require-css/0.1.8/css',
    'json': 'libs/requirejs-plugins/1.0.3/json',
    'text': 'libs/require-text/2.0.12/text'
  },
  shim: {
    'voyageone-angular-com': ['angularAMD'],
    'angular-sanitize': ['angular'],
    'angular-route': ['angular'],
    'angular-animate': ['angular'],
    'angular-cookies': ['angular'],
    'angular-translate': ['angular'],
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'],
    'angular': {exports: 'angular', deps: ['jquery']},
    'jquery': {exports: 'jQuery'},
    'json': ['text'],
    'angularAMD': [
      'angular',
      'angular-route',
      'angular-sanitize',
      'angular-animate',
      'angular-translate',
      'angular-cookies',
      'angular-block-ui',
      'ngload'
    ]
  }
});

// Bootstrap App !!
requirejs([
  'angularAMD',
  'angular',
  'underscore',
  'json!views/routes.json',
  'voyageone-angular-com',
  'voyageone-com'
], function (angularAMD, angular, _, routes) {
  var mainApp = angular.module('voyageone.cms', [
      'ngRoute',
      'ngAnimate',
      'ngCookies',
      'ngSanitize',
      'pascalprecht.translate',
      'blockUI',
      'voyageone.angular'
    ])

    .config(function (blockUIConfig) {
      blockUIConfig.autoBlock = false;
    })

    .config(function ($routeProvider) {
      return _.each(routes, function (module) {
        _.each(module, function (route, hash) {
          $routeProvider.when(hash, angularAMD.route(route));
        });
      });
    });

  return angularAMD.bootstrap(mainApp);
});