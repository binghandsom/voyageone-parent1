/**
 * Created by linanbin on 15/12/7.
 */

define([], function () {

    function errorListController($scope, notify, $routeParams, $translate, $errorListService) {

        $scope.vm = {
            searchInfo: {
                cartId: null,
                catId: null,
                errType: null
            },
            type: $routeParams.type, // TODO 暂时无法区分权限,所以这个字段以后备用
            errorPageOption: {curr: 1, total: 0, fetch: search},
            errorList: []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.updateFinishStatus = updateFinishStatus;

        /**
         * 初始化数据.
         */
        function initialize() {
            $errorListService.init()
                .then(function (res) {
                    $scope.vm.masterData = res.data;
                    search();
                });
        }

        /**
         * 清空画面上显示的数据
         */
        function clear() {
            $scope.vm.searchInfo = {
                cartId: null,
                catId: null,
                errType: null
            };
        }

        /**
         * 检索
         */
        function search() {
            var data = angular.copy($scope.vm.searchInfo);
            if ($scope.vm.searchInfo.codes)
                data.codes = $scope.vm.searchInfo.codes.split("\n");
            data.offset = ($scope.vm.errorPageOption.curr - 1) * $scope.vm.errorPageOption.size;
            data.rows = $scope.vm.errorPageOption.size;
            $errorListService.search(data)
                .then(function (res) {
                    $scope.vm.errorList = res.data.errorList;
                    $scope.vm.errorPageOption.total = res.data.errorCnt;
                })
        }

        function updateFinishStatus(id) {
            $errorListService.updateFinishStatus({seq: id})
                .then(function () {
                    notify.success ($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    search();
                })
        }
    }

    errorListController.$inject = ['$scope', 'notify', '$routeParams', '$translate', '$errorListService'];
    return errorListController;
});