/**
 * @description:
 * 为 jQuery 的插件 stickUp 提供 angular 风格包装
 *
 * @example: <stick-up>....</stick-up>
 * @user:    jonas
 * @version: 0.2.8
 */
angular.module("voyageone.angular.directives").directive("stickUp", function () {
    return {
        restrict: "E",
        scope: false,
        compile: function stickUpCompile(element) {
            $(document).ready(function () {
                element.stickUp();
            });
        }
    };
});
