/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/datePicker.ctl',
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope) {

        $scope.vm = {"searchInfo": {}, "masterData": {}};

        $scope.clear = clear;
        $scope.search = search;
        $scope.export = exportFile;

        function clear () {
            $scope.vm.searchInfo = {};
        }

        function search () {

        }

        function exportFile () {

        }
    };
});
