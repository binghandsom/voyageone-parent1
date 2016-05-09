/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function sizeChartController($scope,sizeChartService,confirm,notify) {
        $scope.vm = {
            sizeChartList: [],
            searchInfo : {sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],startTime:"",endTime:"",sizeTypeList:[]},
            brandNameList:[],
            productTypeList:[],
            sizeTypeList:[],
            sizeChartPageOption : {curr: 1, total: 0, fetch: search},
            search:search
        };

        $scope.initialize  = function () {
            sizeChartService.init().then(function(resp){
               $scope.vm.brandNameList = resp.data.brandNameList;
               $scope.vm.productTypeList = resp.data.productTypeList;
               $scope.vm.sizeTypeList = resp.data.sizeTypeList;
            });
            search();
        };

        $scope.search = search;

        function search() {
            var data = $scope.vm.sizeChartPageOption;
            _.extend(data ,$scope.vm.searchInfo);
            sizeChartService.search(data).then(function(reps){
                $scope.vm.sizeChartList = reps.data.sizeChartList;
                $scope.vm.sizeChartPageOption.total = reps.data.total;
            });
        }

        /**
         * 清空操作
         */
        $scope.clear = function () {
            $scope.vm.searchInfo  = {sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],startTime:"",endTime:"",sizeTypeList:[]};
        };

        /**
         * 删除尺码表操作
         */
        $scope.deleteRow = function(sizeChartId){
            confirm("确认要删除该尺码表吗？").result.then(function () {
                sizeChartService.delete({sizeChartId:sizeChartId}).then(function(reps){
                    notify.success ("删除成功！");
                });
            });
        }

    }

    sizeChartController.$inject = ['$scope', 'sizeChartService','confirm','notify'];
    return sizeChartController;
});