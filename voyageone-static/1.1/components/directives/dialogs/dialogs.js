/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 0.0.5
 */

define(["components/app", "underscore"], function (app) {

    return app

        .factory("$dialogs", ["$modal", "$filter", $dialogs])

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

    function $dialogs($modal, $filter) {

        function tran(translationId, values) {
            return $filter('translate')(translationId, values);
        }

        return function(options) {

            if (!_.isObject(options)) throw "arg type must be object";

            var values;

            if (_.isObject(options.content)) {

                values = options.content.values;
                options.content = options.content.id;
            }

            options.title = tran(options.title);
            options.content = tran(options.content, values);

            var modalInstance = $modal.open({
                templateUrl: "components/directives/dialogs/dialogs.tpl.html",
                controller: ["$scope", function (scope) {
                    _.extend(scope, options);
                }],
                size: 'md'
            });

            options.close = function () {
                modalInstance.dismiss('close')
            };

            options.ok = function () {
                modalInstance.close('');
            };

            return modalInstance;
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
