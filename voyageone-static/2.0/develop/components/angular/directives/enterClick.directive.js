/**
 * @Date: 2015-11-19 15:13:02
 * @User: Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.directives.enterClick", []).directive("enterClick", function () {
    return {
        restrict: "A",
        link: function (scope, elem, attr) {
            angular.element(elem).on("keyup", function (e) {
                if (e.keyCode !== 13) return;
                var selectExp = attr.enterClick;
                var targetElem, handler = function () {
                    targetElem.triggerHandler("click");
                };
                try {
                    targetElem = document.querySelector(selectExp);
                } catch (e) {
                    targetElem = null;
                }
                if (!targetElem) {
                    // 如果取不到元素，则尝试作为表达式执行
                    handler = function () {
                        scope.$eval(selectExp);
                    };
                } else {
                    targetElem = angular.element(targetElem);
                    // 如果元素存在，但是是禁用状态的，放弃执行
                    if (targetElem.attr("disabled")) return;
                }
                handler();
            });
        }
    };
});
