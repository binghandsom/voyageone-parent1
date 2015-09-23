//angular.module ('mainModule')
//    .directive ('setNgAnimate', ['$animate', function ($animate) {
//        return {
//            link: function ($scope, $element, $attrs) {
//                $scope.$watch (function () {
//                    return $scope.$eval ($attrs.setNgAnimate, $scope);
//                }, function (valnew, valold) {
//                    $animate.enabled (!!valnew, $element);
//                });
//            }
//        };
//    }]);
define (function (require) {

    var mainApp = require ('components/app');

    mainApp.directive ('setNgAnimate', ['$animate', function ($animate) {
        return {
            link: function ($scope, $element, $attrs) {
                $scope.$watch (function () {
                    return $scope.$eval ($attrs.setNgAnimate, $scope);
                }, function (valnew, valold) {
                    $animate.enabled (!!valnew, $element);
                });
            }
        };
    }]);
});