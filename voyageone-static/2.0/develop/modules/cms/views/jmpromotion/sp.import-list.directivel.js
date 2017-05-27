define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SPImportListDirectiveController($scope, $routeParams, cmsBtJmPromotionImportTaskService, cmsBtJmPromotionExportTaskService, jmPromotionDetailService, alert, confirm, $translate, $filter) {
        $scope.vm = {
            "promotionId": $routeParams.jmpromIdd,
            cmsBtJmPromotionImportTaskList: []
        };

        $scope.initialize = function () {
            $scope.searchImport();
        };

        $scope.searchImport = function () {
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.jmpromId).then(function (res) {
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        };

        $scope.downloadImportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadExcel", angular.toJson({id: id}));
        };

        $scope.downloadImportErrorExcel = function (id) {
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadImportErrorExcel", angular.toJson({id: id}));
        };

        /**导出excel方法*/
        function ExportExcel(action, source) {
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
        $scope.importSearch = function(page) {
            $scope.searchImport();
        }
    }

    cms.directive('spImportList', [function spImportListDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$scope', '$routeParams', 'cmsBtJmPromotionImportTaskService', 'cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'alert', 'confirm', '$translate', '$filter', SPImportListDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/sp.import-list.directive.html'
        }
    }]);
});