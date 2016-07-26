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
            var scale , _length;

            var _numArr =  attr.scale.split(",");

            if(_numArr.length !== 2){

                console.warn("scale格式为{ 位数 },{ 精度 } 默认值=》位数：15位，精度为小数点2位。");

                /**设置默认值 长度为15  小数点精度为2位*/
                _length = 15;
                scale = 2;

            }else{

                _length = _numArr[0];
                scale = _numArr[1];

            }

            element.on('keyup', function () {

                var regex;

                if(scale != 0)
                    regex = new RegExp("^\\d+(\\.\\d{1," + scale + "})?$");
                else
                    regex = new RegExp("^\\d+$");


                if (regex.test(this.value))
                    return;

                ngModelController.$setViewValue(this.value.substr(0, this.value.length - 1));
                ngModelController.$render();

            }).on("keypress",function(event){

                var _value = angular.copy(this.value);

                if(_value.toString().length >= _length){
                    event.preventDefault();
                }

            });
        }
    };
});