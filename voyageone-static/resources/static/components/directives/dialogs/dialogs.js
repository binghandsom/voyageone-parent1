/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 0.0.5
 */

define(["components/app", "underscore"], function (app) {

    return app

        .factory("$dialogs", ["ngDialog", "$filter", $dialogs])

        .factory("vAlert", ["$dialogs", vAlert])

        .factory("vConfirm", ["$dialogs", vConfirm])

        .directive("vConfirm", ["vConfirm",
            function (confirm) {
                return {
                    restrict: "A",
                    link: function ($scope, ele, attr) {
                        var callKey = "yes";
                        ele.click(function () {
                            confirm(attr["vConfirm"]).then(function () {
                                if (ele.data(callKey)) $scope.$eval(ele.data(callKey));
                            });
                        });
                    }
                };
            }]);

    function $dialogs(ngDialog, $filter) {

        function tran(translationId, values) {
            return $filter('translate')(translationId, values);
        }

        return function(options) {
            // 必要属性有 isAlert, content, title。参见 tpl html。

            if (!_.isObject(options)) throw "arg type must be object";

            var values;

            // 为 translate 的格式化提供支持，检测类型并转换
            if (_.isObject(options.content)) {

                values = options.content.values;
                options.content = options.content.id;
            }

            // translate 如果找不到会返回原字符串，所以此处无需判断
            options.title = tran(options.title);
            options.content = tran(options.content, values);

            return ngDialog.openConfirm({
                template: "components/directives/dialogs/dialogs.tpl.html",
                controller: ["$scope", function (scope) {
                    _.extend(scope, options);
                }]
            });
        };
    }

    function vAlert($dialogs) {
        return function (content, title) {
            return $dialogs({
                title: title || 'CORE_TXT_ALERT',
                content: content,
                isAlert: true
            });
        };
    }

    function vConfirm($dialogs) {
        return function (content, title) {
            return $dialogs({
                title: title || 'CORE_TXT_CONFIRM',
                content: content,
                isAlert: false
            });
        };
    }
});