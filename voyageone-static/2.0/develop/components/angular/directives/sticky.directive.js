/**
 * @description:
 * 为 jQuery 的插件 stickUp 提供 angular 风格包装
 *
 * @example: <sticky>....</sticky>
 * @user:    jonas
 * @version: 0.2.8
 */
angular.module("voyageone.angular.directives").directive("sticky", function () {
    return {
        restrict: "E",
        scope: false,
        link: function stickyPostLink(scope, element, attr) {
            var $document = $(document);
            var top = parseInt(element.css('top'));
            var topFix = parseInt(attr.topFix) || 0;
            $document.on('scroll', function () {
                var scrollTop = parseInt($document.scrollTop());
                if (scrollTop > top + topFix) {
                    element.css('top', scrollTop - topFix + 'px');
                } else {
                    element.css('top', top + 'px');
                }
            });
        }
    };
});
