/**
 * Created by linanbin on 15/12/7.
 */


define([
    'angularAMD'
], function (angularAMD) {
    angularAMD
        .controller('popupCtrl', popupCtrl);

    function popupCtrl($scope, $modal) {
        var vm = this;

        vm.formats = ['yyyy-MM-dd', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
        vm.format = vm.formats[0];

        $scope.openCustomBasePropety = openCustomBasePropety;

        function openCustomBasePropety(viewSize) {
            //var modalInstance = $modal.open({
            //    templateUrl: 'ims/addvaluepopup.html',
            //    controller: 'ModalInstanceCtrl',
            //    size: size,
            //    resolve: {
            //        items: function () {
            //            return $scope.items;
            //        }
            //    }
            //}
        }
    }
});