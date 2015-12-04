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
    'voyageone-angular-com' : 'components/dist/voyageone.angular.com',
    'css': 'libs/require-css/0.1.8/css'
  },
  shim: {
    'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.css'],
    'voyageone-angular-com': ['angular'],
    'angular': {exports: 'angular'}
  }
});

// Bootstrap App !!
require([
  'angular',
  'angular-block-ui',
  'voyageone-angular-com'
], function (angular) {
  angular.module('voyageone.cms.channel', [
    'blockUI',
    'voyageone.angular'
  ]).controller('channelController', function($scope, ajaxService) {
    ajaxService.post('/core/access/user/getChannel').then(function(res) {
      $scope.channels = res.data;
    }, function(res) { alert(res.message || res.code); });
    $scope.choose = function(channel, app) {
      ajaxService.post('/core/access/user/selectChannel', {channelId: channel.channelId}).then(function(res) {
        location.href = 'modules/' + app + '/app.html';
      }, function(res) { alert(res.message || res.code); })
    };
  });
  return angular.bootstrap(document, ['voyageone.cms.channel']);
});