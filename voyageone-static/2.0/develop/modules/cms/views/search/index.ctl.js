/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/search.service'
], function () {

    return function ($scope, searchIndexService) {

        $scope.vm = {
            "searchInfo": {},
            "masterData": {},
            "groupPageOption": {curr: 1, total: 0, size: 30, fetch: getGroupList},
            "productPageOption": {curr: 1, total: 250, size: 20, fetch: getProductList},
            "groupList": [],
            "productList": [1]
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.export = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;

        function initialize () {
            searchIndexService.init().then(function (res) {
                $scope.vm.masterData = res.data;
            })
        }

        function clear () {
            $scope.vm.searchInfo = {};
        }

        function search () {
            searchIndexService.search().then(function (res) {
                $scope.vm.groupList = res.data.groupList;
                $scope.vm.groupPageOption.total = res.data.groupList.length;
                $scope.vm.productList = res.data.productList;
                $scope.vm.productPageOption.total = res.data.productList.length;
            })
        }

        function exportFile () {

        }

        function getGroupList () {

            console.log($scope.vm.groupPageOption);
        }

        function getProductList () {

        }
    };
});
