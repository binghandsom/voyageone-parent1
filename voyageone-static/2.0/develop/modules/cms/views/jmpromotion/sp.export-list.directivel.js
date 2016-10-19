define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SPExportListDirectiveController($scope,$routeParams,cmsBtJmPromotionImportTaskService,cmsBtJmPromotionExportTaskService,jmPromotionDetailService, alert, confirm, $translate, $filter)
    {
        $scope.vm = {
            "promotionId": $routeParams.parentId,
            cmsBtJmPromotionExportTaskList: [],
        };
        $scope.initialize = function () {
            $scope.searchExport();
        };
        $scope.searchExport = function () {
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        }
    }
    cms.directive('spExportList', [function spExportListDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$scope','$routeParams', 'cmsBtJmPromotionImportTaskService', 'cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'alert', 'confirm', '$translate', '$filter', SPExportListDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/sp.export-list.directive.html'
        }
    }]);
});