/**
 * 引入对上传框插件 filestyle 的指令支持
 *
 * @Date:    2015-07-28 14:55
 *
 * @User:    Jonas
 * @Version: 0.0.1
 */
define(["components/app"], function (app) {
    app.directive('filestyle', function () {
        return {
            restrict: 'A',
            controller: ["$scope", "$element", function ($scope, $element) {
                var options = $element.data();

                // old usage support
                options.classInput = $element.data('classinput') || options.classInput;

                $element.filestyle(options);
            }]
        };
    });
});
