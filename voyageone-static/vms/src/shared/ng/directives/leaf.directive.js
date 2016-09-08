angular.module("vo.directives").directive("leaf", function ($compile) {
    return {
        restrict: "E",
        replace: true,
        scope: {
            leaf: "=",
            result: "="
        },
        template: '<li><a>{{leaf.catName}}</a></li>',
        link: function (scope, element) {
            if (angular.isArray(scope.leaf.children) && scope.leaf.children.length > 0) {
                element.append("<tree ng-if='leaf.children.length>0' tree='leaf.children' result='result'></tree>");
                element.addClass('dropdown-submenu');
            }

            element.bind('mouseup', function (event) {
                event.stopPropagation();
                var paraSpan = document.createElement("span");
                paraSpan.innerHTML = "<label class='selectedCat' title = '点击取消选择' ng-click='ctrl.deleCat()'><i class='fa fa-close'></i>&nbsp;&nbsp;</label>";

                var node = document.createTextNode("已选择:" + scope.leaf.catPath.replace(/-/g, "/"));
                paraSpan.appendChild(node);

                var navElement = document.getElementsByClassName("nav")[1];
                navElement.firstElementChild.className = "dropdown";
                navElement.appendChild(paraSpan);
                scope.result.push(scope.leaf.catPath.replace(/-/g, "/"));
            });
            $compile(element.contents())(scope);
        }
    };
});