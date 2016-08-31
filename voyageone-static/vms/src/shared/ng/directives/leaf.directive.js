angular.module("vo.directives").directive("leaf", function($compile) {
    return {
        restrict: "E",
        replace: true,
        scope: {
            leaf: "="
        },
        //templateUrl: 'tree-li.html',
        template: '<li ng-class="{divider: leaf.name == \'divider\'}">'
                    +'<a ng-if="leaf.name !== \'divider\'">{{leaf.name}}</a>'
                   +'</li>',
        link: function(scope, element) {
            if (angular.isArray(scope.leaf.subtree)) {
                element.append("<tree tree='leaf.subtree'></tree>");
                element.addClass('dropdown-submenu');
                $compile(element.contents())(scope);
            } else {
                element.bind('click', function() {
                    alert("You have clicked on " + scope.leaf.name);
                });

            }
        }
    };
});