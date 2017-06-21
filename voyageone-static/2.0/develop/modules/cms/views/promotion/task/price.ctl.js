/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function priceController($scope, promotionService, taskPriceService, notify, $routeParams,confirm,$translate) {
        pageSize = 5;
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "searchKey": '',
            "promotion": {},
            "priceList": [],
            "pricePageOption": {curr: 1, total: 5, size: 10, fetch: search},
            "statusCnt":{},
            "searchInfo":{}
        };


        $scope.initialize = function () {
            promotionService.getPromotionList({"promotionId": $routeParams.promotionId}).then(function (res) {
                $scope.vm.promotion = res.data[0];
                // if ($scope.vm.promotion.triggerTime) $scope.vm.promotion.triggerTime = new Date($scope.vm.promotion.triggerTime);
            }, function (err) {

            });

            search();
        }
        $scope.getMinMaxPrice = function (minPrice,maxPrice) {
            if (maxPrice== minPrice)
                return maxPrice.toFixed(2);

            return minPrice.toFixed(2) + "~" +maxPrice.toFixed(2);
        };
        $scope.search=search;
        function search() {
            taskPriceService.getPriceList({
                "promotionId": $routeParams.promotionId,
                "taskType":0,
                "key": $scope.vm.searchKey,
                "synFlg": $scope.vm.searchInfo.synFlg,
                "start": ($scope.vm.pricePageOption.curr - 1) * $scope.vm.pricePageOption.size,
                "length": $scope.vm.pricePageOption.size
            }).then(function (res) {
                $scope.vm.pricePageOption.total = res.data.total;
                $scope.vm.priceList = res.data.resultData;
                $scope.vm.statusCnt.failCnt = res.data.failCnt;
                $scope.vm.statusCnt.pendingCnt = res.data.pendingCnt;
                $scope.vm.statusCnt.stopCnt = res.data.stopCnt;
                $scope.vm.isAllPromotion = res.data.isAllPromotion;
            }, function (err) {

            })
        }

        $scope.searchStatus = function(status){
            $scope.vm.searchInfo.synFlg = status;
            $scope.vm.pricePageOption.curr = 1;
            search();
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
            confirm($translate.instant('TXT_MSG_STARTUP_ITEM'))
                .then(function () {
                    taskPriceService.updateTaskStatus({"promotionId": $routeParams.promotionId,"synFlg":synFlg,"errMsg":"","taskType":0}).then(function (res) {
                        _.each($scope.vm.priceList,function(item){
                            item.synFlg = synFlg;
                            item.errMsg = "";
                        })
                    }, function (err) {
                        notify.warning("fail")
                    })
                })
        }

        $scope.updateAllFailStatus = function () {
            confirm($translate.instant('TXT_MSG_STARTUP_ITEM'))
                .then(function () {
                    taskPriceService.updateTaskStatus({"promotionId": $routeParams.promotionId,"synFlg":1,"errMsg":"","taskType":0,"selSynFlg":3}).then(function (res) {
                        _.each($scope.vm.priceList,function(item){
                            item.synFlg = 1;
                            item.errMsg = "";
                        })
                    }, function (err) {
                        notify.warning("fail")
                    })
                })
        };

        $scope.delAllPromotionByCustomPromotionId = function () {
            confirm("是否从全店特价宝删除一下商品")
                .then(function () {
                    taskPriceService.delAllPromotionByCustomPromotionId($routeParams.promotionId).then(function (res) {
                        notify.success("成功")
                    }, function (err) {
                        notify.warning("fail")
                    })
                })

        };

        $scope.refreshAllPromotionByCustomPromotionId = function () {
            confirm("是否从全店特价宝回复一下商品")
                .then(function () {
                    taskPriceService.refreshAllPromotionByCustomPromotionId($routeParams.promotionId).then(function (res) {
                        notify.success("成功")
                    }, function (err) {
                        notify.warning("fail")
                    })
                })
        }
        
        
        
    };

    priceController.$inject = ['$scope', 'promotionService', 'taskPriceService', 'notify', '$routeParams', 'confirm','$translate'];
    return priceController;
});
