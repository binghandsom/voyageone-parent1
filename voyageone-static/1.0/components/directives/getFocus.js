/**
 * Sets focus to this element if the value of focus-me is true.
 * @author sky
 * @Date 20150430
 * @example
 *  <a ng-click="addName=true">add name</a>
 *  <input ng-show="addName" type="text" ng-model="name" focus-me="{{addName}}" />
 */
define(["components/app"], function (app) {

    return app.directive('focusMe', ['$timeout', function ($timeout) {
        return {
            restrict: "A",
            scope: {
                trigger: '@focusMe'
            },
            link: function (scope, element) {

                scope.$watch('trigger', function (value) {

                    if (value === "true") $timeout(function () {
                        element[0].focus();
                    });
                });
            }
        };
    }]);
});