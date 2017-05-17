define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SPExportListDirectiveController($scope,$routeParams,cmsBtJmPromotionImportTaskService,cmsBtJmPromotionExportTaskService,jmPromotionDetailService, alert, confirm, $translate, $filter)
    {
        $scope.vm = {
            "promotionId":$routeParams.jmpromId,
            cmsBtJmPromotionExportTaskList: [],
        };
        $scope.initialize = function () {
            $scope.searchExport();
        };
        $scope.searchExport = function () {
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.jmpromId).then(function (res) {
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        };
        $scope.downloadExportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionExportTask/index/downloadExcel", angular.toJson({id: id}));
        };

        function ExportExcel(action, source)//导出excel方法
        {
            var Form = document.createElement("FORM");
            document.body.appendChild(Form);
            Form.method = "POST";
            var newElement = $("<input name='source' type='hidden' />")[0];
            Form.appendChild(newElement);
            newElement.value = source;
            var IsExcelElement = $("<input name='IsExcel' type='hidden' />")[0];
            Form.appendChild(IsExcelElement);
            IsExcelElement.value = 1;
            Form.action = action;
            Form.submit();
        };

        // 查询数据文件创建的状态
        $scope.exportSearch = function(page) {
            $scope.searchExport();
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