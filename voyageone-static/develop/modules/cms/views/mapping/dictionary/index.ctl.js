/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function dictionaryIndex($scope, notify, $location, $translate, $dictionaryService, cRoutes, confirm) {

        $scope.vm = {
            searchInfo: { cartId: null },
            dictionaryPageOption: {curr: 1, total: 0, fetch: search},
            dictionaryList: []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.openDictionaryItem = openDictionaryItem;
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
            $dictionaryService.getDictList(data)
                .then(function (res) {
                    $scope.vm.dictionaryList = res.data.dictionaryList;
                    $scope.vm.dictionaryPageOption.total = res.data.dictionaryListCnt;
                })
        }

        /**
         * 跳转到单个字典页面
         * @param id
         */
        function openDictionaryItem (id) {
            $location.path(cRoutes.mapping_dict_item_edit.url + id);
        }

        /**
         * 删除dictionary数据
         * @param dictionaryInfo
         */
        function delDictItem (dictionaryInfo) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    $dictionaryService.delDict(dictionaryInfo)
                        .then(function () {
                            notify.success ($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                            search();
                        })
                });
        }
    }

    dictionaryIndex.$inject = ['$scope', 'notify', '$location', '$translate', '$dictionaryService', 'cRoutes', 'confirm'];
    return dictionaryIndex;
});