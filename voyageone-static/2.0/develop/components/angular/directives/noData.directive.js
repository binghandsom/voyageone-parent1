/**
 * @Description:
 * table中无数据范围的数据
 * @User: linanbin
 * @Version: 2.0.0, 15/12/11
 */

angular.module('voyageone.angular.directives.noData', [])
    .directive('noData', function ($templateCache, $compile) {

        var templateKey = "voyageone.angular.directives.noData.tpl.html";

        // 有数据分页样式
        if (!$templateCache.get(templateKey)) {
            $templateCache.put(templateKey,
            '<div class="alert alert-warning alert-dismissable">\n' +
            '   <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>\n' +
            '<h4><i class="icon fa fa-warning"></i>&nbsp;{{\'TXT_COM_WARNING\' | translate}}</h4>\n' +
            '{{\'TXT_COM_MSG_NO_DATE\' | translate}}' +
            '</div>');
        }

        return {
            restrict: "A",
            replace: false,
            scope: {
                $$data: "@noData"
            },
            link: function (scope, element) {

                // 如果数据不存在则显示警告信息
                if(scope.$parent.$eval(scope.$$data).length  == 0) {
                    element.append($compile($templateCache.get(templateKey))(scope));
                }
            }
        };
    });
