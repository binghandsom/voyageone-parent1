/**
 * @Description
 * Channel Select Page Main JS
 * @Date:    2015-11-30 14:04:37
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
    paths: {
        'angular': 'libs/angular.js/1.5.6/angular',
        'angular-cookies': 'libs/angular.js/1.5.6/angular-cookies',
        'angular-block-ui': 'libs/angular-block-ui/0.2.1/angular-block-ui',
        'voyageone-angular-com': 'components/dist/voyageone.angular.com',
        'css': 'libs/require-css/0.1.8/css'
    },
    shim: {
        'angular-block-ui': ['angular', 'css!libs/angular-block-ui/0.2.1/angular-block-ui.min.css'],
        'voyageone-angular-com': ['angular'],
        'angular-cookies': ['angular'],
        'angular': {exports: 'angular'}
    }
});

// Bootstrap App !!
require([
    'angular',
    'angular-block-ui',
    'angular-cookies',
    'voyageone-angular-com'
], function (angular) {
    angular.module('voyageone.cms.channel', [
        'blockUI',
        'ngCookies',
        'voyageone.angular'
    ]).controller('channelController', function ($scope, $ajax, cookieService, $window) {
        $ajax.post('/core/access/user/getChannel').then(function (res) {
            $scope.channels = res.data;
        }, function (res) {
            alert(res.message || res.code);
        });
        $scope.choose = function (channel, app) {
            $ajax.post('/core/access/user/selectChannel', {channelId: channel.channelId, applicationId:app.applicationId,application:app.application}).then(function (res) {
                cookieService.application(app.application);
                cookieService.channel(channel.channelId);
                location.href = 'modules/' + app.application + '/app.html#/home';
            }, function (res) {
                alert(res.message || res.code);
            })
        };
        $scope.logout = function () {
            $ajax.post('/core/access/user/logout')
                .then(function () {
                    cookieService.removeAll();
                    $window.location = '/login.html';
                });
        }
    });
    return angular.bootstrap(document, ['voyageone.cms.channel']);
});