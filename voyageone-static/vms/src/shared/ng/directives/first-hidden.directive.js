/**
 * @Date: 2016-06-23 14:08:09
 * @User: Jonas
 */
angular.module("vo.directives").directive("firstHidden", function () {
    return {
        restrict: "C",
        scope: false,
        link: function (scope, element) {
            element.removeClass('first-hidden');
        }
    };
});
