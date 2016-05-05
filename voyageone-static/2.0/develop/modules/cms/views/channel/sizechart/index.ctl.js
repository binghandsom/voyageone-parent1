/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function sizeChartController($scope,systemCategoryService) {
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

        $scope.clear = function () {
            $scope.vm.searchInfo = {catName: "", catId: ""};
        };

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
    }

    sizeChartController.$inject = ['$scope', 'systemCategoryService'];
    return sizeChartController;
});