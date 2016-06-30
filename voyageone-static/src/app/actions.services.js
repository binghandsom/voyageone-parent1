define(function (require) {

    var _ = require('underscore');

    function CacheFlag(id) {
        this.id = id;
    }

    CacheFlag.NONE = new CacheFlag(0);
    CacheFlag.ONCE = new CacheFlag(1);
    CacheFlag.SESSION = new CacheFlag(2);
    CacheFlag.LOCAL = new CacheFlag(3);

    function actionHashcode(md5, root, action, args) {
        var argsJson = angular.toJson(args);
        var md5Arg = root + action + argsJson;
        return '_' + md5.createHash(md5Arg);
    }

    function getActionUrl(root, action) {
        return root + (root.lastIndexOf("/") === root.length - 1 ? "" : "/") + action;
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

        _class = function (ajax, $sessionStorage, $localStorage, md5, $q) {
            this._a = ajax;
            this._sc = $sessionStorage;
            this._lc = $localStorage;
            this._5 = md5;
            this._q = $q;
            this._c = {};
        };

        _.each(actions, function (content, key) {

            var _url, _root, _resolve, _reject, _cacheFlag;

            if (_.isString(content))
                _url = content;
            else if (_.isObject(content)) {
                _url = content.url;
                _resolve = content.then;
                _root = content.root;
                _cacheFlag = content.cache;
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

            if (!(_cacheFlag instanceof CacheFlag))
                _cacheFlag = CacheFlag.NONE;

            _url = getActionUrl(_root, _url);

            _class.prototype[key] = _cacheFlag === CacheFlag.NONE ? function (args) {
                return this._a.post(_url, args).then(_resolve, _reject);
            } : function (args) {
                var deferred, result;
                var session = this._sc,
                    local = this._lc,
                    hash = actionHashcode(this._5, root, key, args),
                    promise = this._c[hash];
                if (promise)
                    return promise;
                deferred = this._q.defer();
                promise = deferred.promise;
                this._c[hash] = promise;

                switch(_cacheFlag) {
                    case CacheFlag.SESSION:
                        result = session[hash];
                        break;
                    case CacheFlag.LOCAL:
                        result = local[hash];
                        break;
                }

                if (result !== null && result !== undefined)
                    deferred.resolve(result);
                else
                    this._a.post(_url, args).then(function (res) {
                        result = _resolve(res);
                        switch (_cacheFlag) {
                            case CacheFlag.SESSION:
                                session[hash] = result;
                                break;
                            case CacheFlag.LOCAL:
                                local[hash] = result;
                                break;
                        }
                        deferred.resolve(result);
                    }, function (res) {
                        result = _reject(res);
                        deferred.reject(result);
                    });

                return promise;
            };
        });

        return ['ajaxService', '$sessionStorage', '$localStorage', 'md5', '$q', _class];
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
                cache: CacheFlag.ONCE
            },
            setChannel: 'setChannel'
        })
    }
});
