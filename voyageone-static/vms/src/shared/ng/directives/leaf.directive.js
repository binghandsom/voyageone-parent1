angular.module("vo.directives").directive("leaf", function ($compile) {
    return {
        restrict: "E",
        replace: true,
        scope: {
            leaf: "=",
            result: "=",
            search: '='
        },
        template: '<li><a>{{leaf.catName}}</a></li>',
        link: function (scope, element) {
            if (angular.isArray(scope.leaf.children) && scope.leaf.children.length > 0) {
                element.append("<tree ng-if='leaf.children.length>0' tree='leaf.children' result='result' search='search'></tree>");
                element.addClass('dropdown-submenu');
            }

            element.bind('mouseup', function (event) {
                event.stopPropagation();
                var navElement = document.getElementsByClassName("nav")[1];
                navElement.firstElementChild.className = "dropdown";
                if (scope.result.indexOf(scope.leaf.catPath) < 0) {
                    scope.result.push(scope.leaf.catPath);
                }
                scope.search.search();
            });
            $compile(element.contents())(scope);
        }
    };
});