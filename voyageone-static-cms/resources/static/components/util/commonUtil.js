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
        'index': '/VoyageOne/login.html#',
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
        commonUtil.goToSystemPage(mainPage.home);
    };

    commonUtil.goToSystemPage = function (name) {
        window.location = mainHtml.master + name;
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
     * Replace the special char to replaceValue.
     * @param value
     * @param replaceValue
     * @returns {*}
     */
    commonUtil.replaceSpecialChar = function (value, replaceValue) {

        if (_.isEmpty (value)) {
            return "";
        } else {
            return value.replace (/[^a-zA-Z0-9 -]/g, replaceValue);
        }
    };

    /**
     * Replace blank space to hyphen.
     * @param value
     * @returns {*}
     */
    commonUtil.replaceSpaceToHyphen = function (value) {

        if (_.isEmpty (value)) {
            return "";
        } else {
            return value.replace (/ /g, "-");
        }
    };

    /**
     * 返回被替换掉的UrlPath
     * @param path
     * @param value
     */
    commonUtil.returnReallyPath = function (path, value) {
        if (!_.isEmpty (path)) {
            var replaceValue = path.substring (path.indexOf (':'), path.length);
            return path.replace (replaceValue, value);
        } else {
            return  '';
        }
    };

    /**
     * 返回被替换掉的UrlPath
     * @param path
     * @param values
     * @returns {*}
     */
    commonUtil.returnReallyPathByMoreParam = function (path, values) {
        if (!_.isEmpty (path)) {
            var replaceValues = commonUtil.splitByChar( path.substring (path.indexOf(':'), path.length), '/');
            _.each(replaceValues, function(replaceValue, index) {
                path = path.replace (replaceValue, values[index]);
            });

            return path;
        } else {
            return  '';
        }
    };

    /**
     * 将日期转换为yyyy/MM/dd格式
     * @param data_inicial
     * @returns {*}
     */
     commonUtil.doFormatDate = function (data_inicial) {
        try {
            var date = '0' + data_inicial.getDate ();
            var month = '0' + (data_inicial.getMonth () + 1); //Months are zero based
            var year = data_inicial.getFullYear ();

            date = date.substr (date.length - 2, 2);
            month = month.substr (month.length - 2, 2);

            return year + "-" + month + "-" + date;
        } catch (e) {
            return data_inicial;
        }
    };

    /**
     * 检测value是否不为undefined, null, empty.
     * @param value
     * @returns {boolean}
     */
    commonUtil.isNotEmpty = function (value) {

        if (_.isUndefined(value) || _.isNull(value) || _.isEqual(value, ''))
            return false;
        else
            return true;
    };

    /**
     * 根据指定的符号将string转成数组
     * @param value
     * @param splitValue
     * @returns {*}
     */
    commonUtil.splitByChar = function (value, splitValue) {
        if (commonUtil.isNotEmpty(value)) {
            return value.split(splitValue);
        }

        return '';
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
