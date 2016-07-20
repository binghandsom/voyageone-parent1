angular.module("vo.directives").directive("vpagination", function ($templateCache, $compile, vpagination) {
    var templateKey = "voyageone.angular.directives.pagination.tpl.html";
    var templateKeyNoData = "voyageone.angular.directives.paginationNoData.tpl.html";
    // 有数据分页样式
    if (!$templateCache.get(templateKey)) {
        // 这个 html 是经过压缩的 html , 如果需要修改分页的 html 结构, 请去 vpagination.directive.html 修改, 修改后压缩并粘贴覆盖这里的代码
        $templateCache.put(templateKey, '<div class="col-sm-12 form-inline"style="line-height: 34px;"><div class="col-sm-3"><span class="col-sm-2"translate="TXT_SHOWING_NO"></span><input class="text-center form-control"type="text"ng-model="curr.pageNo"style="padding: 1px 5px; font-size: 12px; line-height: 1.5; border-radius: 3px; max-height: 22px; max-width: 30px;"><span>&nbsp;</span><span>/</span><span>&nbsp;</span><span ng-bind="totalPages"></span><span>&nbsp;</span><span translate="TXT_PAGE"></span><span>&nbsp;</span><button class="btn btn-xs btn-default"type="button"ng-click="goPage(curr.pageNo)"translate="BTN_GO"></button></div><form class="col-sm-4 text-center form-inline"><span translate="TXT_PAGER_SIZE"></span><span>&nbsp;</span><select class="form-control"ng-change="changePerPage(perpages.selectedOption)"ng-options="option for option in perpages.availableOptions"ng-model="perpages.selectedOption"style="padding: 1px 5px; font-size: 12px; line-height: 1.5; border-radius: 3px; max-height: 22px; max-width: 60px;"></select><span>&nbsp;</span><span translate="TXT_SHOWING"></span><span>&nbsp;</span><span ng-bind="curr.start"></span><span>&nbsp;</span><span>-</span><span>&nbsp;</span><span ng-bind="curr.end"></span><span>&nbsp;</span><span translate="TXT_OF"></span><span>&nbsp;</span><span ng-bind="totalItems"></span><span>&nbsp;</span><span translate="TXT_ITEMS"></span></form><div class="col-sm-5"><ul class="pagination pull-right"style="margin: 0"><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(1)"ng-disabled="curr.isFirst">&laquo;</a></li><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(curr.pageNo - 1)"ng-disabled="curr.isFirst">&lsaquo;</a></li><li ng-if="curr.isShowStart"><a href>...</a></li><li ng-repeat="page in curr.pages track by $index"ng-class="{active: isCurr(page)}"><a href ng-click="goPage(page)">{{page}}</a></li><li ng-if="curr.isShowEnd"><a href>...</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(curr.pageNo + 1)"ng-disabled="curr.isLast">&rsaquo;</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(totalPages)"ng-disabled="curr.isLast">&raquo;</a></li></ul></div></div>');
    }
    // 无数据分页样式
    if (!$templateCache.get(templateKeyNoData)) {
        // 这里同上
        $templateCache.put(templateKeyNoData, '<div class="text-center">&nbsp;<span translate="TXT_SHOWING"></span>&nbsp;<span>0&nbsp;-&nbsp;0</span>&nbsp;<span translate="TXT_OF"></span>&nbsp;<span>0</span>&nbsp;<span translate="TXT_ITEMS"></span></div>');
    }
    var defConfig = {
        curr: 1,
        total: 0,
        size: 10,
        showPageNo: 5,
        fetch: null
    };
    return {
        restrict: "AE",
        replace: false,
        scope: {
            $$configNameForA: "@vpagination",
            $$configNameForE: "@config"
        },
        link: function (scope, element) {
            // 获取用户的config配置
            var userConfigName = scope.$$configNameForA || scope.$$configNameForE;
            var userConfig = scope.$parent.$eval(userConfigName);
            // 将用户配置覆盖到默认配置后，在重新覆盖到用户配置上，用于补全配置属性
            var userWithDefConfig = angular.extend({}, defConfig, userConfig);
            scope.config = angular.extend(userConfig, userWithDefConfig);
            scope.config.setPageIndex = function (pageIndex) {
                if (scope.config.curr == pageIndex) {
                    scope.config.fetch(scope.config.curr, scope.config.size);
                }
                else {
                    scope.goPage(pageIndex);
                }
            };
            var p = new vpagination(scope.config);
            // 监视配置变动
            scope.$parent.$watch(userConfigName, function () {
                refresh();
            }, true);
            /**
             * 跳转到指定页
             * @param num
             */
            scope.goPage = function (num) {
                p.goPage(isNaN(Number(num)) ? 1 : Number(num));
            };
            /**
             * 判断是否是当前页
             * @param num
             * @returns {*|boolean}
             */
            scope.isCurr = function (num) {
                return p.isCurr(num);
            };
            function refresh() {
                // 获取总页数
                scope.totalPages = p.getPageCount();
                // 获取总items数
                scope.totalItems = p.getTotal();
                // 获取当前页的信息
                scope.curr = p.getCurr();
                // 获取每页数量
                scope.config.size = p.getSize();

                scope.perpages = {
                    availableOptions: [10, 20, 50, 100],
                    selectedOption: scope.config.size
                };
                // 根据总数量显示不同的分页样式
                var tempHtml;
                if (p.getTotal() == 0) {
                    tempHtml = $compile($templateCache.get(templateKeyNoData))(scope);
                } else {
                    tempHtml = $compile($templateCache.get(templateKey))(scope);
                }
                element.html(tempHtml);
            }

            scope.changePerPage = function (perpage) {
                scope.config.size = parseInt(perpage);
                //当改变页数时，切换到第一页
                scope.config.curr = 1;
                p.goPage(parseInt(scope.config.curr));
            }
        }
    };
}).factory("vpagination", function () {
    /**
     * 创建一个分页服务
     * @param {{ curr: number, size: number, total: number, fetch: function }} config 配置
     */
    return function (config) {
        var _pages, _lastTotal = 0, _showPages = [], defaultPage = config.size;
        /**默认page为20，当改变时触发分页，add by pwj*/
        /**
         * 返回总件数
         * @returns {*}
         */
        this.getTotal = function () {
            return config.total;
        };


        /**
         * 获取每页数量
         * @returns {*}
         */
        this.getSize = function () {
            return config.size;
        };

        /**
         * 返回当前页的开始和结束号
         * @returns {{start: number, end: number}}
         */
        this.getCurr = function () {
            return {
                pageNo: curr(),
                start: getCurrStartItems(),
                end: getCurrEndItems(),
                isFirst: isFirst(),
                isLast: isLast(),
                pages: createShowPages(),
                isShowStart: isShowStart(),
                isShowEnd: isShowEnd()
            };
        };
        // 跳转到指定页
        this.goPage = load;
        // 返回总页数
        this.getPageCount = getPages;
        // 是否是当前页
        this.isCurr = isCurr;

        /**
         * 跳转到指定页
         * @param {number} page 页号
         */
        function load(page) {
            page = page || config.curr;
            if (page < 1 || page > getPages() || isCurr(page)) return;
            config.curr = page;
            //保留上次每页条数
            defaultPage = config.size;
            config.fetch(config.curr, config.size);
        }

        /**
         * 初始化page列表
         * @returns {Array}
         */
        function createShowPages() {
            var minPage, maxPage, _showPages = [];
            if (config.curr < config.showPageNo) {
                minPage = 1;
                if (_pages <= config.showPageNo) maxPage = _pages; else maxPage = config.showPageNo;
            } else if (config.curr + 2 > _pages) {
                minPage = _pages + 1 - config.showPageNo;
                maxPage = _pages;
            } else {
                minPage = config.curr + 3 - config.showPageNo;
                maxPage = config.curr + 2;
            }
            // 按照指定数量创建按钮
            for (var i = minPage; i <= maxPage; i++) {
                //scope.pages.push({num: 1, active: "", show: false});
                _showPages.push(i);
            }
            return _showPages;
        }

        /**
         * 获取当前总页数
         * @returns {number}
         */
        function getPages() {
            if (_lastTotal != config.total || config.size !== defaultPage) {
                _pages = parseInt(config.total / config.size) + (config.total % config.size > 0 ? 1 : 0);
                _lastTotal = config.total;
            }
            return _pages;
        }

        /**
         * 返回当前页的起始号
         * @returns {number}
         */
        function getCurrStartItems() {
            return (config.curr - 1) * config.size + 1;
        }

        /**
         * 返回当前页的结束号
         * @returns {number}
         */
        function getCurrEndItems() {
            var currEndItems = config.curr * config.size;
            return currEndItems <= config.total ? currEndItems : config.total;
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
         * @param page 页码
         * @returns {boolean}
         */
        function isCurr(page) {
            return config.curr == page && config.size === defaultPage;
        }

        function curr() {
            return config.curr;
        }

        /**
         * 是否显示开始...项目
         * @returns {boolean}
         */
        function isShowStart() {
            _showPages = createShowPages();
            return _showPages[0] > 1;
        }

        /**
         * 是否显示结束...项目
         * @returns {boolean}
         */
        function isShowEnd() {
            _showPages = createShowPages();
            return _showPages[_showPages.length - 1] < _pages;
        }
    };
});
