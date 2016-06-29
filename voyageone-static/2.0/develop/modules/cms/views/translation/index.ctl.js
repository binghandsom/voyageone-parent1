/**
 * Created by 123 on 2016/2/16.
 */
/**
 * @Description:
 *
 * @User: lewis
 * @Version: 1.0.0, 2016/02/18
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {

    function translationDetail($scope, $routeParams, $translate, translationService, notify, confirm, alert) {
        $scope.vm = {
            searchInfo : {},
            taskInfos: {},
            prodPageOption : {curr: 1, total: 0, fetch: searchHistoryTasks},
            sortFieldOptions : [],
            lenInfo : {},
            getTaskInfo : {
                distributeRule: 1,
                distributeCount: 10,
                sortCondition: "",
                sortRule: ""
            }
        };
        $scope.btnclick = false;
        $scope.searchHistoryTasks = searchHistoryTasks;
        // 获取初始化数据
        $scope.initialize = function() {
            $scope.vm.prodPageOption.curr = 1;
            translationService.getTasks()
                .then(function (res) {
                    $scope.vm.taskInfos = res.data.taskInfos;
                    $scope.vm.prodPageOption.total = res.data.taskInfos.prodListTotal;
                    $scope.vm.sortFieldOptions = res.data.sortFieldOptions;
                    $scope.vm.lenInfo = res.data.lenSetInfo;
                })
        };

        // 分发翻译任务.
        $scope.assignTasks = function() {
            translationService.getTasks()
                .then(function (res) {
                    if (res.data.taskInfos.prodListTotal > 0) {
                        alert($translate.instant('TXT_MSG_HAVE_UN_TRANSLATED_TASK'));
                    } else if ($scope.vm.getTaskInfo.distributeCount > 20) {
                        alert("最多不能超过20个任务!")
                    } else if ($scope.vm.getTaskInfo.distributeCount > $scope.vm.taskInfos.totalUndoneCount) {
                        alert("获取任务数量不能超过剩余任务数量！")
                    } else {
                        translationService.assignTasks($scope.vm.getTaskInfo)
                            .then(function (res) {
                                $scope.vm.taskInfos = res.data;
                                $scope.vm.prodPageOption.total = res.data.prodListTotal;
                                $scope.vm.searchInfo = {};
                            })
                    }
                })
        };

        // 暂存当前任务
        $scope.saveTask = function(productItem, index) {
            translationService.saveTask(productItem)
                .then(function (res){
                    $scope.vm.taskInfos.productTranslationBeanList[index].modifiedTime = res.data.modifiedTime;
                    $scope.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                    $scope.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                    $scope.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
        };

        // 提交当前任务.
        $scope.submitTask = function(productItem, index) {
            translationService.submitTask(productItem)
                .then(function (res){
                    $scope.vm.taskInfos.productTranslationBeanList.splice(index,1);
                    $scope.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                    $scope.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                    $scope.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $scope.vm.prodPageOption.total = $scope.vm.prodPageOption.total - 1;
                })
        };

        // 从主产品拷贝翻译信息.
        $scope.copyFormMainProduct = function (productItem,index) {
            translationService.copyFormMainProduct(productItem)
                .then(function (res){
                    $scope.vm.taskInfos.productTranslationBeanList[index] = res.data;
                })
        };

        // 查询历史任务.
        function searchHistoryTasks(page) {
            $scope.btnclick = true;
            $scope.vm.prodPageOption.curr = !page ? $scope.vm.prodPageOption.curr : page;
            $scope.vm.searchInfo.pageNum = $scope.vm.prodPageOption.curr;
            $scope.vm.searchInfo.pageSize = $scope.vm.prodPageOption.size;

            translationService.searchHistoryTasks($scope.vm.searchInfo)
                .then(function (res) {
                    $scope.vm.taskInfos.productTranslationBeanList = res.data.productTranslationBeanList;
                    $scope.vm.prodPageOption.total = res.data.prodListTotal;
                })
        }

        // qing

        // 撤销翻译任务.
        $scope.cancelTask = function (productItem, index) {
            translationService.cancelTask({prodCode :productItem.productCode})
                .then(function (res) {
                    $scope.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                    $scope.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                    $scope.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    productItem.tranSts = 0;
                })
        };

        // 清空查询条件.
        $scope.clearConditions = function () {
            $scope.vm.getTaskInfo = {
                distributeRule: 1,
                distributeCount: 10,
                sortCondition: "",
                sortRule: ""
            };
            $scope.vm.searchInfo = {};
        };

    }

    translationDetail.$inject = ['$scope','$routeParams','$translate','translationService','notify','confirm','alert'];
    return translationDetail;
});

define([
    'cms',
    'modules/cms/controller/popup.ctl'
]),function () {
    cms.controller('aa',(function(){
        function aa(){};
        
        aa.prototype =function () {

        };
        
        return aa;
    })())
}
