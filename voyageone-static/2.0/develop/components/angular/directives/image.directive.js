/**
 * @Description: 用于替换成cms2中可显示的图片url
 *
 * @User: linanbin
 * @Version: 2.0.0, 16/5/12
 */
angular.module("voyageone.angular.directives.image", []).directive("image", function () {
    return {
        restrict: "A",
        scope: {
            image: "@"
        },
        link: function (scope, element) {
            if (scope.image != null && scope.image != "")
                element[0].src = scope.$root.imageUrl.replace('%s', scope.image) + ".jpg";
        }
    };
});

