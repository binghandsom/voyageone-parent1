define(function (require) {

    var _ = require('underscore');

    function CommonDataService(root, actions) {
        this.root = root;
        this.actions = actions;
    }

    CommonDataService.prototype.getDeclare = function () {

        var _class;

        var self = this,
            root = self.root || "",
            actions = self.actions;

        if (!actions)
            return null;

        _class = function (ajaxService) {
            this.ajaxService = ajaxService;
        };

        _.each(actions, function (content, key) {

            var _url, _root, _resolve, _reject;

            if (_.isString(content))
                _url = content;
            else if (_.isObject(content)) {
                _url = content.url;
                _resolve = content.then;
                _root = content.root;
            }

            if (!_url) {
                console.error('URL is undefined', content);
                return;
            }

            if (_root === false)
                _root = "";
            else if (_root === null || _root === undefined || _root === true)
                _root = root;

            if (_.isArray(_resolve)) {
                _reject = _resolve[1];
                _resolve = _resolve[0]
            }

            if (!_.isFunction(_resolve))
                _resolve = null;

            _class.prototype[key] = function (args) {
                return this.ajaxService.post(_root + _url, args).then(
                    _resolve || function (res) {
                        return res.data;
                    },
                    _reject || null);
            };
        });

        return ['ajaxService', _class];
    };

    window.CommonDataService = CommonDataService;

    return {
        testService: new CommonDataService('/test/', {
            action1: 'getSomeData',
            action2: {
                url: 'getSomeData2',
                then: function (res) {
                    return res.message;
                }
            }
        }),
        userService: new CommonDataService('/core/access/user/', {
            logout: 'logout'
        }),
        menuService: new CommonDataService('/core/home/menu/', {
            getVendorMenuHeaderInfo: 'getVendorMenuHeaderInfo'
        })
    }
});