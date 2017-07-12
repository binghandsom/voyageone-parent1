/**
 * @description 监听回车事件
 * @author piao
 */
angular.module("voyageone.angular.directives").directive('ngEnter',function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});