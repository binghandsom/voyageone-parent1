/**
 * @Description
 * Login Page Main JS
 * @Date:    2015-11-24 17:03:16
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
  paths: {
    'angular': 'libs/angular.js/1.5.6/angular',
    "voyageone-angular-com" : 'components/dist/voyageone.angular.com',
    'angular-translate': 'libs/angular-translate/2.8.1/angular-translate',
    'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
    'css': 'libs/require-css/0.1.8/css'
  },
  shim: {
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.min.css'],
    'angular-translate': ['angular'],
    'voyageone-angular-com': ['angular'],
    'angular': {exports: 'angular'}
  }
});

// Bootstrap App !!
require([
  'angular',
  'angular-block-ui',
  'angular-translate',
  'voyageone-angular-com'
], function (angular) {
  angular.module('voyageone.cms.login', [
    'pascalprecht.translate',
    'blockUI',
    'voyageone.angular'
  ]).controller('loginController', function($scope, $ajax) {
    $scope.username = '';
    $scope.password = '';
    $scope.isSavePwd = false;
    $scope.errorMessage = '';

    $scope.login = function() {
      if (!$scope.username || !$scope.username.length) {
        $scope.errorMessage = '用户名必须填写';
        return;
      }
      if (!$scope.password || !$scope.password.length) {
        $scope.errorMessage = '密码必须填写';
        return;
      }
      $scope.errorMessage = '';
      $ajax.post('/core/access/user/login', {
        username: $scope.username,
        password: $scope.password,


        timezone: -(new Date().getTimezoneOffset() / 60)
      }).then(function(){
        location.href = 'channel.html';
      }, function(res) {
        $scope.errorMessage = res.message || ('登录失败(' + (res.code || '?') + ')');
      });
    };
  });
  return angular.bootstrap(document, ['voyageone.cms.login']);
});