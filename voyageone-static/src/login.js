/**
 * @Date: 2016-06-24 17:35:36
 * @User: Jonas
 */
define([
    'angular',
    'angular-block-ui',
    'angular-translate',
    'angular-vo'
], function (angular) {
    angular.module('voyageone.cms.login', [
        'pascalprecht.translate',
        'blockUI',
        'vo.ng'
    ]).controller('loginController', function ($scope, $ajax) {
        $scope.username = '';
        $scope.password = '';

        $scope.login = function () {

            $scope.dirty = true;

            if ($scope.loginForm.$invalid)
                return;


            if ($scope.username === 'a') {
                $scope.message = 'aaaaaaa';
                return;
            }
            location.href = '/app/app.html';
        };
    });
    return angular.bootstrap(document, ['voyageone.cms.login']);
});