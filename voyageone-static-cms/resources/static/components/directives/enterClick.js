/**
 * @User: Jonas
 * @Date: 2015-04-27 10:19:34
 * @Version: 0.0.2
 */

define(["components/app"], function (app) {

    return app.directive("enterClick", function () {

        return {
            restrict: "A",
            link: function (scope, elem, attr) {
                elem.keyup(function (e) {

                    if (e.keyCode !== 13) return;

                    var selectExp = attr.enterClick;
                    var targetElem, handler = function () {
                        targetElem.click();
                    };

                    try {
                        targetElem = angular.element(selectExp);
                    } catch (e) {
                        targetElem = null
                    }

                    if (!targetElem || !targetElem.length) {
                        // 如果取不到元素，则尝试作为表达式执行
                        handler = function () {
                            scope.$eval(selectExp);
                        };
                    } else if (targetElem.attr("disabled")) {
                        // 如果元素存在，但是是禁用状态的，放弃执行
                        return;
                    }

                    handler();
                });
            }
        };
    });
});
