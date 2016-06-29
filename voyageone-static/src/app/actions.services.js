define(function (require) {

    var _ = require('underscore');

    function actionHashcode(md5, root, action, args) {
        var argsJson = angular.toJson(args);
        var md5Arg = root + action + argsJson;
        return '_' + md5.createHash(md5Arg);
    }

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

        _class = function (ajaxService, $localStorage, md5, $q) {
            this.ajaxService = ajaxService;
            this.$storage = $localStorage;
            this.md5 = md5;
            this.$q = $q;
            this.cached = {};
        };

        _.each(actions, function (content, key) {

            var _url, _root, _resolve, _reject, _cacheable;

            if (_.isString(content))
                _url = content;
            else if (_.isObject(content)) {
                _url = content.url;
                _resolve = content.then;
                _root = content.root;
                _cacheable = !!content.localstorage;
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
                _resolve = function (res) {
                    return res.data;
                };

            _class.prototype[key] = !_cacheable ? function (args) {
                return this.ajaxService.post(_root + _url, args).then(_resolve, _reject);
            } : function (args) {
                var deferred, result;
                var storage = this.$storage,
                    hash = actionHashcode(this.md5, root, key, args),
                    promise = this.cached[hash];
                if (promise)
                    return promise;
                deferred = this.$q.defer();
                promise = deferred.promise;
                this.cached[hash] = promise;

                result = storage[hash];
                if (result !== null || result !== undefined)
                    deferred.resolve(result);
                else
                    this.ajaxService.post(_root + _url, args).then(function (res) {
                        result = _resolve(res);
                        storage[hash] = result;
                        deferred.resolve(result);
                    }, function (res) {
                        result = _reject(res);
                        deferred.reject(result);
                    });

                return promise;
            };
        });

        return ['ajaxService', '$localStorage', 'md5', _class];
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
            getVendorMenuHeaderInfo: {
                url: 'getVendorMenuHeaderInfo',
                localstorage: true
            },
            setChannel: 'setChannel'
        })
    }
});