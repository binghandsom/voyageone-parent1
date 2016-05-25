
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope, jmPromotionService, confirm, $translate, cActions, notify, $location, cRoutes) {
        $scope.vm = {"modelList": [],
                    "jmMasterBrandList":[],
                    status: {
                        open: true
                    }};
        $scope.searchInfo = { };
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
            jmPromotionService.getListByWhere($scope.searchInfo).then(function (res) {
                $scope.vm.modelList = res.data;
            }, function (res) {
            })
        };
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.name).result.then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                jmPromotionService.delete(data.id).then(function () {
                    $scope.vm.modelList.splice(index, 1);
                }, function (res) {
                })
            });
        };
    }
    indexController.$inject = ['$scope', 'jmPromotionService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});