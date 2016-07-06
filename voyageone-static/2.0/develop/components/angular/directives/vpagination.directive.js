/**
 * @Description:
 * 用于分页
 * @User:    Edward
 * @Version: 0.2.0, 2015-12-08
 */
angular.module("voyageone.angular.directives").directive("vpagination", function ($templateCache, $compile, vpagination) {
    var templateKey = "voyageone.angular.directives.pagination.tpl.html";
    var templateKeyNoData = "voyageone.angular.directives.paginationNoData.tpl.html";
    // 有数据分页样式
    if (!$templateCache.get(templateKey)) {
        // 这个 html 是经过压缩的 html , 如果需要修改分页的 html 结构, 请去 vpagination.directive.html 修改, 修改后压缩并粘贴覆盖这里的代码
        $templateCache.put(templateKey, '<div class="row"><div class="col-sm-3"><span translate="TXT_SHOWING_NO"></span><span>&nbsp;</span><input class="text-center" type="text" ng-model="curr.pageNo"><span>&nbsp;</span><span>/</span><span>&nbsp;</span><span ng-bind="totalPages"></span><span>&nbsp;</span><span translate="TXT_PAGE"></span><span>&nbsp;</span><button class="btn btn-xs btn-default" type="button" ng-click="goPage(curr.pageNo)" translate="BTN_GO"></button></div><div class="col-sm-4 text-center"><span translate="TXT_PAGER_SIZE"></span><span>&nbsp;</span><select ng-change="changePerPage(perpages.selectedOption)" ng-options="option for option in perpages.availableOptions" ng-model="perpages.selectedOption"></select><span>&nbsp;</span><span translate="TXT_SHOWING"></span><span>&nbsp;</span><span ng-bind="curr.start"></span><span>&nbsp;</span><span>-</span><span>&nbsp;</span><span ng-bind="curr.end"></span><span>&nbsp;</span><span translate="TXT_OF"></span><span>&nbsp;</span><span ng-bind="totalItems"></span><span>&nbsp;</span><span translate="TXT_ITEMS"></span></div><div class="col-sm-5 text-right"><ul class="pagination"><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(1)" ng-disabled="curr.isFirst">&laquo;</a></li><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(curr.pageNo - 1)" ng-disabled="curr.isFirst">&lsaquo;</a></li><li ng-if="curr.isShowStart">...</li><li ng-repeat="page in curr.pages track by $index" ng-class="{active: isCurr(page)}"><a href ng-click="goPage(page)">{{page}}</a></li><li ng-if="curr.isShowEnd">...</li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(curr.pageNo + 1)" ng-disabled="curr.isLast">&rsaquo;</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(totalPages)" ng-disabled="curr.isLast">&raquo;</a></li></ul></div></div>');
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
});
