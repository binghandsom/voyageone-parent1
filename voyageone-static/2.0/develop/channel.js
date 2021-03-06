/**
 * @Description
 * Channel Select Page Main JS
 * @Date:    2015-11-30 14:04:37
 * @User:    Jonas
 * @Version: 2.0.0
 */

define([
    'underscore',
    'components/dist/common'
], function (_) {

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

        $scope.choose = function (channel) {

            var app = getCmsApp(channel);

            if(!app)
                return;

            $ajax.post('/core/access/user/selectChannel', {
                channelId: channel.channelId,
                applicationId: app.applicationId,
                application: app.application
            }).then(function () {
                cookieService.application(app.application);
                cookieService.channel(channel.channelId);
                // 2016-07-08 13:52:21
                // 成功后记录 channel, 用于缓存关键字
                // user 对象在 login 生成
                $localStorage.user.channel = channel.channelId;
                $localStorage.user.channelName = channel.channelName;
                // 之后跳转相应的应用

                /**
                 * 判断是中国CMS or 美国CMS
                 */
                $ajax.post('/cms/home/menu/getMenuHeaderInfo').then(function (res) {
                    let isCn = judgePath(res.data.menuTree);

                    $localStorage.isCn = isCn;

                    if(isCn)
                        location.href = 'modules/cms/app.html#/home';
                    else
                        location.href = 'modules/cms/app.html#/usa/home';
                });


            }, function (res) {
                alert(res.message || res.code);
            })
        };

        $scope.channelFilter = function (input) {

            return !!getCmsApp(input);
        };

        $scope.logout = function () {
            $ajax.post('/core/access/user/logout')
                .then(function () {
                    cookieService.removeAll();
                    $window.location = '/login.html';
                });
        };

        function getCmsApp(channel){
            return _.find(channel.apps, function (item) {
                return item.application === 'cms';
            });
        }

        function judgePath(menu){
            return _.some(menu, menu=>{
                return menu.resName === 'feed管理';
            })
        }

    });

    return angular.bootstrap(document, ['voyageone.cms.channel']);
});