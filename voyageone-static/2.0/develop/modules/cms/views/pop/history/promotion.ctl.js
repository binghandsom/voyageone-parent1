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
            code: data,
            pageOption: {curr: 1, total: 0, size: 10, fetch: getPromotionList},
            promotionList: [],
            cartId: '',
            cartList: []
        };

        $scope.initialize = function () {
            getPromotionList();
        };

        function getPromotionList() {
            var data = {
                code: $scope.vm.code,
                cartId: $scope.vm.cartId,
                offset: ($scope.vm.pageOption.curr - 1) * $scope.vm.pageOption.size,
                rows: $scope.vm.pageOption.size
            };
            $promotionHistoryService.getPromotionHistory(data).then(function (res) {
                $scope.vm.promotionList = res.data.list;
                $scope.vm.pageOption.total = res.data.total;
                $scope.vm.cartList = res.data.cartList
            });
        }
    });
});