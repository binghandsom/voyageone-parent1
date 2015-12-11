/**
 * @Description:
 * table中无数据范围的数据
 * @User: linanbin
 * @Version: 2.0.0, 15/12/11
 */

angular.module('voyageone.angular.directives.ifNoRows', [])
    .directive('ifNoRows', function ($templateCache, $compile) {

        var tempNoDataKey = "voyageone.angular.directives.ifNoRows.tpl.html";

        // 没有数据显示警告
        if (!$templateCache.get(tempNoDataKey)) {
            $templateCache.put(tempNoDataKey,
            '<div class="alert text-center">\n' +
            '    <h4><i class="icon fa fa-warning"></i>&nbsp;{{\'TXT_COM_WARNING\' | translate}}</h4>\n' +
            '{{\'TXT_COM_MSG_NO_DATE\' | translate}}' +
            '</dv>');
        }

        return {
            restrict: "A",
            replace: false,
            scope: {
                $$data: "@ifNoRows"
            },
            link: function (scope, element) {

                // 如果数据不存在则显示警告信息
                if(scope.$parent.$eval(scope.$$data)  == 0) {
                    element.append($compile($templateCache.get(tempNoDataKey))(scope));
                }
            }
        };
    });
