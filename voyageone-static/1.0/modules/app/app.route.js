/**
 * @Name:    app.route.js
 * @Date:    2015/4/30
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ("components/app");

    // define the route info.
    mainApp.constant ("commonRoute", {
        "common_default_index": {
            "hash": "/",
            "page": "modules/app/common/index.tpl.html",
            "controller": "modules/app/common/index.ctl"
        },
        'common_error_index': {
            'hash': '/app/common/error',
            'page': 'modules/app/common/error.tpl.html',
            'controller': 'modules/app/common/error.ctl'
        }
    });

    mainApp.config (["$routeProvider", "commonRoute",
        function ($routeProvider, route) {

            var _ = require ("underscore");

            return _.each (route, function (value) {

                var angularAMD = require ("angularAMD");
                var commonUtil = require ('components/util/commonUtil');

                return $routeProvider.when (value.hash, angularAMD.route ({
                    templateUrl: value.page,
                    resolve: {
                        load: ["$q", "$rootScope", function ($q, $rootScope) {
                            return commonUtil.loadController ($q, $rootScope, value.controller);
                        }]
                    }
                }));
            });
        }]);
});