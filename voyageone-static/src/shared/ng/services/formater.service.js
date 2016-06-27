/**
 * @Date: 2015-11-19 15:13:02
 * @User: Jonas
 * @Version: 2.0.0
 */
angular.module("vo.directives").directive("dateModelFormat", function ($filter) {
    return {
        restrict: "A",
        require: "ngModel",
        link: function (scope, elem, attrs, ngModel) {
            ngModel.$parsers.push(function (viewValue) {
                return $filter("date")(viewValue, attrs.dateModelFormat || "yyyy-MM-dd HH:mm:ss");
            });
        }
    };
});
