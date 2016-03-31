
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, jmPromotionService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": [],"jmMasterBrandList":[]};
        $scope.searchInfo = {};
        $scope.datePicker = [];
        $scope.initialize = function () {
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
                $scope.search();
            });
        };
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.search = function () {
           // console.log($scope.searchInfo);
            jmPromotionService.getListByWhere($scope.searchInfo).then(function (res) {
                console.log(res);
                $scope.vm.modelList = res.data;
               // $scope.groupPageOption.total = $scope.vm.modelList.size;
            }, function (res) {
            })
        };
    }
    indexController.$inject = ['$scope', 'jmPromotionService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});