/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    return function ($scope, promotionService, taskPriceService, notify, $routeParams) {
        pageSize = 5;
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "searchKey": '',
            "promotion": {},
            "priceList": [],
            "pricePageOption": {curr: 1, total: 5, size: 10, fetch: search}
        };


        $scope.initialize = function () {
            promotionService.getPromotionList({"promotionId": $routeParams.promotionId}).then(function (res) {
                $scope.vm.promotion = res.data[0];
            }, function (err) {

            });

            search();
        }

        function search() {
            taskPriceService.getPriceList({
                "promotionId": $routeParams.promotionId,
                "taskType":0,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.pricePageOption.curr - 1) * $scope.vm.pricePageOption.size,
                "length": $scope.vm.pricePageOption.size
            }).then(function (res) {
                $scope.vm.pricePageOption.total = res.data.total;
                $scope.vm.priceList = res.data.resultData;
            }, function (err) {

            })
        }
    };
});
