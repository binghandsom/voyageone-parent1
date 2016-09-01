/**
 * Created by tony-piao on 2016/5/5.
 */
define([
    'modules/cms/controller/popup.ctl'
], function () {
    function sizeChartController($scope, sizeChartService, alert, notify, $translate, popups) {
        $scope.vm = {
            sizeChartList: [],
            searchInfo: {
                sizeChartName: "",
                finishFlag: "",
                brandNameList: [],
                productTypeList: [],
                startTime: "",
                endTime: "",
                sizeTypeList: []
            },
            brandNameList: [],
            productTypeList: [],
            sizeTypeList: [],
            sizeChartPageOption: {curr: 1, total: 0, fetch: search},
            search: search
        };

        $scope.initialize = function () {
            sizeChartService.init().then(function (resp) {
                $scope.vm.brandNameList = resp.data.brandNameList;
                $scope.vm.productTypeList = resp.data.productTypeList;
                $scope.vm.sizeTypeList = resp.data.sizeTypeList;
            });
            search();
        };

        $scope.search = function () {
            $scope.vm.sizeChartPageOption.curr = 1;
            search();
        };

        /**
         * 检索
         */
        function search() {
            var data = $scope.vm.sizeChartPageOption;
            _.extend(data, $scope.vm.searchInfo);
            sizeChartService.search(data).then(function (reps) {
                $scope.vm.sizeChartList = reps.data.sizeChartList;
                $scope.vm.sizeChartPageOption.total = reps.data.total;
            });
        }

        /**
         * 清空操作
         */
        $scope.clear = function () {
            $scope.vm.searchInfo = {
                sizeChartName: "",
                finishFlag: "",
                brandNameList: [],
                productTypeList: [],
                startTime: "",
                endTime: "",
                sizeTypeList: []
            };
        };

        /**
         * 删除尺码表操作
         */
        $scope.deleteRow = function (entity) {

            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).then(function () {

                sizeChartService.delete({sizeChartId: entity.sizeChartId}).then(function () {
                    notify.success($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                    search();
                });

            });

        }

    }

    sizeChartController.$inject = ['$scope', 'sizeChartService', 'confirm', 'notify', '$translate', 'popups'];
    return sizeChartController;
});