/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope) {

        $scope.vm = {
            "searchInfo": {},
            "masterData": {},
            "groupPageOption": {curr: 1, total: 0, size: 30, fetch: getGroupList},
            "productPageOption": {curr: 1, total: 250, size: 20, fetch: getProductList},
            "groupList": [],
            "productList": [1]
        };

        $scope.clear = clear;
        $scope.search = search;
        $scope.export = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;

        function clear () {
            $scope.vm.searchInfo = {};
        }

        function search () {

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
