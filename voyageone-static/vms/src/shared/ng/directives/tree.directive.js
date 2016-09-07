/**
 * Created by sofia on 2016/8/30.
 */
angular.module("vo.directives").directive("tree", function() {
    return {
        restrict: "E",
        replace: true,
        scope: {
            tree: '=',
            result: '='
        },
        template: '<ul class="dropdown-menu">'
                  +'<leaf ng-repeat="leaf in tree" leaf="leaf" result="result"></leaf>'
                  +'</ul>'
    };
});