/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl',
    'underscore'
], function () {

    function detailController($scope, promotionService, promotionDetailService, notify, $routeParams, $location, alert, $translate, confirm, cRoutes, selectRowsFactory,cookieService,cActions) {
        $scope.promotionOld={};
        $scope.datePicker = [];
        $scope.vm = {
            "promotionId": $routeParams.promotionId,
            "tabIndex": 0,
            "searchKey": '',
            "promotion": {},
            "cartList":[],
            "groupPageOption": {curr: 1, total: 0, size:10,fetch: searchGroup},
            "codePageOption": {curr: 1, total: 0, size:10, fetch: searchCode},
            "skuPageOption": {curr: 1, total: 0, size:10, fetch: searchSku},
            "groupList": [],
            "codeList": [],
            "skuList": [],
            groupSelList: { selList: []},
            codeSelList: { selList: []},
            skuSelList: { selList: []},
            tagList:[],
            platformUrl: '',
            "exportPageOption":{curr:1, total:0, size:10, fetch:searchExport},
            "exportList":[]
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
        $scope.getMinMaxPrice = function (minPrice,maxPrice) {
            if (maxPrice== minPrice)
                return maxPrice.toFixed(2);

            return minPrice.toFixed(2) + "~" +maxPrice.toFixed(2);
        };
        $scope.search = function () {

            // 重新初始化选中标签
            tempGroupSelect = new selectRowsFactory();
            tempProductSelect = new selectRowsFactory();
            searchGroup();
            searchCode();
            //searchSku();
            searchExport();
        };
        //处理tag sunpt
        $scope.codeTagChange=function(m,oldTagNameList) {

            //获取删除的tag
            var delTagNameList = _.filter(oldTagNameList, function (tagName) {
                return !_.contains(m.tagNameList, tagName);
            });
            //获取新增的tag
            var addTagNameList = _.filter(m.tagNameList, function (tagName) {
                return !_.contains(oldTagNameList, tagName);
            });

            // console.log("delTagNameList")
            // console.log(delTagNameList)
            // console.log("addTagNameList")
            // console.log(addTagNameList)

            var productTagList = [];
            _.each(delTagNameList, function (tagName) {
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName, checked: 0});
            });
            _.each(addTagNameList, function (tagName) {
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName, checked: 2});
            });

            //console.log(productTagList);

            var parameter = {};
            parameter.tagList = productTagList;
            parameter.id = m.id;
            //console.log(parameter);
            promotionDetailService.updatePromotionProductTag(parameter).then(function (res) {
                //   alert($translate.instant('TXT_SUCCESS'));
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });

        };

        $scope.updateCode = function(code){
            delete code.isUpdate;
            promotionDetailService.updatePromotionProduct(code).then(function (res) {
                code.promotionPriceBak = code.promotionPrice;
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            })
        };

        $scope.delPromotionModel = function(group){
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
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
            confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
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
            if($scope.vm.tabIndex == 1){
                if($scope.vm.groupSelList.selList.length>0){
                    confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
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
            } else if($scope.vm.tabIndex == 0){
                if($scope.vm.codeSelList.selList.length>0) {
                    confirm($translate.instant('TXT_MSG_DELETE_ITEM'))
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

        $scope.openOtherDownload = function (promotion) {
            promotionService.exportPromotion({"promotionId":promotion.id, "templateType":0}).then(function (resp) {
                notify("OK", "success");
            });

            // $.download.post(cActions.cms.promotion.promotionService.root + "/" + cActions.cms.promotion.promotionService.exportPromotion, {"promotionId": promotion.id,"promotionName":promotion.promotionName});
        };
        $scope.openJuhuasuanDownload = function (promotion) {
            promotionService.exportPromotion({"promotionId":promotion.id, "templateType":1}).then(function (resp) {
                notify("OK", "success");
            });
            // $.download.post(cActions.cms.promotion.promotionDetailService.root + "/" + cActions.cms.promotion.promotionDetailService.tmallJuhuasuanExport, {"promotionId": promotion.id,"promotionName":promotion.promotionName});
        };
        $scope.openTmallDownload = function (promotion) {
            promotionService.exportPromotion({"promotionId":promotion.id, "templateType":2}).then(function (resp) {
                notify("OK", "success");
            });
            // $.download.post(cActions.cms.promotion.promotionDetailService.root + "/" + cActions.cms.promotion.promotionDetailService.tmallPromotionExport, {"promotionId": promotion.id,"promotionName":promotion.promotionName});
        };

        function searchExport() {
            promotionService.getPromotionExportTask({
                  "promotionId": $routeParams.promotionId,
                  "pageNum": $scope.vm.exportPageOption.curr,
                  "pageSize": $scope.vm.exportPageOption.size
             }).then(function (res) {
                $scope.vm.exportPageOption.total = res.data.exportTotal;
                $scope.vm.exportList = res.data.exportList == null ? [] : res.data.exportList;
            })
        }

        $scope.downloadExportExcel = function (promotion) {
            $.download.post(cActions.cms.promotion.promotionService.root + "/" + cActions.cms.promotion.promotionService.downloadPromotionExport, {"promotionExportTaskId": promotion.id});
        };

        /**
         * 导出任务列表查询
         * @param page
         */
        $scope.exportSearch = function(page) {
            $scope.vm.exportPageOption.curr = !page ? $scope.vm.exportPageOption.curr : page;

            $searchAdvanceService2.exportSearch({
                                                    "pageNum": $scope.vm.exportPageOption.curr,
                                                    "pageSize": $scope.vm.exportPageOption.size
                                                }).then(function (res) {
                $scope.vm.exportList = res.data.exportList;
                _.each($scope.vm.exportList, function (item) {
                    item.fileName = item.fileName.split(",");
                });
                $scope.vm.exportPageOption.total = res.data.exportListTotal;
            })
        };


        $scope.addPromotionByGroup = function () {
            promotionDetailService.addPromotionByGroup($scope.vm.promotion.id).then(function (res) {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            })
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
    detailController.$inject = ['$scope', 'promotionService', 'promotionDetailService', 'notify', '$routeParams', '$location','alert','$translate','confirm', 'cRoutes', 'selectRowsFactory', 'cookieService','cActions'];
    return detailController;
});
