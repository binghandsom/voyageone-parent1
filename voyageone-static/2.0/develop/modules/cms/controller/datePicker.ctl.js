/**
 * Created by linanbin on 15/12/5.
 */

define([
    'angularAMD'
], function (angularAMD) {
    angularAMD
        .controller('datePickerCtrl', datePickerCtrl);

    function datePickerCtrl($scope) {
        var vm = this;

        vm.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];

        $scope.format = vm.formats[0];

        $scope.open = open;

        function open($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        }
    }
});
