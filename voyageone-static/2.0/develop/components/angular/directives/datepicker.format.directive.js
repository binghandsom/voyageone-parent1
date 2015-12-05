/**
 * @Date: 2015-11-19 15:13:02
 * @User: Jonas
 * @Version: 2.0.0
 */

angular.module('voyageone.angular.directives.datepickerFormat', [])
  .directive('datepickerFormat', function ($filter) {
    return {
      restrict: "A",
      require: 'ngModel',
      link: function (scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function (viewValue) {
          return $filter('date')(viewValue, attrs.datepickerFormat || 'yyyy-MM-dd HH:mm:ss');
        });
      }
    };
  });
