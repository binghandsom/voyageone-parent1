/**
 * Created by sofia on 2016/8/12.
 */

define(['components/dist/common'], function () {
    angular.module('voyageone.admin.login', [
        'blockUI',
        'ngStorage',
        'voyageone.angular'
    ]).controller('adminLoginController', function ($scope, $ajax, $localStorage, $sessionStorage) {

        $scope.username = '';
        $scope.password = '';
        $scope.isSavePwd = false;
        $scope.errorMessage = '';

        $sessionStorage.$reset();

        $scope.login = function () {
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
                timezone: -(new Date().getTimezoneOffset() / 60),
                application: 'admin'
            }).then(function () {
                $localStorage.user = {
                    name: $scope.username
                };
                // 成功后跳转
                location.href = 'modules/admin/app.html';
            }, function (res) {
                $scope.errorMessage = res.message || ('登录失败(' + (res.code || '?') + ')');
            });
        };
    });
    return angular.bootstrap(document, ['voyageone.admin.login']);
});