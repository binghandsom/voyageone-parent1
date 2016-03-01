/**
 * @Name:    searchController.js
 * @Date:    2015/3/2
 *
 * @User:    Tom
 * @Version: 1.0.0
 */

define (function (require) {
    var searchApp = require ('modules/oms/oms.module');

    require ('modules/oms/common/common.service');
    require ('modules/oms/orders/orders.service');
    require ('modules/oms/services/orderDetailInfo.service');
    require ('modules/oms/services/orderSearch.service');

    searchApp.controller ('searchController', ['$scope', '$rootScope', '$location', 'searchService', 'orderDetailInfoService', 'omsType', 'omsCommonService', 'omsRoute', 'searchOrderService',
        function ($scope, $rootScope, $location, searchService, orderDetailInfoService, omsType, omsCommonService, omsRoute, searchOrderService) {

            var _ = require ('underscore');

            // 检索条件初始化
            $scope.search = {};
            // 给所有listbox设置初始值
            $scope.search.propertySelected = "";
            $scope.search.shoppingCartSelected = "";
            $scope.search.orderStatus = "";
            $scope.search.quickFilter = "";
            $scope.search.localShipOnHold = "";
            $scope.search.invoice = "";
            $scope.search.shippingMethod = "";
            $scope.search.paymentMethod = "";
            
            $scope.search.paymentStatus = "";
            $scope.search.transactionStatus = "";
            $scope.search.orderKindOriginal = "";

            $scope.search.extNormalSend = "";
            $scope.search.extCancelSend = "";
            $scope.search.extCancelConfirm = "";
           

            /* 日期控件定义使用 start */
            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };
            $scope.formats = ['dd-MMMM-yyyy', 'yyyy-MM-dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[1];
            /* 日期控件定义使用 end */

            /* 初始化检索表格内容 start */
            // 检索条件：显示
            $scope.searchItemPanel = false;
            // 隐藏检索结果
            $scope.showHiddenStyle_orderSearchList = false;
            // 表格中显示用的总件数
            $scope.grid_totalServerItems = 0;
            // 表格每页显示的件数（候选值，默认值和当前页数）
            $scope.grid_pagingOptions = {
                pageSizes: [10, 20, 50, 100],
                pageSize: 10,
                currentPage: 1
            };
            // 排序信息
            $scope.grid_sortInfo = {
                fields: [''],
                directions: ['']
            };
            // 存放[列]的具体内容
            $scope.grid_selections = [];
            // 存放[列]的具体内容
            $scope.grid_columnDefines =
                [
                    {field: "orderNumber", displayName: "TXT_ORDERS_SEARCH_SEARCH_ORDER_NUMBER", width: '120px',cellTemplate:"<div class=\"ngCellText\" ng-class=\"col.colIndex()\"><a href=\"\" ng-click=\"doNgGridDblClick(row)\"><span ng-cell-text>{{row.getProperty(col.field)}}</span></a></div>"},
                    {field: "sourceOrderId", displayName: "TXT_ORDERS_SEARCH_SEARCH_WEB_ORDER", width: '180px'},
                    {field: "orderDate", displayName: "TXT_ORDERS_SEARCH_SEARCH_ORDER_DATE", width: '110px'},
                    {field: "finalGrandTotal", displayName: "TXT_ORDERS_SEARCH_SEARCH_GRAND_TOTAL_AMOUNT", width: '100px', cellClass: 'text-right'},
                    {field: "paymentBalanceDue", displayName: "TXT_OMS_PAYMENT_BALANCE_DUE", width: '80px', cellClass: 'text-right'},
                    {field: "transactionBalanceDue", displayName: "TXT_OMS_TRANSACTION_BALANCE_DUE", width: '80px', cellClass: 'text-right'},
                    {field: "orderStatus", displayName: "TXT_ORDERS_SEARCH_SEARCH_ORDER_STATUS", width: '120px'},
                    {field: "store", displayName: "TXT_ORDERS_SEARCH_SEARCH_STORE", width: '80px'},
                    {field: "channel", displayName: "TXT_ORDERS_SEARCH_SEARCH_CHANNEL", width: '100px'},
                    {field: "wangwangId", displayName: "TXT_ORDERS_SEARCH_SEARCH_WANGWANG_ID", width: '150px'}
                ];
            // 初始化检索数据
            $scope.grid_searchListItems = [];
            $scope.grid_totalServerItems = 0;
            // 将上面写的所有参数实装到表格中
            $scope.gridOptions = {
                data: 'grid_searchListItems',
                enablePaging: true,
                enableColumnResize: true,
                showFooter: true,
                totalServerItems: 'grid_totalServerItems',
                pagingOptions: $scope.grid_pagingOptions,
                filterOptions: $scope.grid_filterOptions,
                sortInfo: $scope.grid_sortInfo,
                selectedItems: $scope.grid_selections,
                multiSelect: false,                        // TODO: 如果需要有多个项目批量操作的话，这里要改成true的
                showSelectionCheckbox: false,
                useExternalSorting: true,
                columnDefs: 'grid_columnDefines'
            };

            // 自定义内容

            $scope.initialize = function () {

            	 $scope.search.orderKindOriginal=true;
                 $scope.search.orderKindSplit=true;
                 $scope.search.orderKindPresent=true;
                 $scope.search.orderKindExchange=true;
                 $scope.search.orderKindPriceDifference=true;

                $scope.search.extNormalSend=false;
                $scope.search.extCancelSend=false;
                $scope.search.extCancelConfirm=false;

                searchService.doGetCodeList ()
                    .then (function (data) {
                    // 共通下拉列表初始化
                    $scope.quickFilterList = omsCommonService.doGetOneCodeFromList (data, omsType.quickFilter);
                    $scope.orderStatusList = omsCommonService.doGetOneCodeFromList (data, omsType.orderStatus);
                    $scope.localShipOnHoldList = omsCommonService.doGetOneCodeFromList (data, omsType.localShipOnHold);
                    $scope.invoiceList = omsCommonService.doGetOneCodeFromList (data, omsType.invoice);
                    $scope.itemStatusList = omsCommonService.doGetOneCodeFromList (data, omsType.itemStatus);
                    $scope.shippingMethodList = omsCommonService.doGetOneCodeFromList (data, omsType.shippingMethod);
                    $scope.paymentMethodList = omsCommonService.doGetOneCodeFromList (data, omsType.paymentMethod);
                })
                    .then (function () {
                    searchService.doGetInitData ()
                        .then (function (data) {
                        // 设定propertylist.
                        $scope.propertyList = data.propertyList;
                        // 设定cartinfolist，每次切换property需要更新cartlist.
                        $scope.shoppingCartInfoListBackup = data.shoppingCartInfoList;
                        // 检索时间开始
                        $scope.search.orderDateFrom = data.searchDateFrom;
                        // 检索时间终了
                        $scope.search.orderDateTo = data.searchDateTo;

                        doChangeCart ();

                        // TODO 判断如果该页面的检索条件存在则查询
                        if (searchOrderService.getSearchConditionFlag ()) {
                            // 重新展示检索条件
                            $scope.search = searchOrderService.getSearchCondition ();
                            $scope.doSearch ($scope.grid_pagingOptions.currentPage);
                        }
                    })
                })
            };

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
             * 检索满足条件的数据.
             * @param page
             */
            $scope.doSearch = function (page) {
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

                //处理日期时间
                if (_.isDate ($scope.search.orderDateFrom)) {
                    $scope.search.orderDateFrom = doFormatDate ($scope.search.orderDateFrom);
                }
                if (_.isDate ($scope.search.orderDateTo)) {
                    $scope.search.orderDateTo = doFormatDate ($scope.search.orderDateTo);
                }
               
                // 当前页码
                $scope.search.page = page;
                // 每页显示行数
                $scope.search.rows = $scope.grid_pagingOptions.pageSize;

                // 执行检索
                // TODO 是否需要做入力validate check
                searchService.doSearch ($scope.search)
                    .then (function (data) {
                    searchSuccessFn (data);
                });
            };

            /**
             * 但property发生变化时调用.
             */
            $scope.$watch ('search.propertySelected', function () {
                doChangeCart ();
            });

            /**
             * [每页显示件数]有变化的场合
             */
            $scope.$watch ('grid_filterOptions', function (newVal, oldVal) {
                if ($scope.grid_searchListItems.length > 0 && !_.isEqual (newVal, oldVal)) {
                    $scope.doSearch (newVal);
                }
            }, true);

            /**
             * [上一页，下一页，首页，末页]按钮按下，或者[指定页编号]有变话的场合
             */
            $scope.$watch ('grid_pagingOptions', function (newVal, oldVal) {
                if ($scope.grid_searchListItems.length > 0 && !_.isEqual (newVal, oldVal)) {
                    $scope.doSearch ($scope.grid_pagingOptions.currentPage);
                }
            }, true);

            /**
             * 排序内容变了的场合（TODO：现在具体内容还没做）
             */
            $scope.$watch ('grid_sortInfo', function (newVal, oldVal) {
                if ($scope.grid_searchListItems.length > 0 && !_.isEqual (newVal, oldVal)) {
                    $scope.doSearch ($scope.grid_pagingOptions.currentPage);
                }
            }, true);

            /**
             * 订单检索结果一览列表,双击操作跳转到orderDetail页面.
             * @param row
             */
            $scope.doNgGridDblClick = function (row) {

                var orderListInfo = {};
                // 设置传递给order detail 页面的order list.
                orderListInfo.orderInfoListForOrderDetail = [];
                _.each ($scope.grid_searchListItems, function (orderInfo) {
                    orderListInfo.orderInfoListForOrderDetail.push ({"orderNumber": orderInfo.orderNumber, "sourceOrderId": orderInfo.sourceOrderId});
                });
                orderListInfo.searOrderCurrentPage = $scope.grid_pagingOptions.currentPage;
                orderListInfo.searchOrderCurrentPageCount = Math.ceil ($scope.grid_totalServerItems / $scope.grid_pagingOptions.pageSize);
                orderListInfo.searchOrderCurrentPageRows = $scope.grid_pagingOptions.pageSize;
                orderDetailInfoService.setOrderInfoList (orderListInfo);

                // 跳转到order detail 页面.
                var currentSourceOrderId = $scope.grid_searchListItems[row.rowIndex].sourceOrderId;
                orderDetailInfoService.setOrderBeforePageUrl ($location.path ());

                var orderDetailHash = omsRoute.oms_orders_orderdetail.hash.substring (omsRoute.oms_orders_orderdetail.hash.indexOf (':'), omsRoute.oms_orders_orderdetail.hash.length);

                // 跳转到详细页面
                $location.path (omsRoute.oms_orders_orderdetail.hash.replace (orderDetailHash, currentSourceOrderId));
            };

            /**
             * 判断逻辑用来判断当前property被选中，弹出哪个cart list.
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
             * 检索执行成功后的操作.
             * @param data
             */
            function searchSuccessFn (data) {
                // 设置满足条件的检索数据
                $scope.grid_searchListItems = data.ordersInfoList;
                $scope.grid_totalServerItems = data.total;

                // 设置当前页为
                $scope.grid_pagingOptions.currentPage = $scope.search.page;

                // 将检索条件保存在session中.
                searchOrderService.setSearchConditionFlag (true);
                searchOrderService.setSearchCondition ($scope.search);

                // 检索条件隐藏
                $scope.searchItemPanel = true;

                //展示ResultList
                $scope.showHiddenStyle_orderSearchList = true;

                if (!$scope.$$phase) {
                    $scope.$apply ();
                }
            }

            /**
             * 将日期转换为yyyy/MM/dd格式
             * @param data_inicial
             * @returns {*}
             */
            function doFormatDate (data_inicial) {
                try {
                    var date = '0' + data_inicial.getDate ();
                    var month = '0' + (data_inicial.getMonth () + 1); //Months are zero based
                    var year = data_inicial.getFullYear ();

                    date = date.substr (date.length - 2, 2);
                    month = month.substr (month.length - 2, 2);

                    return year + "-" + month + "-" + date;
                } catch (e) {
                    return data_inicial;
                }
            }
        }]);
});
