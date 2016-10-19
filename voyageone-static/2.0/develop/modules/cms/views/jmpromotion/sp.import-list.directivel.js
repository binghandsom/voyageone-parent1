define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SPImportListDirectiveController($scope,$routeParams,cmsBtJmPromotionImportTaskService,cmsBtJmPromotionExportTaskService,jmPromotionDetailService, alert, confirm, $translate, $filter)
    {
        $scope.datePicker = [];
        $scope.vm = {
            "promotionId": $routeParams.parentId,
            cmsBtJmPromotionImportTaskList: [],
        };
        $scope.initialize = function () {
            $scope.searchImport();
        };
        $scope.searchImport = function () {
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        }
    }
    cms.directive('spImportList', [function spImportListDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$scope','$routeParams', 'cmsBtJmPromotionImportTaskService', 'cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'alert', 'confirm', '$translate', '$filter', SPImportListDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/sp.import-list.directive.html'
        }
    }]);
});