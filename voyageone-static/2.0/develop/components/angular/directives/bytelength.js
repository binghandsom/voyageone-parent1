/**
 * @Date: 2016-06-22 15:59:47
 * @User: Jonas
 */
(function () {
    /**
     * 原理参见来源: http://stackoverflow.com/questions/5515869/string-length-in-bytes-in-javascript
     */
    function lengthInUtf8Bytes(str) {
        // Matches only the 10.. bytes that are non-initial characters in a multi-byte sequence.
        var m = encodeURIComponent(str).match(/%[89ABab]/g);
        return str.length + (m ? m.length : 0);
    }

    /**
     * 第三方函数包装
     */
    function getByteLength(str) {
        return lengthInUtf8Bytes(str);
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
                    if (!length) return;
                    ngModelController.$parsers.push(function (viewValue) {
                        ngModelController.$setValidity('maxbytelength', checkLength(getByteLength(viewValue), length));
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