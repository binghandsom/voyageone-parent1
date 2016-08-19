/**
 * Created by sofia on 2016/7/22.
 */
(function() {
    angular.module("voyageone.angular.filter", []).filter("stringCutter", function() {
        return function (value, wordWise, max, tail) {
            if (!value) return '';

            max = parseInt(max, 10);
            if (!max) return value;
            if (value.length <= max) return value;

            value = value.substr(0, max);
            if (wordWise) {
                var lastSpace = value.lastIndexOf(' ');
                if (lastSpace != -1) {
                    value = value.substr(0, lastSpace);
                }
            }
            return value + (tail || ' …');
        };
    });
})();