angular.module("vo.directives").directive("ifNoRows", function ($templateCache, $compile) {
    var tempNoDataKey = "voyageone.angular.directives.ifNoRows.tpl.html";
    // 没有数据显示警告
    if (!$templateCache.get(tempNoDataKey)) {
        $templateCache.put(tempNoDataKey, '<div class="text-center text-hs" id="noData"><h4 class="text-vo"><i class="icon fa fa-warning"></i>&nbsp;<span translate="TXT_ALERT"></span></h4><span translate="TXT_MSG_NO_DATA"></span></dv>');
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