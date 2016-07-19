angular.module("voyageone.angular.directives").directive("input", function () {
    return {
        restrict: "E",
        require: ['?ngModel'],
        link: function (scope, element, attr, ctrls) {

            var type = attr.type;
            var ngModelController = ctrls[0];

            if (!type)
                return;

            type = type.toLowerCase();

            if (type !== 'number')
                return;

            ngModelController.$parsers.unshift(function (value) {
                if (!value) {
                    element.val(ngModelController.$modelValue);
                    return ngModelController.$modelValue;
                }
                return value;
            });
        }
    };
});