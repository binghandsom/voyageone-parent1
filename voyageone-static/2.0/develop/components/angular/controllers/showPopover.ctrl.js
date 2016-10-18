/**
 * @Description:
 * 显示html的popover的共同方法
 * @User: linanbin
 * @Version: 2.0.0, 15/12/14
 */
angular.module("voyageone.angular.controllers").controller("showPopoverCtrl", function ($scope) {
    $scope.showInfo = showInfo;
    function showInfo(values) {
        if (values == undefined || values == '') {
            return '';
        } else if (values.value == undefined) {
            var tempHtml = "";
            if (values instanceof Array) {
                angular.forEach(values, function (data, index) {
                    tempHtml += data;
                    if (index !== values.length) {
                        tempHtml += "<br>";
                    }
                });
            } else {
                tempHtml += values;
            }
        } else {
            if (values.isUseComplexTemplate == true) {
                $scope.dynamicPopover = {
                    type: values.type,
                    value1: values.value,
                    value2: values.value2,
                    templateUrl: 'dynamicPopoverTemplate.html'
                };
            }
        }
        return tempHtml;
    }
});
