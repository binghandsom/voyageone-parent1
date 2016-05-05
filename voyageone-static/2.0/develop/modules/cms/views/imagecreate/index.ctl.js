/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    function detailController($scope,cmsMtImageCreateService, $uibModal, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory) {
        $scope.vm = {modelList:[]};
        $scope.dataPageOption = {curr: 1,size:5, total: 0, fetch: goPage.bind(this)}
        $scope.initialize = function () {

            $scope.search();
        };
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.search = function () {
            // console.log("searchInfo");
            // console.log($scope.searchInfo);
             goPage(1,10)
            var data={};
            cmsMtImageCreateService.getCountByWhere(data).then(function (res) {
                $scope.dataPageOption.total = res;
            }, function (res) {
            });
        };
        function  goPage(pageIndex,size) {
//(pageIndex - 1) * size;
            var data ={};// angular.copy($scope.searchInfo);
            data.start = ($scope.dataPageOption.curr - 1) *   $scope.dataPageOption.size
            data.length =  $scope.dataPageOption.size;
            cmsMtImageCreateService.getPageByWhere(data).then(function (res) {
                $scope.vm.modelList = res;
            }, function (res) {
            })
        }


        $scope.searchImport=function(){
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
               // console.log(res);
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.downloadImportExcel=function(id){
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/imagecreate/index/downloadExcel",angular.toJson({id:id}));
        }
        $scope.downloadImportErrorExcel=function(id)
        {
            ExportExcel("/cms/imagecreate/index/downloadImportErrorExcel",angular.toJson({id:id}));
        }

        $scope.openImageCreateImport = function () {
            var controllerUrl="modules/cms/views/imagecreate/pop/import.ctl";
            var templateUrl="views/imagecreate/pop/import.tpl.html";
            var controller="popImageCreateImportCtl";
            require([controllerUrl], function () {
                var modalInstance = $uibModal.open({
                    templateUrl: templateUrl,
                    controller:controller,
                    resolve: {
                        context: function () {
                            return null;
                        }
                    }
                });
                modalInstance.result.then(function () {
                    $scope.search();
                })
            });
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
    }
    detailController.$inject = ['$scope','cmsMtImageCreateService', '$uibModal', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory'];
    return detailController;
});