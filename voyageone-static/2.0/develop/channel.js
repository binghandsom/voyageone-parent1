/**
 * @Description
 * Channel Select Page Main JS
 * @Date:    2015-11-30 14:04:37
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
  paths: {
    'angular': 'libs/angular.js/1.4.7/angular',
    'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
    'css': 'libs/require-css/0.1.8/css'
  },
  shim: {
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'],
    'angular': {exports: 'angular'}
  }
});

// Bootstrap App !!
require([
  'angular',
  'angular-block-ui'
], function (angular) {
  angular.module('voyageone.cms.channel', [
    'blockUI'
  ]).controller('channelController', function($scope, $http) {
    $scope.choose = function(app) {
      location.href = 'views/' + app + '/app.html';
    };
    $http.post('/core/access/user/getChannel').then(function(response) {
      var res = response.data;
      if (res.message) {
        alert(res.message);
        return;
      }
      $scope.channels = res.result.data;
    });
  });
  return angular.bootstrap(document, ['voyageone.cms.channel']);
});