/**
 * @Description:
 * 用于显示过多内容的text,基于bootstrap的popover;
 * content暂时只支持字符串
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-4-20
 */
(function() {
    angular.module("voyageone.angular.directives.popoverText", []).directive("popoverText", function() {
        return {
            restrict: "AE",
            transclude : true,
            template: '<small  popover-html="content" popover-placement="{{direct}}" >'+
                      '<div class="table_main">'+
                      '<ul>'+
                      '<li></li>'+
                      '</ul>'+
                      '</div></small>',
            replace : true,
            scope: {
                content: "=content",
                direct:"@direct"
            },
            link: function(scope, element) {
                var li = $(element).find("li");
                li.html(scope.content);
                if(li.html().length >= 100)
                    li.html(li.html().substr(0,100) + '...').css({cursor:'pointer'});
                else
                    li.html(li.html());
            }
        };
    });
})();