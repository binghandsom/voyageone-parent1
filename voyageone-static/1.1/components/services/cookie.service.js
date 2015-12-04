/**
 * @Name:    cookieService.js
 * @Date:    2015/2/13
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(function(require) {

    var mainApp = require('components/app');

    // define cookieService to save and clear cookie info.
    mainApp.service('cookieService', ['$cookieStore',
        function($cookieStore) {

            var _ = require('underscore');
            var cookie = {
                'token': 'voyageOne.user.token',
                'language': 'voyageOne.user.language',
                'company': 'voyageOne.user.company',
                'channel': 'voyageOne.user.channel',
                'loginTime': 'voyageOne.user.loginTime'
            };

            /**
             * init all cookie value.
             */
            this.init = function () {
                $cookieStore.put(cookie.token, '');
                $cookieStore.put(cookie.company, '');
                $cookieStore.put(cookie.channel, '');
                $cookieStore.put(cookie.loginTime, _.now());
            };

            /**
             * remove all cookie.
             */
            this.removeAll = function () {
                $cookieStore.remove(cookie.token);
                $cookieStore.remove(cookie.company);
                $cookieStore.remove(cookie.channel);
                $cookieStore.remove(cookie.loginTime);
            };

            this.getToken = function () {
                return _.isNaN($cookieStore.get(cookie.token)) ? '' : $cookieStore.get(cookie.token);
            };

            this.setToken = function (value) {
                $cookieStore.put(cookie.token, value);
            };

            this.getSelLanguage = function () {
                return _.isNaN($cookieStore.get(cookie.language)) ? '' : $cookieStore.get(cookie.language);
            };

            this.setSelLanguage = function (value) {
                $cookieStore.put(cookie.language, value);
            };

            this.getSelCompany = function () {
                return _.isNaN($cookieStore.get(cookie.company)) ? '' : $cookieStore.get(cookie.company);
            };

            this.setSelCompany = function(value) {
                $cookieStore.put(cookie.company, value);
            };

            this.getSelChannel = function () {
                return _.isNaN($cookieStore.get(cookie.channel)) ? '' : $cookieStore.get(cookie.channel);
            };

            this.setSelChannel = function(value) {
                $cookieStore.put(cookie.channel, value);
            };

            this.getLoginTime = function () {
                return _.isNaN($cookieStore.get(cookie.loginTime)) ? _.now() : $cookieStore.get(cookie.loginTime);
            };

            this.setLoginTime = function (value) {
                $cookieStore.put(cookie.loginTime, value);
            };
        }]);
});