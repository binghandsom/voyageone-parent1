angular.module("vo.directives").directive("drawChart", function () {
    return {
        restrict: "A",
        controller: ["$scope", "$element", function ($scope, $element) {
            var ctx = $element.find('#myChart').getContext("2d");
            new Chart(ctx).Doughnut(data, options);
        }],
        link: function (scope, element, attrs, $element) {
            var ctx = $element.find('#myChart').getContext("2d");
            new Chart(ctx).Doughnut(data, options);
        }
    };
});