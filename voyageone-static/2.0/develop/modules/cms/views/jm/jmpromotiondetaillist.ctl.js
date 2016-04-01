/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function detailController($scope, jmPromotionService, jmPromotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {
        $scope.datePicker = [];
        $scope.vm = {"promotionId": $routeParams.parentId};
        $scope.initialize = function () {
            //promotionService.init().then(function (res) {
            //    $scope.vm.platformTypeList = res.data.platformTypeList;
            //    $scope.vm.promotionStatus = res.data.promotionStatus;
            //    promotionService.getPromotionList({"promotionId": $routeParams.parentId}).then(function (res) {
            //        $scope.vm.promotion = res.data[0];
            //        $scope.promotionOld = _.clone($scope.vm.promotion);
            //        if ($scope.vm.promotion.tejiabaoId != "0") {
            //            $scope.vm.promotion.tejiabao = true;
            //        }
            //    });
            //});
            //$scope.search();
        };
    }
    detailController.$inject = ['$scope', 'jmPromotionService', 'jmPromotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory'];
    return detailController;
});