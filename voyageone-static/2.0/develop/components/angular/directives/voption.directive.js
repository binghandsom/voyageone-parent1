(function() {
    /**
     * @Description:
     * 用于动态显示options
     * @User:    Edward
     * @Version: 0.2.0, 2015-12-22
     */
    angular.module("voyageone.angular.directives.voption", []).directive("voption", function($templateCache, $compile) {
        var templateKey_select = "voyageone.angular.directives.optionSelect.tpl.html";
        // 显示成select样式
        if (!$templateCache.get(templateKey_select)) {
            $templateCache.put(templateKey_select, '<select class="form-control" ng-model="$$data.value.value" ng-options="option.value as option.displayName for option in $$data.options"> <option value="">{{\'TXT_SELECT_NO_VALUE\' | translate}}</option></select>');
        }
        return {
            restrict: "E",
            replace: true,
            scope: {
                $$data: "=data"
            },
            link: function(scope, element) {
                // 定义不同的展示种类
                var typeList = {
                    SINGLE_CHECK: "SINGLECHECK"
                };
                // 监视配置变动
                scope.$watch("$$data", function() {
                    refresh();
                }, true);
                /**
                 * 根据type选择展示不同的控件
                 */
                function refresh() {
                    var tempHtml;
                    switch (scope.$$data.type) {
                      case typeList.SINGLE_CHECK:
                        tempHtml = $compile($templateCache.get(templateKey_select))(scope);
                        break;
                    }
                    element.html(tempHtml);
                }
            }
        };
    });
})();