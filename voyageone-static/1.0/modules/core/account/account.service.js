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

    coreApp.service ('companyService', ['$q', 'coreAction', 'ajaxService', 'userService',
        function ($q, coreAction, ajaxService, userService) {

            var _ = require ('underscore');

            /**
             * get the company list by user.
             * @returns {*}
             */
            this.doGetCompany = function () {
                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (coreAction.core_account_company_doGetCompany)
                    .then (addGetInCompanyIntoCompanyList)
                    .then (function (data) {
                    defer.resolve (data);
                });
                return defer.promise;
            };

            /**
             * set the selected company.
             * @param companyId
             */
            this.doSelectCompany = function (companyId) {
                var defer = $q.defer ();
                ajaxService.ajaxPostByIdWithoutValidate (companyId, coreAction.core_account_company_doSelectCompany)
                    .then (function (response) {
                    userService.setSelCompany (companyId);
                    defer.resolve (response);
                });
                return defer.promise;
            };

            /**
             * get the response.data.companyList and add item:shortName/getInName to companyList.
             * @param response
             * @returns {*}
             */
            function addGetInCompanyIntoCompanyList (response) {
                var companyList = response.data.companyList;

                for (var i = 0; i < companyList.length; i++) {

                    // add shortName.
                    var shortName = {"shortName": companyList[i].companyName.substr (0, 1)};
                    _.extend (companyList[i], shortName);
                }
                return companyList;
            }
        }]);
});