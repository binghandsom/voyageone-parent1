angular.module("vo.directives").directive("leaf", function ($compile) {
    return {
        restrict: "E",
        replace: true,
        scope: {
            leaf: "="
        },
        template: '<li><a>{{leaf.catName}}</a></li>',
        link: function (scope, element) {
            if (angular.isArray(scope.leaf.children)) {
                element.append("<tree ng-if='leaf.children.length>0' tree='leaf.children'></tree>");
                element.addClass('dropdown-submenu');
                $compile(element.contents())(scope);
                element.bind('click', function (event) {
                    window.xx = event;
                    event.stopPropagation();
                    var para=document.createElement("span");
                    var node=document.createTextNode("已选择:"+scope.leaf.catName);
                    para.appendChild(node);
                    var element=document.getElementsByClassName("nav")[1];
                    element.appendChild(para);
                });
            } else {
            }
        }
    };
});