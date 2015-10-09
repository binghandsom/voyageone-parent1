/**
 * @Name:    searchController.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define (function (require) {
    var accountingApp = require ('modules/oms/oms.module');

    require ('modules/oms/common/common.service');
    require ('modules/oms/orders/orders.service');
    require ('modules/oms/services/orderDetailInfo.service');
    require ('modules/oms/services/accountSearch.service');

    accountingApp.controller ('accountingController', ['$scope', '$rootScope', '$location', 'searchService', 'orderDetailInfoService', 'omsType', 'omsCommonService', 'omsRoute', 'accountingService', 'rateService','omsAction','blockUI','vAlert','$filter',
        function ($scope, $rootScope, $location, searchService, orderDetailInfoService, omsType, omsCommonService, omsRoute, accountingService, rateService, omsAction, blockUI,vAlert, $filter) {
            // 请求根目录，上传文件指定
            var PROJECT_NAME = "/VoyageOne";

            var _ = require ('underscore');

            // 上传汇率初始化
            $scope.input = {};
            // 上传listbox设置初始值
            $scope.input.propertySelected = "";
            $scope.input.shoppingCartSelected = "";
            $scope.input.origStoreId = "";
            $scope.input.origChannelId = "";
            $scope.input.origRate = "";
            $scope.input.origCurrency = "";
            $scope.input.origCalculationError = "";

            // 汇率检索条件初始化
            $scope.search = {};
            // 给所有listbox设置初始值
            $scope.search.propertySelected = "";
            $scope.search.shoppingCartSelected = "";
            // 检索页
            $scope.search.page = 0;
            // 页面大小
            $scope.search.size = 0;

            /* 日期控件定义使用 start */
            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[1];
            /* 日期控件定义使用 end */

            /* rate文件检索设置 start */
            $scope.pageOption = {
                curr: 1,
                size: 10,
                total: 1,
                fetch: reqSearchRate
            };
            /* rate文件检索设置 end */

            /* rate检索按钮 */
            $scope.doSearch = search;
            /* rate上传按钮 */
            $scope.doSaveRate = saveRate;

            // rate文件检索结果 显示标志
            $scope.showHiddenStyle_rateSearchList = false;

            // 页面初始化
            $scope.initialize = function () {
                rateService.doGetCodeList ()
                    .then (function (data) {
                    // 共通下拉列表初始化
                    $scope.currencyList = omsCommonService.doGetOneCodeFromList (data, omsType.currencyType);
                })
                    .then (function () {
                    accountingService.doGetInitData()
                        .then(function (data) {
                        // 设定propertylist.
                        $scope.propertyList = data.propertyList;
                        // 设定cartinfolist，每次切换property需要更新cartlist.
                        $scope.shoppingCartInfoListBackup = data.shoppingCartInfoList;
                        // 检索时间开始
                        $scope.search.searchDateFrom = data.searchDateFrom;
                        // 检索时间终了
                        $scope.search.searchDateTo = data.searchDateTo;

                        doChangeCart();
                    })
                })
            }

            /**
             * 弹出日期控件
             * @param $event
             */
            $scope.open = function ($event, name) {
                $event.preventDefault ();
                $event.stopPropagation ();

                //$scope.opened = true;
                switch (name) {
                    // 订单日期(开始)
                    case "openOrderDateFrom":
                        $scope.fromDateOpened = true;
                        break;
                    // 订单日期(结束)
                    case "openOrderDateTo":
                        $scope.toDateOpened = true;
                        break;
                    default :
                        break;
                }
            };

            /**
             * [检索条件]但property发生变化时调用.
             */
            $scope.$watch ('search.propertySelected', function () {
                doChangeCart ();
            });

            /**
             * [检索条件]判断逻辑用来判断当前property被选中，弹出哪个cart list.
             */
            function doChangeCart () {
                $scope.shoppingCartInfoList = [];

                // 循环shopping cart全部的内容，找出propertyId与store的propertyId一致的那个
                _.each ($scope.shoppingCartInfoListBackup, function (shoppingCartInfo) {
                    if (!_.isEmpty ($scope.search.propertySelected)
                        && _.isEqual ($scope.search.propertySelected, shoppingCartInfo.propertyId)) {
                        $scope.shoppingCartInfoList = shoppingCartInfo.shoppingCartList;
                    }
                });
            }

            /**
             * [上传]但property发生变化时调用.
             */
            $scope.$watch ('input.propertySelected', function () {
                doChangeUploadCart ();
            });

            /**
             * [上传]判断逻辑用来判断当前property被选中，弹出哪个cart list.
             */
            function doChangeUploadCart () {

                //$scope.input.shoppingCartSelected = "?";
                //$scope.input.shoppingCartSelected = undefined;
                //$scope.input.shoppingCartSelected = null;

                $scope.shoppingCartInfoListForInput = [];

                // 循环shopping cart全部的内容，找出propertyId与store的propertyId一致的那个
                _.each ($scope.shoppingCartInfoListBackup, function (shoppingCartInfo) {
                    if (!_.isEmpty ($scope.input.propertySelected)
                        && _.isEqual ($scope.input.propertySelected, shoppingCartInfo.propertyId)) {
                        $scope.shoppingCartInfoListForInput = shoppingCartInfo.shoppingCartList;
                    }
                });

                $scope.input.shoppingCartSelected = "";
            }

            /**
             * 取得当前所有的Store
             * @returns {string}
             */
            function getAllStore () {
                var allStore = [];
                _.each ($scope.propertyList, function (storeInfo) {
                    if (!_.isEmpty (storeInfo.propertyId))
                        allStore.push (storeInfo.propertyId);
                });
                return allStore;
            }

            /**
             * 取得当前所有的cart
             * @returns {Array}
             */
            function getAllCart () {
                var allCart = [];
                _.each ($scope.shoppingCartInfoListBackup, function (shippingCartInfo) {
                    // 如果没有选择property,那么property的channel都查询
                    if (_.isEmpty ($scope.search.propertySelected)) {
                        _.each (shippingCartInfo.shoppingCartList, function (cartInfo) {
                            if (!_.isEmpty (cartInfo.id))
                                allCart.push (cartInfo.id);
                        })
                    } else {
                        // 如果选择了property，只检索该property下面的所有channel
                        if (_.isEqual ($scope.search.propertySelected, shippingCartInfo.propertyId)) {
                            _.each (shippingCartInfo.shoppingCartList, function (cartInfo) {
                                if (!_.isEmpty (cartInfo.id))
                                    allCart.push (cartInfo.id);
                            })
                        }
                    }
                });
                return _.uniq (allCart);
            }

            /**
             * rate文件检索
             * @returns
             */
            function search() {
                $scope.pageOption.curr = 1;
                reqSearchRate();
            }

            /**
             * SettlementFile检索条件设定
             * @returns
             */
            function setSearchCond() {
                $scope.m_blnSearchClicked = true;

                // 选择空白store的时候，传递store数组
                $scope.search.storeId = [];
                if (_.isEmpty ($scope.search.propertySelected)) {
                    $scope.search.storeId = getAllStore ();
                } else {
                    $scope.search.storeId.push ($scope.search.propertySelected);
                }
                // 选择空白cart的时候，传递cart数组
                $scope.search.channelId = [];
                if (_.isEmpty ($scope.search.shoppingCartSelected)) {
                    $scope.search.channelId = getAllCart ();
                } else {
                    $scope.search.channelId.push ($scope.search.shoppingCartSelected);
                }

                var f = "yyyy-MM-dd";
                var dateFilter = $filter("date");
                var format = function (v) {
                    return v ? dateFilter(v, f) : "";
                };

                //处理日期时间
                if (_.isDate ($scope.search.searchDateFrom)) {
                    //$scope.search.searchDateFrom = doFormatDate ($scope.search.searchDateFrom);
                    $scope.search.searchDateFrom = format($scope.search.searchDateFrom);
                }
                if (_.isDate ($scope.search.searchDateTo)) {
                    $scope.search.searchDateTo = format($scope.search.searchDateTo);
                }
            }

            /**
             * SettlementFile检索
             * @returns
             */
            function reqSearchRate() {
                // 检索条件设定
                setSearchCond();

                $scope.search.page = $scope.pageOption.curr - 1;
                $scope.search.size = $scope.pageOption.size;

                rateService
                    .doSearch($scope.search)
                    .then(function (res) {

                        $scope.pageOption.total = res.rows;
                        $scope.rates = res.data;

                        //展示ResultList
                        $scope.showHiddenStyle_rateSearchList = true;
                    });
            }

            /**
             * 汇率上传数据设定
             * @returns
             */
            function saveRate() {

                if (!$scope.frmRate.$valid) {
                    // TODO 显示请输入reason的message.
                } else {
                    // 选择空白store的时候，传递store数组
                    $scope.input.storeId = $scope.input.propertySelected;

                    // 选择空白cart的时候，传递cart数组
                    $scope.input.channelId = $scope.input.shoppingCartSelected;

                    rateService
                        .doSaveRate($scope.input)
                        .then(function (res) {
                            $scope.input.calculationError = res.calculationError;

                            $scope.input.origStoreId = $scope.input.storeId;
                            $scope.input.origChannelId = $scope.input.channelId;
                            $scope.input.origRate = $scope.input.rate;
                            $scope.input.origCurrency = $scope.input.currency;
                            $scope.input.origCalculationError = $scope.input.calculationError;
                        });

                }


            }
        }]);
});
