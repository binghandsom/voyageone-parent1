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
            code: data.code,
            type: data.type,
            pageOption: {curr: 1, total: 1, size: 10, fetch: getPriceList},
            priceList: [],
            skuList: [],
            sku: null,
            isFirstSku:true
        };

        $scope.initialize = function () {
            getPriceList();
        };

        function getPriceList() {
        var data = {
            code: $scope.vm.code,
            sku: $scope.vm.sku,
            isFirstSku:$scope.vm.isFirstSku,
            flag: $scope.vm.type,
            offset: ($scope.vm.pageOption.curr - 1) * $scope.vm.pageOption.size,
            rows: $scope.vm.pageOption.size
        };

        $priceHistoryService.getPriceHistory(data).then(function (res) {
            $scope.vm.priceList = res.data.list;
            $scope.vm.pageOption.total = res.data.total;
            if ($scope.vm.isFirstSku) {
                $scope.vm.skuList = res.data.skuList;
                $scope.vm.sku = res.data.skuList[0].skuCode;
                $scope.vm.isFirstSku=false;
            }
        });
        }
    });
});