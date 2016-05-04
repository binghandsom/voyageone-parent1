/**
 * Created by 123 on 2016/3/31.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {

    function searchIndex($scope, $routeParams, $feedSearchService, $translate) {
        $scope.vm = {
            searchInfo: {},
            feedPageOption: {curr: 1, total: 0, fetch: search},
            feedList: []
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;

        /**
         * 初始化数据.
         */
        function initialize() {
            // 默认设置成第一页
            $scope.vm.feedPageOption.curr = 1;
            // 如果是来自category的检索
            if ($routeParams.type == "1") {
                $scope.vm.searchInfo.category = decodeURIComponent($routeParams.value);
            }
            $feedSearchService.init()
            .then(function (res) {
                $scope.vm.masterData = res.data;
            })
            .then(function() {
                if ($routeParams.type == "1") {
                    search();
                }
            })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear() {
            $scope.vm.searchInfo = {};
            // 默认设置成第一页
            $scope.vm.feedPageOption.curr = 1;
        }

        // 显示feed图片
        $scope.openFeedImagedetail = function (idx) {
            var feedObj = $scope.vm.feedList[idx];
            if (feedObj.hasImgFlg > 0) {
                var picList = [];
                picList[0] = feedObj.image;
                var para = {'mainPic': feedObj.image[0], 'picList': picList, 'hostUrl':0};
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

        /**
         * 检索
         */
        function search(page) {
            $scope.vm.feedPageOption.curr = !page ? $scope.vm.feedPageOption.curr : page;
            $scope.vm.searchInfo.pageNum = $scope.vm.feedPageOption.curr;
            $scope.vm.searchInfo.pageSize = $scope.vm.feedPageOption.size;
            if ($scope.vm.searchInfo.fuzzySearch != undefined) {
                $scope.vm.searchInfo.fuzzyList = $scope.vm.searchInfo.fuzzySearch.split("\n");
            }

            $feedSearchService.search($scope.vm.searchInfo).then(function (res) {
                $scope.vm.feedList = res.data.feedList;
                $scope.vm.feedPageOption.total = res.data.feedListTotal;

                _.forEach($scope.vm.feedList, function (feedInfo) {
                    // 统计sku数
                    var skusList = feedInfo.skus;
                    feedInfo._popSkuInfo = [];
                    if (skusList == undefined) {
                        feedInfo.skusCnt = 0;
                    } else {
                        feedInfo.skusCnt = skusList.length;
                        _.forEach(skusList, function (skuInfo) {
                            var skuDesc = $.trim(skuInfo.sku) + ':' + $.trim(skuInfo.size) + ' -> ' + $.trim(skuInfo.priceClientMsrp)  + ', ' + $.trim(skuInfo.priceClientRetail) + ', ' + $.trim(skuInfo.priceNet)
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
                        angular.forEach(attsMap, function(value, key) {
                            var attsObj = {'aKey': key, 'aValue': value.join('; ')};
                            attsList.push(attsObj);
                        });
                    }
                    feedInfo.attsList = attsList;
                });
            })
        }

    };

    searchIndex.$inject = ['$scope','$routeParams','$feedSearchService','$translate'];
    return searchIndex;
});