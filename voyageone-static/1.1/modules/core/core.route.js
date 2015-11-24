/**
 * @Name:    route.js
 * @Date:    2015/2/2
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');

    mainApp.constant ('coreRoute', {
        'core_account_login': {
            'hash': '/core/account/login',
            'page': 'modules/core/account/login.tpl.html',
            'controller': 'modules/core/account/login.ctl'
        },
        'core_account_company': {
            'hash': '/core/account/company',
            'page': 'modules/core/account/systemSelect.tpl.html',
            'controller': 'modules/core/account/systemSelect.ctl'
        }
        //,
        //'core_menu_home': {
        //    'hash': '/core/menu/home',
        //    'page': 'modules/core/menu/home.tpl.html',
        //    'controller': 'modules/core/menu/home.ctl'
        //},
        //'core_setting_user': {
        //    'hash': '/core/setting/user',
        //    'page': 'modules/core/setting/user.tpl.html',
        //    'controller': 'modules/core/setting/user.ctl'
        //},
        //'core_setting_userdetail': {
        //    'hash': '/core/setting/userdetail',
        //    'page': 'modules/core/setting/userDetail.tpl.html',
        //    'controller': 'modules/core/setting/userDetail.ctl'
        //},
        //'core_setting_role': {
        //    'hash': '/core/setting/role',
        //    'page': 'modules/core/setting/role.tpl.html',
        //    'controller': 'modules/core/setting/role.ctl'
        //},
        //'core_setting_application': {
        //    'hash': '/core/setting/app',
        //    'page': 'modules/core/setting/application.tpl.html',
        //    'controller': 'modules/core/setting/application.ctl'
        //},
        //'core_manager_batchJob': {
        //    'hash': '/core/manager/batchJob',
        //    'page': 'modules/core/manager/batchJob.tpl.html',
        //    'controller': 'modules/core/manager/batchJob.ctl.js'
        //}
    });

    mainApp.config (["$routeProvider", "coreRoute",
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