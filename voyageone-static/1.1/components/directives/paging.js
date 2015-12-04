/**
 * @User: Jonas
 * @Date: 2015-04-24 10:24:10
 * @Version: 0.0.3
 */

define(["components/app", "underscore"], function (app) {

    return app

        .factory("paging", function () {

            /**
             * 创建一个分页服务
             * @param {{ curr: number, size: number, total: number, fetch: function }} config 配置
             */
            return function (config) {

                var _pages, _lastTotal = 0;

                /**
                 * 跳转到第一页
                 */
                this.first = function () {
                    load(1);
                };

                /**
                 * 跳转到最后一页
                 */
                this.last = function () {
                    load(getPages());
                };

                /**
                 * 跳转到前一页
                 */
                this.prev = function () {
                    if (isFirst()) return;
                    load(config.curr - 1);
                };

                /**
                 * 跳转到后一页
                 */
                this.next = function () {
                    if (isLast()) return;
                    load(config.curr + 1);
                };

                this.load = load;

                this.getPages = getPages;

                this.isLast = isLast;

                this.isFirst = isFirst;

                this.isCurr = isCurr;

                this.curr = curr;

                /**
                 * 跳转到指定页
                 * @param {number} page 页号
                 */
                function load(page) {
                    page = page || config.curr;

                    if (page < 1 || page > getPages() || page == config.curr) return;

                    config.curr = page;

                    config.fetch(page, config.size);
                }

                /**
                 * 获取当前总页数
                 * @returns {number}
                 */
                function getPages() {
                    if (_lastTotal != config.total) {
                        _pages = parseInt(config.total / config.size) + ((config.total % config.size) > 0 ? 1 : 0);
                        _lastTotal = config.total;
                    }
                    return _pages;
                }

                /**
                 * 是否是最后一页
                 * @returns {boolean}
                 */
                function isLast() {
                    return config.curr == getPages();
                }

                /**
                 * 是否是第一页
                 * @returns {boolean}
                 */
                function isFirst() {
                    return config.curr == 1;
                }

                /**
                 * 是否是当前页
                 * @param i 页码
                 * @returns {boolean}
                 */
                function isCurr(i) {
                    return config.curr == i;
                }

                /**
                 * 获取当前页
                 * @returns {number}
                 */
                function curr() {
                    return config.curr;
                }

            };
        })

        .filter("dbIndex", [
            function () {
                return function (index, option) {
                    return index + 1 + (option.curr - 1) * option.size;
                };
            }
        ])

        .directive("paging", ["paging", "$templateCache", function (paging, $templateCache) {

            var templateKey = "voyage.toolkit.paging.tpl.html";

            if (!$templateCache.get(templateKey)) {
                $templateCache.put(templateKey, '<ul class="pagination"><li ng-if="!justOne" ng-click="toFirst()"><a href="javascript:;">&laquo;</a></li><li ng-if="b.show" ng-click="toPage(b.num)" class="{{b.active}}" ng-repeat="b in buttons"><a href="javascript:;">{{b.num}}</a></li><li ng-if="!justOne" ng-click="toLast()"><a href="javascript:;">&raquo;</a></li></ul>');
            }

            var defScope = {
                justOne: false,
                buttons: null
            };

            var defConfig = {buttons: 5};

            return {
                restrict: "EA",
                templateUrl: templateKey,
                replace: false,
                scope: {
                    $$configNameForA: "@paging",
                    $$configNameForE: "@config"
                },
                link: function (scope) {
                    var userConfigName = scope.$$configNameForA || scope.$$configNameForE;

                    var userConfig = scope.$parent.$eval(userConfigName);

                    // 先将默认配置创建到 scope 中
                    angular.extend(scope, defScope);

                    // 将用户配置覆盖到默认配置后，在重新覆盖到用户配置上，用于补全配置属性
                    var userWithDefConfig = angular.extend({}, defConfig, userConfig);
                    scope.config = angular.extend(userConfig, userWithDefConfig);

                    // 监视配置变动
                    scope.$parent.$watch(userConfigName, function () {
                        refresh();
                    }, true);

                    if (scope.config.buttons % 2 != 1) {
                        // 说明不是奇数，选中的按钮无法剧终
                        throw "config \"buttons\" must be an odd number.";
                    }

                    var p = new paging(scope.config),
                        _btnOffset;

                    scope.toFirst = function () {
                        p.first();
                    };

                    scope.toLast = function () {
                        p.last();
                    };

                    scope.toPage = function (num) {
                        p.load(num)
                    };

                    function getBtnOffset() {
                        if (!_btnOffset) _btnOffset = parseInt(scope.config.buttons / 2);
                        return _btnOffset;
                    }

                    function hasPrev() {
                        return p.curr() - getBtnOffset() > 1;
                    }

                    function hasNext() {
                        return p.curr() + getBtnOffset() < p.getPages();
                    }

                    function render() {
                        if (scope.buttons) return;
                        else scope.buttons = [];

                        // 按照指定数量创建按钮
                        for (var i = 1; i <= scope.config.buttons; i++) {
                            scope.buttons.push({num: 1, active: "", show: false});
                        }
                    }

                    function refresh() {
                        render();
                        scope.justOne = (p.getPages() < 2);

                        var pages = p.getPages(),
                            i = hasPrev() ? ( hasNext() ? p.curr() - 2 : pages - 4 ) : 1;

                        angular.forEach(scope.buttons, function (btn) {
                            btn.num = i;
                            btn.show = (i >= 1 && i <= pages);
                            btn.active = p.isCurr(i) ? "active" : "";

                            i++;
                        });
                    }
                }
            };

        }]);
});
