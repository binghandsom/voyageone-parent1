/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/search.service'
], function () {

    return function ($scope, searchIndexService) {

        $scope.vm = {
            "searchInfo": {
                "compareType": null,
                "brand": null,
                "promotion": null
            },
            "groupPageOption": {curr: 1, total: 30, size: 30, fetch: getGroupList},
            "productPageOption": {curr: 1, total: 250, size: 30, fetch: getProductList},
            "groupList": [],
            "productList": []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.export = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;

        /**
         * 初始化数据.
         */
        function initialize () {
            searchIndexService.init().then(function (res) {
                $scope.vm.masterData = res.data;
            })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear () {
            $scope.vm.searchInfo = {
                "compareType": null,
                "brand": null,
                "promotion": null
            };
        }

        /**
         * 检索
         */
        function search () {
            searchIndexService.search($scope.vm.searchInfo)
                .then(function (res) {
                $scope.vm.groupList = res.data.groupList;
                //$scope.vm.groupPageOption.total = res.data.groupListTotal;

                $scope.vm.productList = res.data.productList;
                //$scope.vm.productPageOption.total = res.data.productListTotal;
            })
        }

        /**
         * 数据导出
         */
        function exportFile () {

        }

        /**
         * 分页处理group数据
         */
        function getGroupList () {

            searchIndexService.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption)
            .then(function (res) {
                $scope.vm.groupList = res.data.groupList;
                //$scope.vm.groupPageOption.total = res.data.groupListTotal;
            });
        }

        /**
         * 分页处理product数据
         */
        function getProductList () {

            searchIndexService.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList;
                    //$scope.vm.productPageOption.total = res.data.productListTotal;
                });
        }
    };
});
