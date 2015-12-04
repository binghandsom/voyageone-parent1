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

    coreApp.controller ('loginController', ['$scope', '$location', 'loginService', 'cookieService',
        function ($scope, $location, loginService, cookieService) {

            var _ = require ('underscore');

            // initial login page.
            $scope.initialize = function () {
                $scope.user = {};
                loginService.loginInit ();
            };

            // check the login userInfo by service.
            $scope.doLogin = function () {
                if ($scope.loginForm.$valid) {

                    var d = new Date ();
                    $scope.user.timezone = d.getTimezoneOffset () / 60 * -1;
                    loginService.doLogin ($scope.user, $scope)
                        .then (function (response) {
                        loginSuccessFn (response);
                    });
                }
            };

            // operation when the login is successful.
            function loginSuccessFn (response) {
                var commonUtil = require('components/util/commonUtil');
                //if(commonUtil.checkLoginGoTo(response.next))
                //    commonUtil.goToHomePage();
                //else
                //    $location.path(response.next);
                cookieService.setSelCompany (response.data.companyId);
                if(commonUtil.checkLoginGoTo(response.next))
                    commonUtil.goToHomePage();
                else
                    $location.path(response.next);
            }
        }]);
});