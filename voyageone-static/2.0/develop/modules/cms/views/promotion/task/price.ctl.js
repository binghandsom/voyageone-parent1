/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function priceController($scope, promotionService, taskPriceService, notify, $routeParams) {
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
        $scope.search=search;
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

        $scope.updateStatus = function(item,synFlg){
            var data = _.clone(item);
            data.synFlg = synFlg;
            data.errMsg = "";
            taskPriceService.updateTaskStatus(data).then(function (res) {
                item.synFlg = synFlg;
                item.errMsg = "";
            }, function (err) {
                notify.warning("fail")
            });
        }
        
        $scope.updateAllStatus = function (synFlg) {
            taskPriceService.updateTaskStatus({"promotionId": $routeParams.promotionId,"synFlg":synFlg,"errMsg":"","taskType":0}).then(function (res) {
                _.each($scope.vm.priceList,function(item){
                    item.synFlg = synFlg;
                    item.errMsg = "";
                })
            }, function (err) {
                notify.warning("fail")
            });
        }
        
        
        
        
    };

    priceController.$inject = ['$scope', 'promotionService', 'taskPriceService', 'notify', '$routeParams'];
    return priceController;
});
