/**
 * @Name:    show-big-image.directive.js
 * @Date:    2015/5/8
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define(['components/app', 'underscore'], function(app){

    var template = "<tr ng-if='![dataSourceName] || ![dataSourceName].length'><td colspan='[colCount]' class='text-center' translate='WMS_NODATA'></td></tr>";

    return app.directive("ifNoRows", ["$compile", ifNoRows]);

    function ifNoRows($compile) {
        return {
            restrict: "A",
            link: function(scope, table, attr) {

                var dataSourceName = attr["ifNoRows"];

                if (!dataSourceName) throw "没有指定数据源的名称。这还怎么玩！！！";

                // 找第一行的总列数
                var colCount = table.find("tr:first").children("td,th").length;

                var html = template.replace(/\[dataSourceName\]/g, dataSourceName).replace(/\[colCount\]/, colCount);

                var link = $compile(html);

                var node = link(scope);

                table.find("tbody").append(node);

            }
        };
    }
});