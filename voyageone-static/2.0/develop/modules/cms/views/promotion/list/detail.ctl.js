/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'underscore'
], function () {

    function detailController($scope, promotionService, promotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory,cookieService) {
        $scope.promotionOld={};
        $scope.datePicker = [];
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "tabIndex": 0,
            "searchKey": '',
            "promotion": {},
            "cartList":[],
            "groupPageOption": {curr: 1, total: 0, fetch: searchGroup},
            "codePageOption": {curr: 1, total: 0, fetch: searchCode},
            "skuPageOption": {curr: 1, total: 0, fetch: searchSku},
            "groupList": [],
            "codeList": [],
            "skuList": [],
            groupSelList: { selList: []},
            codeSelList: { selList: []},
            skuSelList: { selList: []},
            tagList:[],
            platformUrl: ''
        };
        $scope.currentChannelId = cookieService.channel();

        var tempGroupSelect = new selectRowsFactory();
        var tempProductSelect = new selectRowsFactory();

        $scope.initialize = function () {
            promotionService.init().then(function (res) {
                $scope.vm.platformTypeList = res.data.platformTypeList;
                $scope.vm.promotionStatus = res.data.promotionStatus;
                promotionService.initByPromotionId($routeParams.promotionId).then(function(res){
                    $scope.vm.tagList=res.data.tagList;
                    $scope.vm.platformUrl = res.data.platformUrl;
                });
                promotionService.getPromotionList({"promotionId": $routeParams.promotionId}).then(function (res) {
                    $scope.vm.promotion = res.data[0];
                    $scope.promotionOld = _.clone($scope.vm.promotion);
                    if($scope.vm.promotion.tejiabaoId != "0"){
                        $scope.vm.promotion.tejiabao=true;
                    }
                });
            });
            $scope.search();
        };

        $scope.search = function () {

            // 重新初始化选中标签
            tempGroupSelect = new selectRowsFactory();
            tempProductSelect = new selectRowsFactory();
            searchGroup();
            searchCode();
            //searchSku();
        };
        //处理tag sunpt
        $scope.codeTagChange=function(code) {
            var vm = $scope.vm;
            var tag = _.find(vm.tagList, function (num) {
                return num.id === code.tagId;
            });
            if(tag) {
                code.tag = tag.tagName;
                code.tagPathName = tag.tagPathName;
                code.tagPath =tag.tagPath;
                code.isUpdate = true;
            }
            console.log(code);
            console.log(tag);
        }
        $scope.updateCode = function(code){
            delete code.isUpdate;
            console.log("updateCode"+code);
            promotionDetailService.updatePromotionProduct(code).then(function (res) {
                code.promotionPriceBak = code.promotionPrice;
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            })
        };

        $scope.delPromotionModel = function(group){
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var data = [];
                    data.push(group);
                    promotionDetailService.delPromotionModel(data).then(function (res) {
                        notify.success($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                        $scope.search();
                        $scope.vm.groupSelList.selList = [];
                    })
                });
        };

        $scope.delPromotionCode = function(code){
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var parameter = [];
                    parameter.push(code);
                    promotionDetailService.delPromotionCode(parameter).then(function () {
                        notify.success($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                        $scope.search();
                        //$scope.vm.codeSelList.selList = [];
                    });
                });
        };

        $scope.delSelPromotion = function(){
            if($scope.vm.tabIndex == 0){
                if($scope.vm.groupSelList.selList.length>0){
                    confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                        .then(function () {
                            var parameter = [];
                            _.forEach($scope.vm.groupSelList.selList, function (object) {
                                parameter.push(object.data);
                            });
                            promotionDetailService.delPromotionModel(parameter).then(function () {
                                notify.success($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                                $scope.search();
                                //$scope.vm.groupSelList.selList = [];
                            })
                        });
                } else {
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            } else if($scope.vm.tabIndex == 1){
                if($scope.vm.codeSelList.selList.length>0) {
                    confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                        .then(function () {
                            var parameter = [];
                            _.forEach($scope.vm.codeSelList.selList, function (object) {
                                parameter.push(object.data);
                            });
                            promotionDetailService.delPromotionCode(parameter).then(function () {
                                notify.success($translate.instant('TXT_MSG_DELETE_SUCCESS'));
                                $scope.search();
                                //$scope.vm.codeSelList.selList = [];
                            });
                        });
                } else {
                    alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                }
            }

        };
        $scope.savePromotionInfo = function(){

            if(!$scope.vm.promotion.tejiabao){
                $scope.vm.promotion.tejiabaoId = "0";
            }
            promotionService.updatePromotion($scope.vm.promotion).then(function (res) {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            })
        };
        $scope.teJiaBaoInit = function(){
            if($scope.vm.codeList.length>0) {
                promotionDetailService.teJiaBaoInit( $routeParams.promotionId).then(function (res) {
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $location.path(cRoutes.promotion_task_price.url + $routeParams.promotionId);
                })
            } else {
                alert($translate.instant('TXT_MSG_NO_PRODUCT_ROWS'));
            }
        };
        $scope.cannelPromotionInfo =function(){
            $scope.vm.promotion = _.clone($scope.promotionOld);
            if($scope.vm.promotion.tejiabaoId != "0"){
                $scope.vm.promotion.tejiabao=true;
            }
        };
        $scope.compare = function(data1,data2){
            return _.isEqual(data1, data2)
        };


        //function selAllFlag(objectList,id){
        //    objectList.selAllFlag = true;
        //    if(!id){
        //        id="id";
        //    }
        //    angular.forEach(objectList.currPageRows, function(object) {
        //        if (!objectList.selFlag[object[id]]) {
        //            objectList.selAllFlag = false;
        //        }
        //    })
        //}
        function searchGroup() {
            promotionDetailService.getPromotionGroup({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.groupPageOption.curr - 1) * $scope.vm.groupPageOption.size,
                "length": $scope.vm.groupPageOption.size
            }).then(function (res) {
                tempGroupSelect.clearCurrPageRows();

                $scope.vm.groupPageOption.total = res.data.total;
                $scope.vm.groupList = res.data.resultData == null ? [] : res.data.resultData;
                _.forEach(res.data.resultData, function(item) {
                    // 初始化数据选中需要的数组
                    tempGroupSelect.currPageRows({"id": item.productModel, data:item});
                });
                $scope.vm.groupSelList = tempGroupSelect.selectRowsInfo;
                //selAllFlag($scope.vm.groupSelList,"modelId");
            })
        }

        function searchCode() {
            promotionDetailService.getPromotionCode({
                "promotionId": $routeParams.promotionId,
                "key": $scope.vm.searchKey,
                "start": ($scope.vm.codePageOption.curr - 1) * $scope.vm.codePageOption.size,
                "length": $scope.vm.codePageOption.size
            }).then(function (res) {
                tempProductSelect.clearCurrPageRows();
                $scope.vm.codePageOption.total = res.data.total;
                $scope.vm.codeList = res.data.resultData == null ? [] : res.data.resultData;
                _.each($scope.vm.codeList,function(item){
                    item.promotionPriceBak = item.promotionPrice;
                    // 初始化数据选中需要的数组
                    tempProductSelect.currPageRows({id: item.productCode, data:item});
                });
                $scope.vm.codeSelList = tempProductSelect.selectRowsInfo;
                //$scope.vm.codeSelList.currPageRows = res.data.resultData;
                //selAllFlag($scope.vm.codeSelList,"productCode");
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
                $scope.vm.skuList = res.data.resultData == null ? [] : res.data.resultData;
            })
        }
    };
    detailController.$inject = ['$scope', 'promotionService', 'promotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory', 'cookieService'];
    return detailController;
});
