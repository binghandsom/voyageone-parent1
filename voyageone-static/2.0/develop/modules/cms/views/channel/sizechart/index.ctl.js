/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function sizeChartController($scope,sizeChartService,confirm) {
        $scope.vm = {
            sizeChartList: [],
            searchInfo : {sizeChartName: "", finishFlag:"",brandNameList:[],productTypeList:[],startTime:"",endTime:"",sizeTypeList:[]},
            finishFlag:[{name:'',value:'all'},{name:'yes',value:'1'},{name:'no',value:'0'}],
            brandNameList:[],
            productTypeList:[],
            sizeTypeList:[],
            sizeChartPageOption : {curr: 1, total: 198, size: 30, fetch: search}
        };

        $scope.initialize  = function () {
            sizeChartService.init().then(function(resp){
               $scope.vm.sizeChartList = resp.data.sizeChartList;
               $scope.vm.brandNameList = resp.data.brandNameList;
               $scope.vm.productTypeList = resp.data.productTypeList;
               $scope.vm.sizeTypeList = resp.data.sizeTypeList;
            });
        };

        $scope.search = search;

        function search() {
            sizeChartService.search($scope.vm.searchInfo).then(function(reps){
                console.log("搜索结果",reps);
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
        $scope.deleteRow = function(){
            confirm("确认要删除该尺码表吗？").result.then(function () {
                alert("yes");
            });
        }

    }

    sizeChartController.$inject = ['$scope', 'sizeChartService'];
    return sizeChartController;
});