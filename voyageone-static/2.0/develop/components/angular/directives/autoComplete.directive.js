/**
 * @description 基于jquery autocomplete
 *                  url：https://github.com/devbridge/jQuery-Autocomplete
 *                  eg: 传递字符串数组,auto-complete="arrays",arrays:['','']
 * @User: piao
 * @Version: 1.0.0
 */
angular.module("voyageone.angular.directives").directive("autoComplete", function () {
    return {
        restrict: "A",
        require: "ngModel",
        scope: {
            matchArrays: "=autoComplete"
        },
        link: function (scope, element, attrs, ngModelCtl) {

            var _matchArray = angular.copy(scope.matchArrays);

            element.autocomplete({
                lookup: _.map(_matchArray,function (str) {
                    return {value:str};
                }),
                onSelect: function (suggestion) {
                    console.log('value', suggestion.value);
                    ngModelCtl.$setViewValue(suggestion.value);
                }
            });

        }
    };
});
