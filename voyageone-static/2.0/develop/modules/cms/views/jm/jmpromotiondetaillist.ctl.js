/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function detailController($scope, jmPromotionService, jmPromotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {
        $scope.datePicker = [];
        $scope.vm = {"promotionId": $routeParams.parentId,modelList:[]};
        $scope.searchInfo={cmsBtJmPromotionId: $routeParams.parentId};
        $scope.parentModel={};
        $scope.initialize = function () {
            jmPromotionService.get($routeParams.parentId).then(function (res) {
                    $scope.parentModel = res.data;
                });
            $scope.search();
        };
        $scope.search = function () {
            console.log("searchInfo");
            console.log($scope.searchInfo);
            jmPromotionDetailService.getListByWhere($scope.searchInfo).then(function (res) {
                console.log(res);
                $scope.vm.modelList = res.data;
                // $scope.groupPageOption.total = $scope.vm.modelList.size;
            }, function (res) {
            })
        };
    }

    detailController.$inject = ['$scope', 'jmPromotionService', 'jmPromotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory'];
    return detailController;
});