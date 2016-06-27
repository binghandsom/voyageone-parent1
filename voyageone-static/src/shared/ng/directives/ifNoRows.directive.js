/**
 * @Description:
 * table中无数据范围的数据
 * @User: linanbin
 * @Version: 2.0.0, 15/12/11
 */
angular.module("vo.directives").directive("ifNoRows", function ($templateCache, $compile) {
    var tempNoDataKey = "vo.directives.ifNoRows.tpl.html";
    // 没有数据显示警告
    if (!$templateCache.get(tempNoDataKey)) {
        $templateCache.put(tempNoDataKey, '<div class="text-center text-hs" id="noData">\n' + '    <h4 class="text-vo"><i class="icon fa fa-warning"></i>&nbsp;{{\'TXT_ALERT\' | translate}}</h4>\n' + "{{'TXT_MSG_NO_DATE' | translate}}" + "</dv>");
    }
    return {
        restrict: "A",
        replace: false,
        scope: {
            $$data: "@ifNoRows"
        },
        link: function (scope, element) {
            scope.$parent.$watch(scope.$$data, function () {
                // 如果数据不存在则显示警告信息
                if (scope.$parent.$eval(scope.$$data) == 0) {
                    element.find("#noData").remove();
                    element.append($compile($templateCache.get(tempNoDataKey))(scope));
                } else {
                    element.find("#noData").remove();
                }
            });
        }
    };
});
