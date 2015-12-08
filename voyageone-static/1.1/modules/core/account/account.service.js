/**
 * @Name:    loginService.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var coreApp = require ('modules/core/core.module');

    coreApp.service ('loginService', ['$q', 'coreAction', 'ajaxService', 'userService', 'cookieService',
        function ($q, coreAction, ajaxService, userService, cookieService) {

            /**
             * set the default language,and init the cookie values.
             */
            this.loginInit = function () {
                userService.init ();
                cookieService.init ();
            };

            /**
             * check the login user access.
             * @param data
             * @param scope
             * @returns {*}
             */
            this.doLogin = function (data, scope) {
                var defer = $q.defer ();
                ajaxService.ajaxPost (data, coreAction.core_account_login_doLogin, scope)
                    .then (function (response) {
                    userService.setIsLogin (true);
                    defer.resolve (response);
                });
                return defer.promise;
            };
        }]);

    coreApp.service('systemSelectService',['$q', 'coreAction', 'ajaxService', 'userService', 'cookieService',
        function ($q, coreAction, ajaxService, userService, cookieService) {

            var _ = require ('underscore');

            /**
             * get the company list by user.
             * @returns {*}
             */
            this.doGetCompanyList = function () {

                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (coreAction.core_account_company_doGetCompany)
                    .then (function (response) {
                    userService.setSelLanguage (cookieService.getSelLanguage ());
                    defer.resolve (response.data);
                });
                return defer.promise;
            };

        }]);
});