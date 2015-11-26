/**
 * @Name:    indexService.js
 * @Date:    2015/2/13
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("modules/app/app.module");
    require ('modules/app/services/user.service');
    require ('components/services/ajax.service');

    mainApp.service ('commonService', ['$q', 'ajaxService', 'userService', 'cookieService', 'commonAction',
        function ($q, ajaxService, userService, cookieService, commonAction) {

            var commonUtil = require ('components/util/commonUtil');

            /**
             * get userInfo  from server.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetUserInfo = function () {
                var defer = $q.defer ();

                ajaxService.ajaxPostOnlyByUrl (commonAction.common_doGetUserInfo)
                    .then (function (response) {
                    var userInfo = response.data.userInfo;
                    userService.setUserInfo (userInfo);
                    userInfo.loginTime = cookieService.getLoginTime ();
                    userInfo.selMenu = userService.getSelMenu ();
                    userService.setCompanyInfo (userInfo.companyList);
                    defer.resolve (userInfo);
                });
                return defer.promise;
            };

            /**
             * get companyList from server.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doGetCompany = function () {
                var defer = $q.defer ();
                ajaxService.ajaxPostOnlyByUrl (commonAction.common_doGetCompany)
                    .then (function (response) {
                    userService.setCompanyInfo (response.data.companyList);
                    defer.resolve (response.data.companyList);
                });
                return defer.promise;
            };

            /**
             * logout clear hte cookie, and init the userInfo.
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doLogout = function () {
                var defer = $q.defer ();

                ajaxService.ajaxPostOnlyByUrl (commonAction.common_doLogout)
                    .then (function () {
                    sessionStorage.clear();
                    userService.init ();
                    cookieService.init ();
                    defer.resolve ('');
                })
                    .then (function () {
                    commonUtil.goToLoginPage ();
                });
                return defer.promise;
            };

            /**
             * update the selected company to server.
             * @param companyId
             */
            this.doSelectCompany = function (companyId) {
                var defer = $q.defer ();

                ajaxService.ajaxPostByIdWithoutValidate (companyId, commonAction.common_doSelectCompany)
                    .then (function (response) {
                    userService.setSelCompany (companyId);
                    defer.resolve (response.next);
                });
                return defer.promise;
            };

            /**
             * update the selected language to server.
             * @param language
             * @returns {r.promise|promise|qFactory.Deferred.promise|x.ready.promise|fd.g.promise}
             */
            this.doSelectLanguage = function (language) {
                var defer = $q.defer ();
                userService.setSelLanguage (language)
                    .then (function () {
                    defer.resolve ('');
                });
                return defer.promise;
            };

            /**
             * change menu to userInfo.
             * @param menu
             */
            this.selectMenu = function (menu) {
                var defer = $q.defer ();
                userService.setSelMenu (menu);
                defer.resolve ('');
                return defer.promise;
            };

            /**
             * return this page's navigationList.
             * @returns {*}
             */
            this.getCurrentNavigationInfo = function () {

                return userService.getCurrentNavigationInfo ();
            };
            

            this.doChgPassword = function (data){
                var defer = $q.defer ();
                ajaxService.ajaxPost (data, commonAction.common_doChgPassword, null)
                    .then (function (response) {
                    defer.resolve (response);
                },function(response){
                	defer.reject (response);
                });
                return defer.promise;
            }
        }]);
});