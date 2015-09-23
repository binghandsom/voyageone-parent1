//angular.module ('mainModule')
//    .directive ('uiFocus', function ($timeout, $parse) {
//        return {
//            link: function (scope, element, attr) {
//                var model = $parse (attr.uiFocus);
//                scope.$watch (model, function (value) {
//                    if (value === true) {
//                        $timeout (function () {
//                            element[0].focus ();
//                        });
//                    }
//                });
//                element.bind ('blur', function () {
//                    scope.$apply (model.assign (scope, false));
//                });
//            }
//        };
//    });
define (function (require) {

    var mainApp = require ('components/app');

    mainApp.directive ('uiFocus', function ($timeout, $parse) {
        return {
            link: function (scope, element, attr) {
                var model = $parse (attr.uiFocus);
                scope.$watch (model, function (value) {
                    if (value === true) {
                        $timeout (function () {
                            element[0].focus ();
                        });
                    }
                });
                element.bind ('blur', function () {
                    scope.$apply (model.assign (scope, false));
                });
            }
        };
    });
});