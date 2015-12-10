/**
 * @Description:
 * 用于分页
 * @User:    Edward
 * @Version: 0.2.0, 2015-12-08
 */

angular.module('voyageone.angular.directives.vpagination', [])
    .directive('vpagination', function (vpagination, $templateCache) {

        var templateKey = "voyageone.angular.directives.pagination.tpl.html";

        if (!$templateCache.get(templateKey)) {
            $templateCache.put(templateKey,
                '<div class="col-sm-2">\n' +
                '    <div class="page-main form-inline">{{\'TXT_COM_SHOWING_NO\' | translate}}&nbsp;<input type="text" ng-model="curr.pageNo"/>&nbsp;/&nbsp;{{totalPages}}&nbsp;{{\'TXT_COM_PAGE\' | translate}}&nbsp;' +
                '        <button class="btn btn-xs btn-default" type="button" ng-click="goPage(curr.pageNo)" translate="BTN_GO"></button>\n' +
                '    </div>\n' +
                '</div>\n' +
                '<div class="col-sm-7 text-center">\n' +
                '    <small class="text-muted inline m-t-sm m-b-sm">{{\'TXT_COM_SHOWING\' | translate}}&nbsp;{{curr.start}}-{{curr.end}}&nbsp;{{\'TXT_COM_OF\' | translate}}&nbsp;{{totalItems}}&nbsp{{\'TXT_COM_ITEMS\' | translate}}</small>\n' +
                '</div>\n' +
                '<div class="col-sm-3 text-right text-center-xs"><div>' +
                '    <ul class="pagination-sm m-t-none m-b pagination ng-isolate-scope ng-valid ng-dirty ng-valid-parse">\n' +
                '        <li ng-class="{disabled: curr.isFirst ||ngDisabled}" class="pagination-first"><a href ng-click="goPage(1)" ng-disabled="curr.isFirst">&laquo;</a></li>\n' +
                '        <li ng-class="{disabled: curr.isFirst ||ngDisabled}" class="pagination-prev"><a href ng-click="goPage(curr.pageNo - 1)" ng-disabled="curr.isFirst">&lsaquo;</a></li>\n' +
                '        <li ng-if="curr.isShowStart" class="disabled" disabled><a href>...</a></li>\n' +
                '        <li ng-repeat="page in curr.pages track by $index" ng-class="{active: isCurr(page)}" class="pagination-page"><a href ng-click="goPage(page)">{{page}}</a></li>\n' +
                '        <li ng-if="curr.isShowEnd" class="disabled" disabled><a href>...</a></li>\n' +
                '        <li ng-class="{disabled: curr.isLast ||ngDisabled}" class="pagination-next"><a href ng-click="goPage(curr.pageNo + 1)" ng-disabled="curr.isLast">&rsaquo;</a></li>\n' +
                '        <li ng-class="{disabled: curr.isLast ||ngDisabled}" class="pagination-last"><a href ng-click="goPage(totalPages)" ng-disabled="curr.isLast">&raquo;</a></li>\n' +
                '    </ul>\n' +
                '</div>');
        }

        //var defScope = {
        //    justOne: false,
        //    buttons: []
        //};

        var defConfig = {curr: 1, total: 0, size: 20, showPageNo:5};

        return {
            restrict: "AE",
            templateUrl: templateKey,
            replace: false,
            scope: {
                $$configNameForA: "@vpagination",
                $$configNameForE: "@config"
            },
            link: function (scope) {

                // 获取用户的config配置
                var userConfigName = scope.$$configNameForA || scope.$$configNameForE;
                var userConfig = scope.$parent.$eval(userConfigName);

                // 先将默认配置创建到 scope 中
                //angular.extend(scope, defScope);

                // 将用户配置覆盖到默认配置后，在重新覆盖到用户配置上，用于补全配置属性
                var userWithDefConfig = angular.extend({}, defConfig, userConfig);
                scope.config = angular.extend(userConfig, userWithDefConfig);

                // 监视配置变动
                scope.$parent.$watch(userConfigName, function () {
                    refresh();
                }, true);

                var p = new vpagination(scope.config);

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

                }
            }
        };
    });