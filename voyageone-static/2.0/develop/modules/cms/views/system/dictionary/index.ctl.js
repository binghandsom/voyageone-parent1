/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function dictionaryIndex($scope, notify, $routeParams, $translate, $dictionaryService) {

        $scope.vm = {
            searchInfo: { cartId: null },
            dictionaryPageOption: {curr: 1, total: 0, size: 20, fetch: search},
            dictionaryList: []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.delDictItem = delDictItem;

        /**
         * 初始化数据.
         */
        function initialize () {
            $dictionaryService.init()
                .then(function (res) {
                    $scope.vm.masterData = res.data;
                    search();
                });
        }

        /**
         * 清空画面上显示的数据
         */
        function clear () {
            $scope.vm.searchInfo = { cartId: null };
        }

        /**
         * 检索
         */
        function search () {
            var data = angular.copy($scope.vm.searchInfo);
            data.offset = ($scope.vm.dictionaryPageOption.curr - 1) * $scope.vm.dictionaryPageOption.size;
            data.rows = $scope.vm.dictionaryPageOption.size;
            $dictionaryService.dtGetDict(data)
                .then(function (res) {
                    $scope.vm.dictionaryList = res.data.dictionaryList;
                    $scope.vm.dictionaryPageOption.total = res.data.dictionaryListCnt;
                })
        }

        /**
         * 删除dictionary数据
         * @param dictionaryInfo
         */
        function delDictItem (dictionaryInfo) {
            $dictionaryService.delDict(dictionaryInfo)
                .then(function () {
                    notify.success ($translate.instant('TXT_COM_DELETE_SUCCESS'));
                    search();
                })
        }
    }

    dictionaryIndex.$inject = ['$scope', 'notify', '$routeParams', '$translate', '$dictionaryService'];
    return dictionaryIndex;
});