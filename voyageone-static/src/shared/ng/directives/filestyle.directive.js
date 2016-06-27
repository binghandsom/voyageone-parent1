/**
 * @Description:
 * 引入对上传框插件 fileStyle 的指令支持
 * @Date:    2015-11-19 17:35:22
 * @User:    Jonas
 * @Version: 2.0.0
 */
angular.module("vo.directives").directive("fileStyle", function () {
    return {
        restrict: "A",
        controller: ["$scope", "$element", function ($scope, $element) {
            var options = $element.data();
            // old usage support
            options.classInput = $element.data("classinput") || options.classInput;
            if ($element.filestyle) {
                $element.filestyle(options);
            }
        }]
    };
});
