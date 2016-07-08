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
        'angular-ngStorage': 'libs/angular-ngStorage/ngStorage',
        'voyageone-angular-com': 'components/dist/voyageone.angular.com'
    },
    shim: {
        'angular-block-ui': ['angular'],
        'voyageone-angular-com': ['angular'],
        'angular-cookies': ['angular'],
        'angular-ngStorage': ['angular'],
        'angular': {exports: 'angular'}
    }
});

// Bootstrap App !!
require([
    'angular',
    'angular-block-ui',
    'angular-cookies',
    'angular-ngStorage',
    'voyageone-angular-com'
], function (angular) {
    angular.module('voyageone.cms.channel', [
        'blockUI',
        'ngCookies',
        'ngStorage',
        'voyageone.angular'
    ]).controller('channelController', function ($scope, $ajax, cookieService, $localStorage, $window) {
        $ajax.post('/core/access/user/getChannel').then(function (res) {
            $scope.channels = res.data;
        }, function (res) {
            alert(res.message || res.code);
        });
        $scope.choose = function (channel, app) {
            $ajax.post('/core/access/user/selectChannel', {channelId: channel.channelId, applicationId:app.applicationId,application:app.application}).then(function () {
                cookieService.application(app.application);
                cookieService.channel(channel.channelId);
                // 2016-07-08 13:52:21
                // 成功后记录 channel, 用于缓存关键字
                // user 对象在 login 生成
                $localStorage.user.channel = channel.channelId;
                // 之后跳转相应的应用
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