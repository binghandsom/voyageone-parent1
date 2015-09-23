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
                    var selectExp = attr.enterClick;

                    var targetElem = angular.element(selectExp);

                    if (!targetElem || !targetElem.length || targetElem.attr("disabled")) return;

                    if (e.keyCode == 13) targetElem.click();
                });
            }
        };
    });
});
