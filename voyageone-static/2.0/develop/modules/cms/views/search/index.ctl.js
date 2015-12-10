/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope) {

        $scope.vm = {"searchInfo": {}, "masterData": {}};

        $scope.clear = clear;
        $scope.search = search;
        $scope.export = exportFile;
        $scope.getGroupList = getGroupList;
        $scope.getProductList = getProductList;
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: getGroupList};
        $scope.productPageOption = {curr: 1, total: 250, size: 20, fetch: getProductList};

        function clear () {
            $scope.vm.searchInfo = {};
        }

        function search () {

        }

        function exportFile () {

        }

        function getGroupList () {

            console.log($scope.groupPageOption);
        }

        function getProductList () {

        }
    };
});
