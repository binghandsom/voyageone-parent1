/**
 * @Description
 * Login Page Main JS
 * @Date:    2015-11-24 17:03:16
 * @User:    Jonas
 * @Version: 2.0.0
 */

require.config({
    paths: {
        'angular': 'assets/js/angular.js/1.5.6/angular',
        "angular-vo": 'components/components.ng',
        'angular-translate': 'assets/js/angular-translate/2.8.1/angular-translate',
        'angular-block-ui': 'assets/js/angular-block-ui/0.2.1/angular-block-ui',
        'css': 'assets/js/require-css/0.1.8/css'
    },
    shim: {
        'angular-block-ui': ['angular', 'css!assets/js/angular-block-ui/0.2.1/angular-block-ui.min.css'],
        'angular-translate': ['angular'],
        'angular-vo': ['angular'],
        'angular': {exports: 'angular'}
    }
});

// Bootstrap App !!
require([
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