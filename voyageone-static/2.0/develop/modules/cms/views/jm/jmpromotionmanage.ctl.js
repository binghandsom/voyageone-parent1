
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, jmPromotionService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": []};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: $scope.search};
        $scope.datePicker = [];
        $scope.initialize = function () {
            //promotionService.init().then(function (res) {
            //    $scope.vm.platformTypeList = res.data.platformTypeList;
            //    $scope.vm.promotionStatus = res.data.promotionStatus;
            //    $scope.search();
            //});
        };
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.search = function () {
            jmPromotionService.getListByWhere($scope.searchInfo).then(function (res) {
                $scope.vm.modelList = res.data;
                $scope.groupPageOption.total = $scope.vm.modelList.size;
            }, function (res) {
            })
        };
    }
    indexController.$inject = ['$scope', 'jmPromotionService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});