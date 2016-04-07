/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function detailController($scope, jmPromotionService,cmsBtJmPromotionImportTaskService,cmsBtJmPromotionExportTaskService, jmPromotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {
        $scope.datePicker = [];
        $scope.vm = {"promotionId": $routeParams.parentId,modelList:[],cmsBtJmPromotionImportTaskList:[],cmsBtJmPromotionExportTaskList:[]};
        $scope.searchInfo={cmsBtJmPromotionId: $routeParams.parentId};
        $scope.parentModel={};
        $scope.initialize = function () {
            jmPromotionService.get($routeParams.parentId).then(function (res) {
                    $scope.parentModel = res.data;
                });
            $scope.search();
        };
        $scope.search = function () {
           // console.log("searchInfo");
           // console.log($scope.searchInfo);
            loadSearchInfo();
            jmPromotionDetailService.getPromotionProductInfoListByWhere($scope.searchInfo).then(function (res) {
                //console.log(res);
                $scope.vm.modelList = res.data;
                // $scope.groupPageOption.total = $scope.vm.modelList.size;
            }, function (res) {
            })
        };
        $scope.searchImport=function(){
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
               // console.log(res);
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.searchExport=function(){
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                // console.log(res);
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.addExport=function(templateType) {
            var model={templateType:templateType,cmsBtJmPromotionId:$scope.vm.promotionId};
            cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
                $scope.searchExport();
            }, function (res) {

            });
        }
        $scope.downloadImportExcel=function(id){
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadExcel",angular.toJson({id:id}));
        }
        $scope.downloadImportErrorExcel=function(id)
        {
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadImportErrorExcel",angular.toJson({id:id}));
        }
        $scope.downloadExportExcel=function(id){
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionExportTask/index/downloadExcel",angular.toJson({id:id}));
        }
        function ExportExcel(action,source)//导出excel方法
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
        function loadSearchInfo()
        {
            $scope.searchInfo.synchStateList=[];
            if( $scope.searchInfo.synchState0)
            {
                $scope.searchInfo.synchStateList.push(0)
            }
            if( $scope.searchInfo.synchState1)
            {
                $scope.searchInfo.synchStateList.push(1)
            }
            if( $scope.searchInfo.synchState2)
            {
                $scope.searchInfo.synchStateList.push(2)
            }
            if( $scope.searchInfo.synchState3)
            {
                $scope.searchInfo.synchStateList.push(3)
            }
        }
    }

    detailController.$inject = ['$scope', 'jmPromotionService','cmsBtJmPromotionImportTaskService','cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory'];
    return detailController;
});