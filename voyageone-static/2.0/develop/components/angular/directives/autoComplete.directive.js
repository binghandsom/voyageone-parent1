/**
 * @description 基于jquery autocomplete
 *                  url：https://github.com/devbridge/jQuery-Autocomplete
 * @User: piao
 * @Version: 1.0.0
 */
angular.module("voyageone.angular.directives").directive("autoComplete", function () {
    return {
        restrict: "A",
        require: "ngModel",
        link: function (scope, element, attrs, ngModelCtl) {

            var countries = [
                { value: 'Andorra', data: 'AD' },
                // ...
                { value: 'Zimbabwe', data: 'ZZ' }
            ];

            element.autocomplete({
                lookup: countries,
                onSelect: function (suggestion) {
                    console.log('You selected: ' + suggestion.value + ', ' + suggestion.data);
                }
            });

        }
    };
});
