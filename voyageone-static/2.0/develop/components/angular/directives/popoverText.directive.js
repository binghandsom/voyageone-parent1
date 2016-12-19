/**
 * @Description:
 * 用于显示过多内容的text,基于bootstrap的popover;
 * content暂时只支持字符串
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-4-20
 */

angular.module("voyageone.angular.directives").directive("popoverText", function () {
    return {
        restrict: "AE",
        transclude: true,
        template: '<small popover-html="content" popover-placement="{{::direct}}"><div class="table_main"><ul><li class="cur-p"></li></ul></div></small>',
        replace: true,
        scope: {
            content: "=content",
            direct: "@direct",
            size: "@size"
        },
        link: function (scope, element) {

            var content = scope.content,
                size = scope.size;
                var li = $(element).find("li");

            if (content.length > scope.size)
                li.html(content.substr(0, size) + '...');
            else
                li.html(content).css({cursor: 'default'});
        }
    };
});
