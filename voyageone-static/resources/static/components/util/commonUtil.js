/**
 * @Name:    commonUtil.js
 * @Date:    2015/2/15
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var _ = require ('underscore');
    var mainHtml = {
        'index': '/VoyageOne/index.html#',
        'master': '/VoyageOne/app.html#'
    };
    var mainPage = {
        'login': '/core/account/login',
        'error': '/app/common/error',
        'home': '/core/menu/home'
    };
    var commonUtil = {};

    commonUtil.loadController = function ($q, $rootScope, fileName) {
        var defer = $q.defer ();
        require ([fileName], function () {
            $rootScope.$apply (function () {
                defer.resolve ();
            });
        });
        return defer.promise;
    };

    commonUtil.goToLoginPage = function () {
        window.location = mainHtml.index + mainPage.login;
    };

    commonUtil.goToErrorPage = function () {
        window.location = mainHtml.index + mainPage.error;
    };

    commonUtil.goToHomePage = function () {
        window.location = mainHtml.master + mainPage.home;
    };

    commonUtil.checkLoginGoTo = function (value) {
        if (_.isEqual (mainPage.home, value)) {
            return true;
        } else {
            return false;
        }
    };

    /**
     * cut the '/' when the url is started by '/'.
     * @param value
     * @returns {*}
     */
    commonUtil.replaceImageUrl = function (value) {

        if (value == null) {
            return null;
        }
        if (value.startWith ('/'))
            return value.substr (1, value.length);
        else
            return value;
    };

    /**
     * check the string start with value.
     * @param value
     * @returns {boolean}
     */
    String.prototype.startWith = function (value) {
        var reg = new RegExp ("^" + value);
        return reg.test (this);
    };

    /**
     * check the string end with value.
     * @param value
     * @returns {boolean}
     */
    String.prototype.endsWith = function (value) {
        var reg = new RegExp (value + "$");
        return reg.test (this);
    };

    return commonUtil;
});
