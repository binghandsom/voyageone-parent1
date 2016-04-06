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
        $scope.selectImport=function(){
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
               // console.log(res);
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        }
        $scope.selectExport=function(){
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                // console.log(res);
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        }
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