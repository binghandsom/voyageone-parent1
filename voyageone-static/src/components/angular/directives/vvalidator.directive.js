/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/25
 */

function vv_getByteLength(value) {
    var byteLen = 0, len = value.length;
    if (value) {
        for (var i = 0; i < len; i++) {
            if (value.charCodeAt(i) > 255) {
                byteLen += 2;
            } else {
                byteLen++;
            }
        }
        return byteLen;
    } else {
        return 0;
    }
}

angular.module("vo.directives").directive("charMaxlength", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxlength = -1;
            attr.$observe("charMaxlength", function (value) {
                var intVal = parseInt(value);
                maxlength = isNaN(intVal) ? -1 : intVal;
                ctrl.$validate();
            });
            ctrl.$validators.maxlength = function (modelValue, viewValue) {
                return maxlength < 0 || ctrl.$isEmpty(viewValue) || vv_getByteLength(viewValue) <= maxlength;
            };
        }
    };
}).directive("charMinlength", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minlength = -1;
            attr.$observe("charMinlength", function (value) {
                var intVal = parseInt(value);
                minlength = isNaN(intVal) ? -1 : intVal;
                ctrl.$validate();
            });
            ctrl.$validators.minlength = function (modelValue, viewValue) {
                return minlength < 0 || ctrl.$isEmpty(viewValue) || vv_getByteLength(viewValue) >= minlength;
            };
        }
    };
}).directive("ngMaxvalue", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxvalue = -1;
            attr.$observe("ngMaxvalue", function (value) {
                if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) maxvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) maxvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else maxvalue = -1;
                ctrl.$validate();
            });
            ctrl.$validators.maxvalue = function (modelValue, viewValue) {
                return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue <= maxvalue;
            };
        }
    };
}).directive("ngMinvalue", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minvalue = -1;
            attr.$observe("ngMinvalue", function (value) {
                if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) minvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) minvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else minvalue = -1;
                ctrl.$validate();
            });
            ctrl.$validators.minvalue = function (modelValue, viewValue) {
                return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue >= minvalue;
            };
        }
    };
}).directive("ngMaxinputnum", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxvalue = -1;
            attr.$observe("ngMaxinputvalue", function (value) {
                maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
                ctrl.$validate();
            });
            ctrl.$validators.maxinputnum = function (modelValue, viewValue) {
                return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length <= maxvalue;
            };
        }
    };
}).directive("ngMininputnum", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minvalue = -1;
            attr.$observe("ngMininputnum", function (value) {
                minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
                ctrl.$validate();
            });
            ctrl.$validators.mininputnum = function (modelValue, viewValue) {
                return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length >= minvalue;
            };
        }
    };
});
