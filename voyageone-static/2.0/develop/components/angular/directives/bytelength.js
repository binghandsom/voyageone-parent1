/**
 * @Date: 2016-06-22 15:59:47
 * @User: Jonas
 */
(function () {
    /**
     * 抄自聚美后台 js
     */
    function sizeof(str) {
        if (str == undefined || str == null) {
            return 0;
        }
        var regex = str.match(/[^\x00-\xff]/g);
        return (str.length + (!regex ? 0 : regex.length));
    }

    /**
     * 第三方函数包装
     */
    function getByteLength(str) {
        return sizeof(str);
    }

    /**
     * directive 创建包装
     */
    function makeByteLength(attrName, checkLength) {
        return function () {
            return {
                restrict: "A",
                require: 'ngModel',
                scope: false,
                link: function (scope, element, attrs, ngModelController) {
                    var length = attrs[attrName];
                    if (!length)
                        return;

                    if(checkLength(getByteLength(scope.field.value), length)){
                        ngModelController.$setValidity(attrName,true);
                    }else{
                        ngModelController.$setValidity(attrName,false);
                    }

                    ngModelController.$parsers.push(function (viewValue) {
                        ngModelController.$setValidity(attrName, checkLength(getByteLength(viewValue), length));
                        return viewValue;
                    });

                }
            };
        };
    }

    angular.module("voyageone.angular.directives")
        .directive("maxbytelength", makeByteLength("maxbytelength", function (byteLength, maxLength) {
            return byteLength <= maxLength;
        }))
        .directive("minbytelength", makeByteLength("minbytelength", function (byteLength, minLength) {
            return byteLength >= minLength;
        }));
}());