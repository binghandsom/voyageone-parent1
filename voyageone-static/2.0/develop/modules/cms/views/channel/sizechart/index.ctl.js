/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function sizeChartController($scope,systemCategoryService,confirm) {
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
            //search();
        };

        $scope.search = search;

        function search() {
            console.log("搜索条件",$scope.vm.searchInfo);
/*            systemCategoryService.getCategoryList({
                "catName":$scope.vm.searchInfo.catName,
                "catId": $scope.vm.searchInfo.catId,
                "skip": ($scope.categoryPageOption.curr - 1) * $scope.categoryPageOption.size,
                "limit": $scope.categoryPageOption.size
            }).then(function (res) {
                $scope.categoryPageOption.total = res.data.total;
                $scope.vm.categoryList = res.data.resultData;
            }, function (err) {

            })*/
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

    sizeChartController.$inject = ['$scope', 'systemCategoryService','confirm'];
    return sizeChartController;
});