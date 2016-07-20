angular.module("voyageone.angular.directives").directive("input", function () {
    return {
        restrict: "E",
        require: ['?ngModel'],
        link: function (scope, element, attr) {

            var type = attr.type;

            if (!type)
                return;

            type = type.toLowerCase();

            if (type !== 'number')
                return;

            element.on('keypress', function (event) {

                var charCode = event.charCode;
                var lastInputIsPoint = element.data('lastInputIsPoint');

                if (charCode !== 0 && charCode !== 46 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                    return;
                }

                if (charCode === 46) {

                    if (lastInputIsPoint || this.value.indexOf('.') > -1) {
                        event.preventDefault();
                        return;
                    }
                    element.data('lastInputIsPoint', true);
                    return;
                }

                element.data('lastInputIsPoint', false);
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