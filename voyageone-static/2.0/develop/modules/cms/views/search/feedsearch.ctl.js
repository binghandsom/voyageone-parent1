/**
 * Created by 123 on 2016/3/31.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {

    function searchIndex($scope, $routeParams, $feedSearchService, $translate) {
        $scope.vm = {
            searchInfo: {
                compareType: null,
                brand: null,
                category: null
            },
            feedPageOption: {curr: 1, total: 0, size: 20, fetch: search},
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
            $feedSearchService.init()
                .then(function (res) {
                    $scope.vm.masterData = res.data;
                })
        }

        /**
         * 清空画面上显示的数据
         */
        function clear() {
            $scope.vm.searchInfo = {
                compareType: null,
                brand: null,
                category: null
            };
            // 默认设置成第一页
            $scope.vm.feedPageOption.curr = 1;
        }

        // 显示feed图片
        $scope.openFeedImagedetail = function (idx) {
            var feedObj = $scope.vm.feedList[idx];
            if (feedObj.hasImgFlg > 0) {
                if (feedObj.hasImgFlg == 1) {
                    $routeParams.imageMain = feedObj.image[0];
                    $routeParams.imageList = [];
                } else {
                    $routeParams.imageMain = feedObj.image[0];
                    $routeParams.imageList = feedObj.image.slice(1, feedObj.image.length);
                }
                return this.openImagedetail();
            }
        };

        // 显示feed属性
        $scope.openFeedCodeDetail = function (idx) {
            var feedObj = $scope.vm.feedList[idx];
            if (feedObj.attsList.length == 0) {
                return;
            }
            $routeParams.attsList = feedObj.attsList;
            this.openCodeDetail();
        };

        /**
         * 检索
         */
        function search() {
            // 对应根据父类目检索
            var catInfo = _getCatPath($scope.vm.searchInfo.category);
            if (catInfo) {
                $scope.vm.searchInfo.catPath = catInfo.catPath;
            } else {
                $scope.vm.searchInfo.catPath = null;
            }
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
                            var skuDesc = 'MSRP(USD): ' + $.trim(skuInfo.price_client_msrp)  + ',  Retail Price(USD): ' + $.trim(skuInfo.price_client_retail) + ',  Cost Price(USD): ' + $.trim(skuInfo.price_net)
                                + ',  MSRP(RMB): ' + $.trim(skuInfo.price_msrp) + ',  Retail Price(RMB): ' + $.trim(skuInfo.price_current);
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

        function _getCatPath (catId) {
            return _.findWhere($scope.vm.masterData.categoryList, {catId: catId});
        }

    };

    searchIndex.$inject = ['$scope','$routeParams','$feedSearchService','$translate'];
    return searchIndex;
});