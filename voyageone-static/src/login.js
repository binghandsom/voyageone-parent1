/**
 * @Date: 2016-06-24 17:35:36
 * @User: Jonas
 */

require('assets/js/angular.js/1.5.6/angular.js');
require('assets/js/angular-translate/2.8.1/angular-translate.js');
require('shared/components.ng.js');
require('assets/js/angular-block-ui/0.2.1/angular-block-ui.js');

angular.module('vo.cms.login', [
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

            alert(1);

            $scope.message = 'aaaaaaa';
            return;
        }
        location.href = '/app/app.html';
    };
});

angular.bootstrap(document, ['vo.cms.login']);