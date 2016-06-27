define(function (require) {

    require('components/menu.component');
    require('controllers/topbar.controller');
    require('./popups.factory');

    var angularAMD = require('angularAMD');
    var actions = require('./actions');
    var _ = require('underscore');

    var app = angular.module('vms', [
        'ngSanitize',
        'ngRoute',
        'ngAnimate',
        'ngCookies',
        'pascalprecht.translate',
        'blockUI',
        'ui.bootstrap',
        'vo.ng',
        'localytics.directives',
        'vms.menu',
        'vms.topbar',
        'vms.popups'
    ]);

    var amdApp = angularAMD.bootstrap(app);

    // 扩展一个 service 创建的帮助方法
    amdApp.createService = function (actionNames, proto) {

        var _actions = actions, _class;

        _.all(actionNames, function (name) {
            if (!_actions) return false;
            return _actions = _actions[name];
        });

        if (!_actions)
            return null;

        _class = function (ajaxService) {
            this.ajaxService = ajaxService;
        };

        _.each(_actions, function (content, key) {
            var customCallback = proto ? proto[key] : null;
            if (key === 'root')
                return;
            _class.prototype[key] = function (args) {
                return this.ajaxService.post(_actions['root'] + content, args).then(customCallback || function (res) {
                        return res.data;
                    });
            };
        });

        return ['ajaxService', _class];
    };

    return amdApp;
});