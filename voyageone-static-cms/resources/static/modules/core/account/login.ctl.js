/**
 * @Name:    loginController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var coreApp = require ('modules/core/core.module');
    require ('modules/core/account/account.service');

    coreApp.controller ('loginController', ['$scope', '$location', 'loginService', 'coreRoute',
        function ($scope, $location, loginService, coreRoute) {

            $scope.initialize = function () {
                $scope.user = {};
                loginService.loginInit ();
            };

            /**
             * �ύ��¼��Ϣ.
             */
            $scope.doLogin = function () {
                if ($scope.loginForm.$valid) {

                    var d = new Date ();
                    $scope.user.timezone = d.getTimezoneOffset () / 60 * -1;
                    loginService.doLogin ($scope.user, $scope)
                        .then (function (response) {
                        // TODO 临时这样写，因为影响了现有voyageone.net(oms)还没有合并进来.
                        $location.path(coreRoute.core_account_company.hash);
                    });
                }
            };
        }]);
});