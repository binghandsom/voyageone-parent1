/**
 * @Name:    searchController.js
 * @Date:    2015/3/26
 *
 * @User:    sky
 * @Version: 1.0.0
 */

define(
    function (require) {
        var searchApp = require('modules/oms/oms.module');
        require('modules/oms/customer/customer.service');
        require('modules/oms/customer/customerDetail.service');
        
        searchApp.controller('customerSearchController', ['$scope', '$rootScope', '$location', 'customerService','customerDetailService',
            function ($scope, $rootScope, $location, customerService, customerDetailService) {
            $scope.c_right_50 = "searchButtonMove";
            // 检索条件
            var SESSION_STORAGE_ORDER_SEARCH = 'orders.search';
            // 表格中显示用的总件数
            $scope.grid_totalServerItems = 0;
            // 表格每页显示的件数（候选值，默认值和当前页数）
            $scope.grid_pagingOptions = {
                pageSizes: [10, 20, 30],
                pageSize: 10,
                currentPage: 1
            };
            
            // page: 当前页数
            $scope.getPagedDataAsync = function (page) {
                setTimeout(function () {
                    // 重新检索
                    $scope.doSearch(page);
                }, 100);
            };
            
            // [每页显示件数]有变化的场合
            $scope.$watch('grid_pagingOptions', function (newVal, oldVal) {
                if ($scope.m_blnSearchClicked) {
                    if (newVal !== oldVal) {
                        $scope.getPagedDataAsync($scope.grid_pagingOptions.currentPage);
                    }
                }
            }, true);
            
        	$scope.grid_columnDefines = [];
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
                    multiSelect: false, 
                    showSelectionCheckbox: false,
                    useExternalSorting: true,
                    columnDefs:'grid_columnDefines'
                };

                // 检索条件结构体
                $scope.search = {
                	customerId: new String(),                //客户Id
                	orderNumber: new String(),               //订单号
                	firstName: new String(),              	 //姓
                	lastName: new String(),                  //名
                	company: new String(),                   //公司 
                	email: new String(),               		 //邮箱
                	city: new String(),               		 //城市
                	state: new String(),               		 //省
                	zip: new String(),               		 //
                	country: new String(),               	 //国家
                	phone: new String()               		 //电话
                };
                
                // 页面初始化
                $scope.initialize = function () {
                
                // 隐藏检索结果
                $scope.showHiddenStyle_SearchList = false;
                // 初始化的时候search按钮是贴着最右边的
                $scope.showPositionStyle_right50 = "";
                // 存放[列]的具体内容
                $scope.grid_columnDefines =
                        [
                            {field: "customerId", displayName:"TXT_CUSTOMER_SEARCH_CUSTMERID", width: '100px'},
                            {field: "lastName", displayName:"GRID_TITLE_TXT_OMS_SEARCH_CUSTOMER_NAME", width: '150px'},
                            {field: "phone", displayName:"TXT_ORDERS_SEARCH_SEARCH_PHONE", width: '130px'},
                            //{field: "lastOrderDate", displayName:"GRID_TITLE_TXT_OMS_SEARCH_CUSTOMER_LASTORDERDATE", width: '250px'},
                            {field: "state", displayName:"GRID_TITLE_TXT_OMS_SEARCH_CUSTOMER_STATE", width: '210px'},
                            {field: "store", displayName: "TXT_ORDERS_SEARCH_SEARCH_STORE", width: '200px'},
                        ];
                customerService.doInit ()
                	.then (function (data) {
                		// 设定propertylist.
            		$scope.propertyList = data.propertyList;
                })
                };
                
                // 检索处理
                $scope.doSearch = function (page) {
                	
                    $scope.m_blnSearchClicked = true;
                    //展示ResultList
                    $scope.showHiddenStyle_SearchList = true;
                    // search按钮要离开最后50像素
                    $scope.showPositionStyle_right50 = $scope.c_right_50;
                    // 当前页码
                    $scope.search.page = page;
                    // 每页显示行数
                    $scope.search.rows = $scope.grid_pagingOptions.pageSize;
                 
                    customerService.doSearch($scope.search, $scope)
                        .then(function (response) {
                            searchSuccessFn(response);
                        })
                        .then(function (response) {
                            $scope.grid_pagingOptions.currentPage = page;
                        })                    	
                        .then(function (response) {
                            // 检索条件Panel收缩
                            $scope.search_items = true;
                        });
                };

                // 检索成功的场合
                function searchSuccessFn(response) {
                    //设置ng-grid控件的数据
                    $scope.setPagingData(
                        response.data.customerList,
                        response.data.total
                    );
                };
                
                //客户检索结果列表的行，双击事件的处理
                $scope.doNgGridDblClick = function (row, source) {
                    var currentCustomerId = $scope.grid_searchListItems[row.rowIndex].customerId;
                    customerDetailService.setCustomerId(currentCustomerId);
                    customerDetailService.setBeforePageUrl($location.path());
                    // 跳转到详细页面
                    $location.path('/oms/customer/customerdetail');
                };
                
                // 检索条件备份
//                function doSaveSearch() {
//                    sessionStorage.setItem(SESSION_STORAGE_ORDER_SEARCH, JSON.stringify($scope.search));
//                };
                
                // 检索条件删除
                function doClearSearch() {
                    sessionStorage.removeItem(SESSION_STORAGE_ORDER_SEARCH);
                };
                
                // 重设表格内容
                $scope.setPagingData = function (data, count) {
                    // 当前页的数据
                    $scope.grid_searchListItems = data;
                    // 用于显示的总件数
                    $scope.grid_totalServerItems = count;
                    if (!$scope.$$phase) {
                        $scope.$apply();
                    }
                };

            }]);
    });
