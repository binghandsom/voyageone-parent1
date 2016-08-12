/**
 * Created by sofia on 2016/8/12.
 */

define(['components/dist/voyageone.angular.com'], function () {
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
            location.href = 'modules/admin/app.html';
        };
    });
    return angular.bootstrap(document, ['voyageone.admin.login']);
});