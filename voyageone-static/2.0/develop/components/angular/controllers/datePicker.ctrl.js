/**
 * @Description:
 * 弹出日期控件
 * @User:    Edward
 * @Version: 0.2.0, 2015-10-10
 */

angular.module('voyageone.angular.controllers.datePicker', [])
    .controller('datePickerCtrl', function ($scope) {
        var vm = this;

        vm.formats = ['yyyy-MM-dd', 'yyyy-MM-dd HH:mm:ss'];

        $scope.formatDate = vm.formats[0];
        $scope.formatDateTime = vm.formats[1];

        $scope.open = open;

        function open($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        }
    });