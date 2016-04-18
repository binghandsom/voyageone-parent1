/**
 * Created by 123 on 2016/4/13.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function indexController($scope,systemCategoryService) {
        $scope.vm = {categoryList: [], searchInfo : {catName: "", catId: ""}};
        $scope.categoryPageOption = {curr: 1, total: 198, size: 30, fetch: search};

        $scope.initialize  = function () {
            search();
        };

        $scope.search = search;

        $scope.clear = function () {
            $scope.vm.searchInfo = {catName: "", catId: ""};
        };

        function search() {
            systemCategoryService.getCategoryList({
                "catName":$scope.vm.searchInfo.catName,
                "catId": $scope.vm.searchInfo.catId,
                "skip": ($scope.categoryPageOption.curr - 1) * $scope.categoryPageOption.size,
                "limit": $scope.categoryPageOption.size
            }).then(function (res) {
                $scope.categoryPageOption.total = res.data.total;
                $scope.vm.categoryList = res.data.resultData;
            }, function (err) {

            })
        }
    }

    indexController.$inject = ['$scope', 'systemCategoryService'];
    return indexController;
});