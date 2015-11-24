/**
 * @Name:    interceptorFactory.js
 * @Date:    2015/2/15
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');
    require ('components/services/cookie.service');
    require ('modules/app/services/user.service');

    // define a factory on mainApp for save the permissions of login user.
    mainApp.factory ('interceptorFactory', [
        '$location',
        'userService',
        'cookieService',
        'commonRoute',
        interceptorFactory]);

    function interceptorFactory(
        $location,
        userService,
        cookieService,
        commonRoute
    ) {

        var _ = require ('underscore');
        var commonUtil = require ('components/util/commonUtil');

        return {
            /**
             * set the token/sellanguage/selCompany info to header.
             * @param config
             * @returns {*}
             */
            request: function (config) {

                // 非 POST 时，无需处理
                if (config.method !== 'POST') return config;

                // 在发送请求为 POST 的时候（即，该请求为 Tomcat 处理的请求）
                // 设置 Java 端需要的，或需要验证的信息

                config.headers['Voyageone-User-Token'] = cookieService.getToken ();
                config.headers['Voyageone-User-Lang'] = cookieService.getSelLanguage ();
                config.headers['Voyageone-User-Company'] = cookieService.getSelCompany ();

                return config;
            },
            response: function (res) {

                var token = res.data.token;

                if (!_.isEmpty(token))
                    cookieService.setToken(token);

                return res;
            },
            requestError: function (config) {
                return config;
            },
            responseError: function () {

                sessionStorage.clear();

                userService.init ();

                cookieService.init ();

                $location.path(commonRoute.common_error_index.hash);
            }
        };
    }
});