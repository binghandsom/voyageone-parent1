(function() {
    /**
     * @Description:
     * 显示html的popover的共同方法
     * @User: linanbin
     * @Version: 2.0.0, 15/12/14
     */
    angular.module("voyageone.angular.controllers.showPopover", []).controller("showPopoverCtrl", function($scope) {
        $scope.showInfo = showInfo;
        function showInfo(values) {
            var tempHtml = "";
            if (values instanceof Array) {
                angular.forEach(values, function(data, index) {
                    tempHtml += data;
                    if (index !== values.length) {
                        tempHtml += "<br>";
                    }
                });
            } else  {
                tempHtml += values;
            }
            return tempHtml;
        }
    });
})();