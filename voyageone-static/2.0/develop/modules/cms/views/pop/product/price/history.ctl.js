/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/21.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPriceHistoryCtl', function ($scope, $priceHistoryService, data) {

        $scope.vm = {
            data: {'flag': true, 'code': '0', 'offset': 0, 'rows': 10, 'sku': '0'},
            groupPageOption: {curr: 1, total: 1, size: 10, fetch: getPriceList},
            priceList: []
        };

        $scope.initialize = function () {
            $priceHistoryService.getPriceHistory($scope.vm.data).then(function (res) {
                $scope.vm.priceList = res.data.list;
                $scope.vm.groupPageOption.total = res.data.total;
            });
        };

        function getPriceList(page) {
            $scope.vm.data.offset = (page - 1) * ($scope.vm.data.rows);
            $priceHistoryService.getPriceHistory($scope.vm.data).then(function (res) {
                $scope.vm.priceList = res.data.list;
                $scope.vm.groupPageOption.curr = page;
            });
        }
    });
});