/**
 * @description
 *
 * 自动创建基于地址定义的数据访问 service.
 * 传入的定义必须是 {object}, 并且至少有 root 属性
 *
 * @User: Jonas
 * @Date: 2015-12-10 19:32:37
 * @Version: 2.0.0
 */

angular.module("voyageone.angular.vresources", []).provider("$vresources", function ($provide) {

    function actionHashcode(md5, root, action, args) {
        var argsJson = angular.toJson(args);
        var md5Arg = root + action + argsJson;
        return '_' + md5.createHash(md5Arg);
    }

    /**
     * 闭包声明一个数据访问的 Service
     * @param {string} name Service 的名称
     * @param {object} actions 方法和地址定义
     */
    function closureDataService(name, actions) {

        var _ServiceClass, root = actions.root;

        if (!actions) {
            return;
        }
        if (typeof actions !== "object") {
            console.log("Failed to new DataResource: [" + actions + "] is not a object");
            return;
        }
        if (!root) {
            console.log("Failed to new DataResource: no root prop" + angular.toJson(actions));
            return;
        }

        _ServiceClass = function (ajax, $localStorage, md5, $q) {
            this._a = ajax;
            this._s = $localStorage;
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
                    return res;
                };

            if (!_cacheFlag || _cacheFlag > 2)
                _cacheFlag = 0;

            _ServiceClass.prototype[key] = _cacheFlag === 0 ? function (args) {
                return this._a.post(_root + _url, args).then(_resolve, _reject);
            } : function (args) {
                var deferred, result;
                var storage = this._s,
                    hash = actionHashcode(this._5, root, key, args),
                    promise = this._c[hash];
                if (promise)
                    return promise;
                deferred = this._q.defer();
                promise = deferred.promise;
                this._c[hash] = promise;

                if (_cacheFlag === 2) {
                    result = storage[hash];
                }

                if (result !== null && result !== undefined)
                    deferred.resolve(result);
                else
                    this._a.post(_root + _url, args).then(function (res) {
                        result = _resolve(res);
                        if (_cacheFlag === 2)
                            storage[hash] = result;
                        deferred.resolve(result);
                    }, function (res) {
                        result = _reject(res);
                        deferred.reject(result);
                    });

                return promise;
            };
        });

        $provide.service(name, ['ajaxService', '$localStorage', 'md5', '$q', _ServiceClass]);
    }

    this.$get = function () {
        return {
            register: function (name, actions) {
                if (!actions) return;
                if (typeof actions !== "object") return;
                // 如果有 root 这个属性,就创建 service
                if (actions.root) {
                    closureDataService(name, actions);
                    return;
                }
                // 否则继续访问子属性
                for (var childName in actions) {
                    // 额外的检查
                    if (actions.hasOwnProperty(childName)) {
                        this.register(childName, actions[childName]);
                    }
                }
            }
        };
    };
}).run(function ($vresources, $actions) {
    $vresources.register(null, $actions);
});