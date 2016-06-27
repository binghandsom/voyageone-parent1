/**
 * @Date: 2016-06-24 17:35:36
 * @User: Jonas
 */
require.config({
    paths: {
        'ng': 'assets/js/angular.js/1.5.6/angular',
        'ng-traslate': 'assets/js/angular-translate/2.8.1/angular-translate',
        'vo-ng': 'shared/components.ng',
        'ng-block': 'assets/js/angular-block-ui/0.2.1/angular-block-ui'
    },
    shim: {
        'ng-traslate': ['ng'],
        'vo-ng': ['ng'],
        'ng-block': ['ng']
    }
});

require([
    'ng-traslate',
    'vo-ng',
    'ng-block'
], function () {

    alert(1);

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
});