/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'modules/cms/service/search.service'
], function () {

    return function ($scope, $rootScope, $routeParams, searchIndexService) {

        $scope.vm = {
            searchInfo: {
                compareType: null,
                brand: null,
                promotion: null
            },
            groupPageOption: {curr: 1, total: 0, size: 20, fetch: getGroupList},
            productPageOption: {curr: 1, total: 0, size: 20, fetch: getProductList},
            groupList: [],
            productList: [],
            groupSelList: {
                currPageRows: [],
                selFlag: [],
                selAllFlag: false,
                selList: []
            },
            productSelList: {
                currPageRows: [],
                selFlag: [],
                selAllFlag: false,
                selList: []
            },
            currTab: "group",
            status: {
                open: true
            }
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
            // 如果来至category 或者 header的检索,将初始化检索条件
            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.catId = $routeParams.value;
            } else if ($routeParams.type == "2") {
                $scope.vm.searchInfo.codeList = $routeParams.value;
            }
            searchIndexService.init().then(function (res) {
                $scope.vm.masterData = res.data;
            })
            .then(function() {
                // 如果来至category 或者header search 则默认检索
                if ($routeParams.type == "1"
                    || $routeParams.type == "2")
                search();
            })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear () {
            $scope.vm.searchInfo = {
                compareType: null,
                brand: null,
                promotion: null
            };
        }

        /**
         * 检索
         */
        function search () {
            searchIndexService.search($scope.vm.searchInfo, $scope.vm.groupPageOption, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.groupList = res.data.groupList;
                    $scope.vm.groupPageOption.total = res.data.groupListTotal;
                    $scope.vm.groupSelList.currPageRows = res.data.groupCurrPageRows;
                    $scope.vm.groupSelList.selFlag = res.data.groupSelFlag;

                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList.currPageRows = res.data.productCurrPageRows;
                    $scope.vm.productSelList.selFlag = res.data.productSelFlag;
            })
        }

        /**
         * 数据导出
         */
        // TODO
        function exportFile () {

        }

        /**
         * 分页处理group数据
         */
        function getGroupList () {

            searchIndexService.getGroupList($scope.vm.searchInfo, $scope.vm.groupPageOption)
            .then(function (res) {
                $scope.vm.groupList = res.data.groupList;
                $scope.vm.groupPageOption.total = res.data.groupListTotal;
                $scope.vm.groupSelList.currPageRows = res.data.groupCurrPageRows;
                $scope.vm.groupSelList.selFlag = res.data.groupSelFlag;
            });
        }

        /**
         * 分页处理product数据
         */
        function getProductList () {

            searchIndexService.getProductList($scope.vm.searchInfo, $scope.vm.productPageOption)
                .then(function (res) {
                    $scope.vm.productList = res.data.productList;
                    $scope.vm.productPageOption.total = res.data.productListTotal;
                    $scope.vm.productSelList.currPageRows = res.data.productCurrPageRows;
                    $scope.vm.productSelList.selFlag = res.data.productSelFlag;
                });
        }
    };
});
