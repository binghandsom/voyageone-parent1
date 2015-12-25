/**
 * Created by linanbin on 15/12/7.
 * Modified by gubuchun on 15/12/21.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPromotionHistoryCtl', function ($scope, $promotionHistoryService, data) {

        $scope.vm = {
            data: {'productCode': '100000', 'offset': 0, 'rows': 5},
            groupPageOption: {curr: 1, total: 1, size: 5, fetch: getPromotionList},
            promotionList: []
        };

        $scope.initialize = function () {
            //$scope.vm.data = data;
            $promotionHistoryService.getPromotionHistory($scope.vm.data).then(function (res) {
                $scope.vm.promotionList = res.data.list;
                $scope.vm.groupPageOption.total = res.data.total;
            });
        };

        function getPromotionList(page) {
            $scope.vm.data.offset = (page - 1) * ($scope.vm.data.rows);
            $promotionHistoryService.getPromotionHistory($scope.vm.data).then(function (res) {
                $scope.vm.promotionList = res.data.list;
                $scope.vm.groupPageOption.curr = page;
            });
        }
    });
});