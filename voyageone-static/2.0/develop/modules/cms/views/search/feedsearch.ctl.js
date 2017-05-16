/**
 * Created by 123 on 2016/3/31.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl',
    'modules/cms/directives/keyValue.directive'
], function () {
    function searchIndex($scope, $routeParams, $feedSearchService, $translate, $q, selectRowsFactory, confirm, alert, attributeService, cActions, $sessionStorage, $filter, cookieService,popups,systemCategoryService,notify) {
        $scope.status={};
        $scope.vm = {
            searchInfo: {},
            feedPageOption: {curr: 1, total: 0, fetch: search},
            feedList: [],
            feedSelList: {selList: []},
            exportPageOption: {curr: 1, size: 10, total: 0, fetch: exportSearch},
            exportList: [],
            currTab: {group: true, export: false},
            selChannelName:""
        };
        $scope.exportStatus = ["正在生成", "完成", "失败"];
        $scope.beforSearchInfo = {};


        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;

        $scope.exportSearch = exportSearch;
        $scope.doExport = doExport;

        var tempFeedSelect = null;

        /**
         * 初始化数据.
         */
        function initialize() {
            // 默认设置成第一页
            $scope.vm.feedPageOption.curr = 1;

            $scope.vm.exportPageOption.curr = 1;

            $scope.vm.searchInfo.isAll = false;


            // 如果是来自category的检索
            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.category = decodeURIComponent($routeParams.value);
            }
            $feedSearchService.init(" ")
                .then(function (res) {
                    $scope.vm.masterData = res.data;
                })
                .then(function () {
                    exportSearch();
                    if ($routeParams.type == "1" || $sessionStorage.feedSearch) {
                        $scope.vm.searchInfo = $sessionStorage.feedSearch;
                         if($sessionStorage.feedSearch.status!=undefined) {
                             $scope.status[$sessionStorage.feedSearch.status] = true;
                         }
                        search();
                        if ($sessionStorage.feedSearch) delete $sessionStorage.feedSearch;
                    }
                })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear() {
            $scope.vm.searchInfo = {};
            $scope.status={};
            // 默认设置成第一页
            $scope.vm.feedPageOption.curr = 1;
        }

        // 显示feed图片
        $scope.openFeedImagedetail = function (idx) {
            var feedObj = $scope.vm.feedList[idx];
            if (feedObj.hasImgFlg > 0) {
                var picList = [];
                picList[0] = feedObj.image;
                var para = {'mainPic': feedObj.image[0], 'picList': picList, 'hostUrl': 0, 'search': 'feed'};
                return this.openImagedetail(para);
            }
        };

        // 显示feed属性
        $scope.openFeedCodeDetail = function (idx) {
            var feedObj = $scope.vm.feedList[idx];
            if (feedObj.attsList.length == 0) {
                return;
            }
            this.openCodeDetail({'attsList': feedObj.attsList});
        };

        $scope.init = function (selChannel){
            if($scope.vm.masterData.isminimall == 1){
                var temp = _.find($scope.vm.masterData.channelList,function (item) {
                    return item.order_channel_id == selChannel;
                });
                if(temp){
                    $scope.vm.selChannelName = temp.full_name;
                }
            }
            $scope.vm.masterData.channelList
            if(selChannel){
                $feedSearchService.init(selChannel)
                    .then(function (res) {
                        $scope.vm.masterData = res.data;
                    })
            }
        };

        $scope.getChannelName = function (selChannel){

        };

        /**
         * 检索
         */
        function search(page, flg) {
            if (flg === true && tempFeedSelect) {
                // 默认设置成第一页
                $scope.vm.feedPageOption.curr = 1;
                tempFeedSelect = null;
            }
            $scope.vm.searchInfo.status=null;
            _.each($scope.status,function(value,key){
                if(value === true){
                    if(!$scope.vm.searchInfo.status) $scope.vm.searchInfo.status=[];
                    $scope.vm.searchInfo.status.push( +key);
                }
            });
            $scope.vm.feedPageOption.curr = !page ? $scope.vm.feedPageOption.curr : page;
            $scope.vm.searchInfo.pageNum = $scope.vm.feedPageOption.curr;
            $scope.vm.searchInfo.pageSize = $scope.vm.feedPageOption.size;
            $scope.vm.searchInfo.orgChaId =  $scope.vm.orgChaId;
            $feedSearchService.search($scope.vm.searchInfo).then(function (res) {

                $scope.vm.currTab.group = true;
                $scope.vm.currTab.export = false;
                $scope.vm.feedList = res.data.feedList;
                $scope.vm.feedPageOption.total = res.data.feedListTotal;

                $scope.beforSearchInfo = angular.copy($scope.vm.searchInfo);

                if (tempFeedSelect == null) {
                    tempFeedSelect = new selectRowsFactory();
                } else {
                    tempFeedSelect.clearCurrPageRows();
                }
                _.forEach($scope.vm.feedList, function (feedInfo) {
                    // 统计sku数
                    var skusList = feedInfo.skus;
                    feedInfo._popSkuInfo = [];
                    if (skusList == undefined) {
                        feedInfo.skusCnt = 0;
                    } else {
                        feedInfo.skusCnt = skusList.length;
                        _.forEach(skusList, function (skuInfo) {
                            var skuDesc = $.trim(skuInfo.sku) + ':' + $.trim(skuInfo.size) + ' -> ' + $.trim(skuInfo.priceClientMsrp) + ', ' + $.trim(skuInfo.priceClientRetail) + ', ' + $.trim(skuInfo.priceNet)
                                + ', ' + $.trim(skuInfo.priceMsrp) + ', ' + $.trim(skuInfo.priceCurrent);
                            feedInfo._popSkuInfo.push(skuDesc);
                        });
                    }

                    // 统计图片数
                    var imgList = feedInfo.image;
                    if (imgList == undefined) {
                        feedInfo.hasImgFlg = 0;
                    } else {
                        feedInfo.hasImgFlg = imgList.length;
                    }

                    // 统计属性数
                    var attsMap = feedInfo.attribute;
                    var attsList = [];
                    if (attsMap != undefined) {
                        var d = attsMap['StoneColor'];
                        angular.forEach(attsMap, function (value, key) {
                            var attsObj = {'aKey': key, 'aValue': value.join('; ')};
                            attsList.push(attsObj);
                        });
                    }
                    feedInfo.attsList = attsList;

                    // 设置勾选框
                    if (feedInfo.updFlg != 8) {
                        tempFeedSelect.currPageRows({"id": feedInfo._id, "code": feedInfo.code});
                    }
                });
                $scope.vm.feedSelList = tempFeedSelect.selectRowsInfo;
            })
        }

        /**
         * 修改feed导入状态
         * @param mark(数字类型) 1: 等待导入,0:不导入
         */
        $scope.updateFeedStatus = function (mark) {
            var selList = $scope.vm.feedSelList.selList;
            if (selList && selList.length == 0 && $scope.vm.searchInfo.isAll != true) {
                alert($translate.instant('TXT_MSG_NO_ROWS_SELECT'));
                return;
            }
            var notice = $scope.vm.searchInfo.isAll ? "您已启动“检索结果全量”选中机制，本次操作对象为检索结果中的所有产品<h3>修改记录数:&emsp;<span class='label label-danger'>" + $scope.vm.feedPageOption.total + "</span></h3>" :
                "您未启动“检索结果全量”选中机制，本次操作对象为检索结果中的已被勾选产品。";
            confirm(notice).then(function () {
                $feedSearchService.updateFeedStatus({
                    'selList': selList,
                    'isAll': $scope.vm.searchInfo.isAll,
                    'status': mark,
                    "searchInfo": $scope.beforSearchInfo
                }).then(function () {
                    if (tempFeedSelect != null) {
                        tempFeedSelect.clearSelectedList();
                    }
                    $scope.vm.searchInfo.isAll = false;
                    search();
                })
            });
        };

        $scope.exportFresh = function exportFresh() {
            exportSearch($scope.vm.exportPageOption.curr);
        };

        function doExport() {
            var data;
            if ($scope.vm.feedSelList.selList && $scope.vm.feedSelList.selList.length > 0) {
                var searchInfo = {"codeList":$scope.vm.feedSelList.selList,"orgChaId":$scope.vm.orgChaId};
                data = {"parameter": JSON.stringify(searchInfo)}
            } else {
                $scope.vm.searchInfo.orgChaId = $scope.vm.orgChaId;
                data = {"parameter": JSON.stringify($scope.vm.searchInfo)}
            }
            $feedSearchService.doExport(data).then(function (data) {
                $scope.vm.exportList.unshift(data.data);
                $scope.vm.currTab.export = true;
                $scope.vm.currTab.group = false;

            })
        }

        function exportSearch(page) {
            $scope.vm.exportPageOption.curr = !page ? $scope.vm.exportPageOption.curr : page;

            $feedSearchService.exportSearch({
                "pageNum": $scope.vm.exportPageOption.curr,
                "pageSize": $scope.vm.exportPageOption.size
            }).then(function (res) {
                $scope.vm.exportList = res.data.exportList;
                _.each($scope.vm.exportList, function (item) {
                    item.fileName = item.fileName.split(",");
                });
                $scope.vm.exportPageOption.total = res.data.exportListTotal;
            })
        }

        $scope.openOtherDownload = function (fileName) {

            $.download.post(cActions.cms.search.$feedSearchService.root + "/" + cActions.cms.search.$feedSearchService.download, {"fileName": fileName});
        };

        $scope.openFeedCategoryMapping = function (popCategoryMul) {
            attributeService.getCatTree()
                .then(function (res) {
                    if (!res.data.categoryTree || !res.data.categoryTree.length) {
                        alert("没数据");
                        return null;
                    }

                    $scope.vm.feedCats = $scope.vm.searchInfo.category ?_.filter($scope.vm.feedCats, function (item) {
                        return $scope.vm.searchInfo.category.indexOf(item.catPath) > -1;
                    }) : '';

                    return popCategoryMul({
                        categories: res.data.categoryTree,
                        from: $scope.vm.feedCats,
                        divType: "-"
                    }).then(function (context) {
                            $scope.vm.feedCats = context;
                            $scope.vm.searchInfo.category = _.map(context, function (item) {
                                return item.catPath;
                            });
                        }
                    );
                });
        };

        $scope.formatStrDate = function (item) {
            item.modified = $filter('date')(new Date(item.modified), 'yyyy-MM-dd HH:mm:ss')
        }

        $scope.popCategoryMapping = function (feedInfo) {

            systemCategoryService.getNewsCategoryList().then(function (res) {
                popups.popupCategoryNew({
                    categories: res.data
                }).then(function (context) {
                    bindCategory(context.selected, feedInfo);
                });
            });

        };

        function bindCategory(category, feedInfo) {
            $feedSearchService.updateMainCategory({"code":feedInfo.code,"mainCategoryInfo":category}).then(function () {
                feedInfo.mainCategoryCn = category.catPath;
                feedInfo.mainCategoryEn = category.catPathEn;
                notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
            })
        }
    };

    searchIndex.$inject = ['$scope', '$routeParams', '$feedSearchService', '$translate', '$q', 'selectRowsFactory', 'confirm', 'alert', 'attributeService', 'cActions', '$sessionStorage', '$filter','cookieService','popups','systemCategoryService','notify'];
    return searchIndex;
});