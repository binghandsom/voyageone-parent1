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
}).directive("scale", function () {
    return {
        restrict: "A",
        require: ['ngModel'],
        link: function (scope, element, attr, ctrls) {

            var type = attr.type;
            var ngModelController = ctrls[0];

            if (!type)
                return;

            type = type.toLowerCase();

            if (type !== 'number')
                return;

            //默认为2位
            var scale = attr.scale ? +attr.scale : 2;

            ngModelController.$parsers.push(function (value) {

                var stringValue = value.toString();

                var regex = new RegExp("^\\d+(\\.\\d{1," + scale + "})?$");

                if (regex.test(stringValue))
                    return value;

                return ngModelController.$modelValue;
            });
        }
    };
});