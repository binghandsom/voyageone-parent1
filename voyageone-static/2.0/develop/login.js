/**
 * @Description
 * Login Page Main JS
 * @Date:    2015-11-24 17:03:16
 * @User:    Jonas
 * @Version: 0.2.0
 */

require.config({
  paths: {
    'angular': 'libs/angular.js/1.4.7/angular',
    'angular-translate': 'libs/angular-translate/2.8.1/angular-translate',
    'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
    'css': 'libs/require-css/0.1.8/css'
  },
  shim: {
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'],
    'angular-translate': ['angular'],
    'angular': {exports: 'angular'}
  }
});

// Bootstrap App !!
require([
  'angular',
  'angular-block-ui',
  'angular-translate'
], function (angular) {
  angular.module('voyageone.cms.login', [
    'pascalprecht.translate',
    'blockUI'
  ]).controller('loginController', function($scope, $http) {
    $scope.username = '';
    $scope.password = '';
    $scope.isSavePwd = '';
    $scope.login = function() {
      alert($scope.username);
      alert($scope.password);
    };
  });
  return angular.bootstrap(document, ['voyageone.cms.login']);
});