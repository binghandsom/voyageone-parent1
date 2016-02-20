/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function detailController($scope, promotionService, promotionDetailService, notify, $routeParams, $location, alert, $translate,menuService) {
        pageSize = 5;
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "tabIndex": 0,
            "searchKey": '',
            "promotion": {},
            "groupList": [],
            "codeList": [],
            "skuList": [],
            "cartList":[],
            "groupPageOption": {curr: 1, total: 5, size: 5, fetch: searchGroup},
            "codePageOption": {curr: 1, total: 5, size: 5, fetch: searchCode},
            "skuPageOption": {curr: 1, total: 7, size: 10, fetch: searchSku},
            groupSelList: {
                currPageRows: [],
                selFlag: [],
                selAllFlag: false,
                selList: []
            },
            codeSelList: {
                currPageRows: [],
                selFlag: [],
                selAllFlag: false,
                selList: []
            },
            skuSelList: {
                currPageRows: [],
                selFlag: [],
                selAllFlag: false,
                selList: []
            }
        };


        $scope.initialize = function () {
            $scope.getCategoryType();
            promotionService.getPromotionList({"promotionId": $routeParams.promotionId}).then(function (res) {
                $scope.vm.promotion = res.data[0];
            }, function (err) {

            });
            $scope.search();
        }
        function selAllFlag(objectList,id){
            objectList.selAllFlag = true;
            if(!id){
                id="id";
            }
            angular.forEach(objectList.currPageRows, function(object) {
                if (!objectList.selFlag[object[id]]) {
                    objectList.selAllFlag = false;
                }
            })
        }
        function searchGroup() {
            promotionDetailService.getPromotionGroup({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.groupPageOption.curr - 1) * $scope.vm.groupPageOption.size,
                "length": $scope.vm.groupPageOption.size
            }).then(function (res) {
                $scope.vm.groupPageOption.total = res.data.total;
                $scope.vm.groupList = res.data.resultData;
                $scope.vm.groupSelList.currPageRows = res.data.resultData;
                selAllFlag($scope.vm.groupSelList,"modelId");
            }, function (err) {

            })
        }

        function searchCode() {
            promotionDetailService.getPromotionCode({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.codePageOption.curr - 1) * $scope.vm.codePageOption.size,
                "length": $scope.vm.codePageOption.size
            }).then(function (res) {
                $scope.vm.codePageOption.total = res.data.total;
                $scope.vm.codeList = res.data.resultData;
                _.each($scope.vm.codeList,function(item){
                    item.promotionPriceBak = item.promotionPrice;
                });
                $scope.vm.codeSelList.currPageRows = res.data.resultData;
                selAllFlag($scope.vm.codeSelList,"productCode");
            }, function (err) {

            })
        }

        function searchSku() {
            promotionDetailService.getPromotionSku({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.skuPageOption.curr - 1) * $scope.vm.skuPageOption.size,
                "length": $scope.vm.skuPageOption.size
            }).then(function (res) {
                $scope.vm.skuPageOption.total = res.data.total;
                $scope.vm.skuList = res.data.resultData;
            }, function (err) {

            })
        }
        $scope.getCategoryType = function() {
            menuService.getCategoryType().then(function(res){
                $scope.vm.cartList = res;
            })
        }
        $scope.search = function () {
            searchGroup();
            searchCode();
            searchSku();
        };

        $scope.updateCode = function(code){
            promotionDetailService.updatePromotionProduct(code).then(function (res) {
                code.promotionPriceBak = code.promotionPrice;
                notify.success("success");
            }, function (err) {
                notify.warning("fail");
            })
        };

        $scope.delPromotionModel = function(group){
            var data = [];
            data.push(group);
            promotionDetailService.delPromotionModel(data).then(function (res) {
                notify.success("success");
                $scope.search();
            }, function (err) {
                notify.warning("fail");
            })
        };

        $scope.delPromotionCode = function(code){
            var parameter = [];
            parameter.push(code);
            promotionDetailService.delPromotionCode(parameter).then(function (res) {
                notify.success("success");
                $scope.search();
            }, function (err) {
                notify.warning("fail");
            })
        };

        $scope.delSelPromotion = function(){
            if($scope.vm.tabIndex == 0){
                if($scope.vm.groupSelList.selList.length>0){
                    promotionDetailService.delPromotionModel($scope.vm.groupSelList.selList).then(function (res) {
                        notify.success("success");
                        $scope.search();
                    }, function (err) {
                        notify.warning("fail");
                    })
                } else {
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            }else if($scope.vm.tabIndex == 1){
                if($scope.vm.codeSelList.selList.length>0) {
                    promotionDetailService.delPromotionCode($scope.vm.codeSelList.selList).then(function (res) {
                        notify.success("success");
                        $scope.search();
                    }, function (err) {
                        notify.warning("fail");
                    })
                } else {
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            }

        };
        $scope.teJiaBaoInit = function(){
            promotionDetailService.teJiaBaoInit( $routeParams.promotionId).then(function (res) {
                notify.success("success");
                $location.path("/promotion/task/price/"+$routeParams.promotionId);
                //href="#/promotion/task/price" target="_blank"-->
            }, function (err) {
                notify.warning("fail");
            })
        }
    };
    detailController.$inject = ['$scope', 'promotionService', 'promotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','menuService'];
    return detailController;
});
