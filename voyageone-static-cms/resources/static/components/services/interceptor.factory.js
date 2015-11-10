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
                if (config.method !== 'POST') return config;

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
                $location.path(commonRoute.common_error_index.hash);
            }
        };
    }
});
